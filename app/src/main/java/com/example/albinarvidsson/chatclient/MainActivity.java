package com.example.albinarvidsson.chatclient;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;


public class MainActivity extends AppCompatActivity {
WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void connectWebSocket(){
        URI uri = null;

        try {
            uri = new URI("ws:/192.168.1.7:8080/ChatServer-master/");
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " Gustav o Albin " + Build.MODEL);
            }

            @Override
            public void send(String text) throws NotYetConnectedException {
                super.send(text);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.messages);
                        textView.setText(textView.getText()+ "\n"+ message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed "+ s);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("Websocket", "Error " + ex.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
    public void sendMessage(View view){
        EditText editText = (EditText)findViewById(R.id.message);
        mWebSocketClient.send(editText.getText().toString());
        editText.setText("");
    }

    public void buttonOnclick(View v){
        try {
            TextView textView = (TextView) findViewById(R.id.messages);
            EditText editText = (EditText) findViewById(R.id.message);
            textView.append(editText.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

