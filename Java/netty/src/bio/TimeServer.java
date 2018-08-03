package bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by admin on 2018/8/3.
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("Time server is start, port: " + port);
            while (true) {
                // 主线程阻塞在accept操作上
                Socket socket = server.accept();
                // 创建一个新线程处理客户端的socket链路
                new Thread(new TimeServerHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                System.out.println("The time server closed...");
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class TimeServerHandler implements Runnable {
        private Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);
                String body;
                while (true) {
                    body = in.readLine();
                    if (body == null) {
                        // 读取结束
                        break;
                    }
                    System.out.println("The server receive msg body: " + body);
                    String currentTime = "QUERY TIME ORDER".equals(body) ? new Date().toString() : "BAD ORDER";
                    out.println(currentTime);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    in = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }

                if (this.socket != null) {
                    try {
                        this.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.socket = null;
                }
            }
        }
    }
}
