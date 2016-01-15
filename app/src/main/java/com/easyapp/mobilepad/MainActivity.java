package com.easyapp.mobilepad;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.easyapp.mobilepad.datacontract.Connection;

public class MainActivity extends Activity {

    private static TCPSocketConnection tcpConnection = null;
    private static DBConnection dbConnection =  null;
    private boolean mBackPressed;

    private Integer mProfileId = 0;
    private String mCurrentPreset;
    private Connection mCurrentConnection;

    enum ChildFragment {
        PRESETS,
        CONNECTIONS,
        SETTINGS,
        CONNECTING,
        PRESET
    }
    private ChildFragment mCurrentFragment = ChildFragment.PRESETS;
    private ChildFragment mBackFragment = ChildFragment.PRESETS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBackPressed = false;
        setContentView(R.layout.activity_main);

        if (dbConnection == null) {
            dbConnection = SQLiteDBConnection.getInstance(getApplicationContext());
        }

        if (savedInstanceState == null) {
            mProfileId = getIntent().getExtras().getInt("profile", -1);
            showPresets();
        } else {
            mProfileId = savedInstanceState.getInt("profile_id");
            mBackFragment = (ChildFragment) savedInstanceState.get("back_fragment");
            mCurrentFragment = (ChildFragment) savedInstanceState.get("current_fragment");
            mCurrentConnection = (Connection) savedInstanceState.get("current_connection");
            mCurrentPreset = savedInstanceState.getString("current_preset");
            switch (mCurrentFragment) {
                case PRESETS:
                    showPresets();
                    break;
                case CONNECTIONS:
                    showConnections();
                    break;
                case SETTINGS:
                    mCurrentFragment = mBackFragment;
                    showSettings();
                    break;
                case CONNECTING:
                    showConnecting();
                    break;
                case PRESET:
                    showCurrentPreset();
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("profile_id", mProfileId);
        outState.putSerializable("current_fragment", mCurrentFragment);
        outState.putSerializable("back_fragment", mBackFragment);
        outState.putSerializable("current_connection", mCurrentConnection);
        outState.putSerializable("current_preset", mCurrentPreset);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        if (mCurrentFragment == ChildFragment.SETTINGS) {
            switch (mBackFragment) {
                case PRESETS:
                    showPresets();
                    break;
                case CONNECTIONS:
                    showConnections();
                    break;
                case SETTINGS:
                    showPresets(); // whatever
                    break;
            }
        } else if (mCurrentFragment == ChildFragment.CONNECTIONS) {
            showPresets();
        } else {
            if (mBackPressed) {
                finish();
            } else {
                mBackPressed = true;
                Toast.makeText(getApplicationContext(), R.string.press_back_to_quit, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCurrentFragment != ChildFragment.CONNECTING) {
            getMenuInflater().inflate(R.menu.menu_connections, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                showSettings();
                return true;
            case R.id.action_logout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        mBackPressed = false;
        setTitle(getString(R.string.settings));
        if (mCurrentFragment != ChildFragment.SETTINGS) {
            mBackFragment = mCurrentFragment;
        }
        mCurrentFragment = ChildFragment.SETTINGS;
        invalidateOptionsMenu();

        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                .replace(R.id.fragment_container, settingsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showPresets() {
        mBackPressed = false;
        setTitle(getString(R.string.choose_preset_layout));
        mCurrentFragment = ChildFragment.PRESETS;
        invalidateOptionsMenu();

        PresetsFragment presetsFragment = PresetsFragment.newInstance(mProfileId, dbConnection);
        presetsFragment.setPresetClickListener(new PresetsFragment.PresetClickListener() {
            @Override
            public void onClick(String presetName) {
                mCurrentPreset = presetName;
                showConnections();
            }
        });
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, presetsFragment)
                .commit();
    }

    private void showConnections() {
        mBackPressed = false;
        setTitle(getString(R.string.connect_to_host));
        mCurrentFragment = ChildFragment.CONNECTIONS;
        invalidateOptionsMenu();

        ConnectionsListFragment connectionsFragment = ConnectionsListFragment.newInstance(mProfileId, dbConnection);
        connectionsFragment.setConnectionClickListener(getConnectionClickListener());
        getFragmentManager().beginTransaction()
                .setCustomAnimations( R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, connectionsFragment)
                .commit();
    }

    private void showConnecting() {
        mBackPressed = false;
        setTitle(getString(R.string.connecting_to) + " " + mCurrentConnection.toString());
        mCurrentFragment = ChildFragment.CONNECTING;
        invalidateOptionsMenu();

        ConnectingProgress connectionProgressFragment = ConnectingProgress.newInstance();
        getFragmentManager().beginTransaction()
                .setCustomAnimations( R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, connectionProgressFragment)
                .commit();
    }

    private ConnectionsListFragment.ConnectionClickListener getConnectionClickListener() {
        return new ConnectionsListFragment.ConnectionClickListener() {
            @Override
            public void onClick(Connection connection) {
                mCurrentConnection = connection;
                showConnecting();
                tcpConnection = new TCPSocketConnection(mCurrentConnection.getHost(), mCurrentConnection.getPort());
                tcpConnection.setListener(newConnectionListener());
                tcpConnection.start();
            }
        };
    }

    TCPSocketConnection.TCPConnectionListener newConnectionListener() {
        return new TCPSocketConnection.TCPConnectionListener() {
            @Override
            public void onRead(String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onConnected(boolean connected_) {
                final boolean connected = connected_;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!connected) {
                            showConnections();
                            Toast.makeText(getApplicationContext(), R.string.connecting_error, Toast.LENGTH_LONG).show();
                        } else {
                            showCurrentPreset();
                        }
                    }
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tcpConnection = null;
                        showConnections();
                        Toast.makeText(getApplicationContext(), R.string.disconnected, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
    }

    private void showCurrentPreset() {
        if (mCurrentPreset.equals("Preset1")) {
            showPreset1();
        } else if (mCurrentPreset.equals("Preset2")) {
            showPreset2();
        } else {
            //
        }
    }

    private void showPreset2() {
        showPreset1();
    }

    private void showPreset1() {
        mBackPressed = false;
        setTitle("Preset1");
        mCurrentFragment = ChildFragment.PRESET;
//
        Preset1Fragment connectionProgressFragment = Preset1Fragment.newInstance(new RemoteInputEmulator(tcpConnection));
        getFragmentManager().beginTransaction()
                .setCustomAnimations( R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, connectionProgressFragment)
                .commit();
    }
}
