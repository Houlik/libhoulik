package com.houlik.libhoulik.houlik;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocket{

    private Socket socket;
    private String host;
    private int port;
    private OutputStream outputStream;
    private InputStream inputStream;
    private OnSocket onSocket;

    public ClientSocket(final String host, final int port){
        this.host = host;
        this.port = port;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(host, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMsg(final String data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream = socket.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void recMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if(socket.isConnected()) {
                            inputStream = socket.getInputStream();
                            byte[] bytes = new byte[1024];
                            int len = inputStream.read(bytes);
                            onSocket.recMsg(new String(bytes, 0, len));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Socket getSocket(){
        return socket;
    }

    public interface OnSocket{
        public void recMsg(String data);
    }

    public void setOnSocket(OnSocket onSocket){
        this.onSocket = onSocket;
    }
}
