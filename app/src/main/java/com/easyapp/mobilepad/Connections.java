package com.easyapp.mobilepad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Connections extends AppCompatActivity {

    EditText textView;
    TextView log;
    TCPSocketConnection connection;
    RemoteInputEmulator inputEmulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        textView = (EditText)findViewById(R.id.result_text);
        log = (TextView)findViewById(R.id.log);
        connection = new TCPSocketConnection("192.168.1.36");
        connection.setListener(new TCPSocketConnection.SocketListener() {
            @Override
            public void onRead(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        log.append(data + "\n");
                    }
                });
            }
        });
        connection.start();
        inputEmulator = new RemoteInputEmulator(connection);
    }

    @Override
    public void onDestroy() {
        connection.interrupt();
        try {
            connection.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connections, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendPacket(View v){
        String outText = textView.getText().toString() + "\n";
        connection.send(outText);
        log.append("sent: " + outText + "\n");
        textView.setText("");
    }
}
