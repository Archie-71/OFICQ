import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class OFICQClient {
    private Socket socket = new Socket();


    public void connect(JTextArea text,String portBuf,String IPBuf) {
        if (portBuf.length() == 0) {
            text.append("请先输入端口号!\n");
            return;
        }
        int ports = Integer.parseInt(portBuf);
        if (ports < 0 || ports > 65535) {
            text.append("非法端口:" + ports + " 请重新设置端口号\n");
        }

        if (IPBuf.length() == 0) {
            text.append("请先输入IP地址!\n");
            return;
        }
        if (!IPBuf.matches("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}")) {
            text.append("非法IP:" + IPBuf + " 请重新设置IP地址\n");
        }
        text.append("客户端连接中...\n");

        OutputStream os;
        try {
            if (socket == null) {
                text.append("连接失败,请稍后重试.\n");
            } else {
                os = socket.getOutputStream();
                os.write("连接成功.\n".getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        @SuppressWarnings("Convert2Lambda") Runnable r = new Runnable() {
            @SuppressWarnings("InfiniteLoopStatement")
            @Override
            public void run() {
                while (true) {
                    try {
                        InputStream is = socket.getInputStream();
                        int len;
                        byte[] bytes = new byte[1024];
                        while ((len = is.read(bytes)) != -1) {
                            String str = new String(bytes, 0, len);
                            text.append(str + '\n');
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }

    public void sendData(String msg) {
        String username = "client";

        System.out.println("Sent.");
        try {
            OutputStream os = socket.getOutputStream();
            os.write((username + ":" + msg).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
