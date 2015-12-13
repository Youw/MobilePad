package com.easyapp.mobilepad;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connections extends ActionBarActivity {

    EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        textView = (EditText)findViewById(R.id.result_text);

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
        String outText = textView.getText().toString();
        try {
            String inText = new SendData().execute(outText).get();
            textView.setText(inText == null ? "Failed to receive data or null" : inText);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public class SendData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Socket socket = null;
            DataInputStream in = null;
            DataOutputStream out = null;
            String result = null;

            try {
                socket = new Socket("192.168.1.36", 8887);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                out.writeBytes(params[0]+"\n");
                result = in.readUTF();
            } catch (Exception e){
                result = e.getMessage();
            }
            finally {
                if(socket!=null){
                    try{
                        socket.close();
                    } catch (IOException e) {
                        result += e.getMessage();
                    }
                }
            }
            return result;
        }
    }
}
