import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class OFICQServer {
    ServerSocket server;
    ArrayList<Socket> clientsList = new ArrayList<>();

    public ServerSocket serverStart(String portBuf, JTextArea outputArea) throws IOException {
        if (portBuf.length() == 0) {
            outputArea.append("请先输入端口号!\n");
            return null;
        }
        int ports = Integer.parseInt(portBuf);
        if (ports < 0 || ports > 65535) {
            outputArea.append("非法端口:" + ports + " 请重新设置端口号\n");
        }
        outputArea.append("服务器启动中...\n");

        return new ServerSocket(ports);
    }

    public void connectClient(JTextArea text, JTextArea list) {
        if(server==null) return; //serverStart fail

        if (clientsList.size() == 1) {
            privateConnect(text);
        }
        else if(clientsList.size()>=2) {
            groupConnect(text,list);
        }
    }

    public void privateConnect(JTextArea text){
        Socket singleClient;
        //noinspection UnusedAssignment
        singleClient = clientsList.get(0);
        try {
            singleClient = server.accept();

            Socket finalSingleClient = singleClient;
            @SuppressWarnings("Convert2Lambda") Runnable r = new Runnable() {
                @SuppressWarnings("InfiniteLoopStatement")
                @Override
                public void run() {
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while (true) {
                        try {
                            InputStream is = finalSingleClient.getInputStream();
                            while (len == is.read(bytes)) {
                                text.append(new String(bytes, 0, len) + '\n');
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            };
            Thread thread = new Thread(r);
            thread.start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void groupConnect(JTextArea text,JTextArea list) {
        byte[] bytes = new byte[1024];
        int len = 0;
        while (true){
            Socket socket = null;
            try {
                socket = server.accept();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            synchronized (Collections.unmodifiableList(clientsList)){
                clientsList.add(socket);
            }

            Socket finalSocket = socket;
            @SuppressWarnings("Convert2Lambda") Runnable r = new Runnable() {
                @Override
                public void run() {
                    String ip = finalSocket.getInetAddress().getHostAddress();
                    list.append(ip+'\n');
                    text.append(ip+"已连接"+'\n');
                    try {
                        InputStream is = finalSocket.getInputStream();
                        while (len == is.read(bytes)){
                            text.append(new String(bytes,0,len)+'\n');
                            send2All(new String(bytes,0,len)+'\n');
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        text.append(ip+"离线"+'\n');
                        synchronized (Collections.unmodifiableList(clientsList)){
                            clientsList.remove(finalSocket);
                        }
                    }
                }
            };
            Thread thread = new Thread(r);
            thread.start();
        }
    }

    public void sendData(String msg,int clientNum) {
        String username = "server";
        String clientside = "client";
        try {
            OutputStream os = clientsList.get(clientNum).getOutputStream();
            os.write((username +" to "+clientside+": " + msg).getBytes(StandardCharsets.UTF_8));
            System.out.println("Message Sent.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void send2All(String msg){
        synchronized (Collections.unmodifiableList(clientsList)){
            for (Socket s:clientsList){
                try {
                    OutputStream os = s.getOutputStream();
                    os.write((msg).getBytes(StandardCharsets.UTF_8));
                    System.out.println("Message Sent.");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    ArrayList<Socket> getClientsList() {
        return clientsList;
    }

//    void setClientsList(ArrayList<Socket> clientsList) {
//        this.clientsList = clientsList;
//    }
}
