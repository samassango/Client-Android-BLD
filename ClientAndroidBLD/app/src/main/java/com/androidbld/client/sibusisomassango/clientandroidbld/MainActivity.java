package com.androidbld.client.sibusisomassango.clientandroidbld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidbld.client.sibusisomassango.clientandroidbld.connectionDAO.Client;

public class MainActivity extends AppCompatActivity {
    private Button btnConnectToServer;
    private TextView serverResults;
    //Client client = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //text view to display server response.
        serverResults = (TextView) findViewById(R.id.rsServerResponse);

        //button to connect to the server.
        btnConnectToServer = (Button) findViewById(R.id.btnConnectToServer);



        btnConnectToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = new Client("196.37.22.179",9011,serverResults);

                client.execute();

                client.readFile("response.xml");
            }
        });
    }
}
