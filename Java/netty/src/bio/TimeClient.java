package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by admin on 2018/8/3.
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        String ip = "127.0.0.1";
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
                if (args.length > 1) {
                    ip = String.valueOf(args[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        new Thread(new ClientHandler(ip, port, 1)).start();
        for (int i = 0; i < 1000; i++) {
            new Thread(new ClientHandler(ip, port, i + 1)).start();
        }
    }

    private static class ClientHandler implements Runnable {
        private String ip;
        private int port;
        private int id;

        public ClientHandler(String ip, int port, int id) {
            this.ip = ip;
            this.port = port;
            this.id = id;
        }

        @Override
        public void run() {
            Socket socket = null;
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                socket = new Socket(ip, port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    out = new PrintWriter(socket.getOutputStream(), true);
                    send(in, out);
                    send(in, out);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    in = null;
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        }

        private void send(BufferedReader in, PrintWriter out) throws IOException {
            out.println("QUERY TIME ORDER");
            System.out.println("Client " + this.id + " Send order to server success.");

            String body;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    // 读取结束
                    break;
                }
                System.out.println("Client " + this.id + " receive Now is: " + body);
            }
        }

    }
}
