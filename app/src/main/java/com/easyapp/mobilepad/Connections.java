package com.easyapp.mobilepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Connections extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        if (savedInstanceState != null) return;

        int profileId = getIntent().getExtras().getInt("profile", -1);
        ConnectionsListFragment connectionsFragment = ConnectionsListFragment.newInstance(profileId);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, connectionsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connections, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, settingsFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.action_logout:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
