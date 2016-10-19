package com.androidbld.client.sibusisomassango.clientandroidbld.connectionDAO;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public Client(String address, int port,TextView serverResponse) {
        serverAddress = address;
        serverPort = port;
        this.serverResponse=serverResponse;
    }
    @Override
    protected Void doInBackground(Void... params) {

        Socket socket = null;
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            //starting socket connection to server.
            socket = new Socket(serverAddress, serverPort);

            //writing to socket using object out put stream.
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeObject("Authentication");
            objectOutputStream.writeObject("12345");
            objectOutputStream.writeObject("12345");
            objectOutputStream.writeObject("ABCDE");
            objectOutputStream.writeObject("ABCDE");
            objectOutputStream.writeObject("Users");
            objectOutputStream.flush();

            //read the server response message
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            try{
               response = (String) objectInputStream.readObject();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
                response = "ClassNotFoundException "+e.getMessage();
            }


        } catch (UnknownHostException e) {
            // handling Unknown Host exception if the ip address doesn't exist.
            e.printStackTrace();
            response = "UnknownHostException "+e.getMessage();
        } catch (IOException e) {
            // handling input output exception.
            e.printStackTrace();
            response = "IOException "+e.getMessage();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // handling input output exception when closing the socket.
                    e.printStackTrace();

                    response = "IOException "+e.getMessage();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        serverResponse.setText(response);
        super.onPostExecute(aVoid);
    }
}
