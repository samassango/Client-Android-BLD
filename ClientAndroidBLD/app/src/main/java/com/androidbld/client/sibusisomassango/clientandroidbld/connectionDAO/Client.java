package com.androidbld.client.sibusisomassango.clientandroidbld.connectionDAO;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by sibusisomassango on 2016/10/19.
 */
public class Client extends AsyncTask<Void, Void, Void> {
    String serverAddress;
    int serverPort;
    String response = "";
    TextView serverResponse;

    String serResponse = null;
    String fileName ="message.xml" ;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/client/" ;

    public Client(String address, int port,TextView serverResponse) {
        serverAddress = address;
        serverPort = port;
        this.serverResponse=serverResponse;
        String data = "<request>\n" +
                "    <EventType>Authentication</EventType>\n" +
                "    <event>\n" +
                "        <UserPin>12345</UserPin>\n" +
                "        <DeviceId>12345</DeviceId>\n" +
                "        <DeviceSer>ABCDE</DeviceSer>\n" +
                "        <DeviceVer>ABCDE</DeviceVer>\n" +
                "        <TransType>Users</TransType>\n" +
                "    </event>\n" +
                "</request>";

      saveToFile(data);
    }
    @Override
    protected Void doInBackground(Void... params) {

        Socket socket = null;
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;



        try {

            //starting socket connection to server.
            socket = new Socket(serverAddress, serverPort);

            //Wait for the server to accept connection before reading the xml file
            BufferedReader reader = new BufferedReader(new FileReader(path+fileName));

            String line;

            StringBuilder stringBuilder = new StringBuilder();

            while((line = reader.readLine())!=null){
                stringBuilder.append(line);
            }

            //Send xml data to server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            writer.println(stringBuilder.toString());
            writer.flush();
            Log.d("Results sent",stringBuilder.toString());
//
//            writer.close();

            //Wait for server response
            BufferedReader serverReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            while ((serResponse = serverReader.readLine()) != null) {
                Log.d("Server Response: " , serResponse);
                response = serResponse;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("RESULTS",response);
        serverResponse.setText(serResponse);
        super.onPostExecute(aVoid);
    }

    public boolean saveToFile( String data){
        try {
            new File(path  ).mkdir();
            File file = new File(path+ fileName);
            if (!file.exists()) {
                file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
        }
            return true;
        }  catch(FileNotFoundException ex) {
            Log.d("FileNotFoundException", ex.getMessage());
        }  catch(IOException ex) {
            Log.d("IOException", ex.getMessage());
        }
        return  false;


    }

}
