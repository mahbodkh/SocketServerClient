import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        Map<String, String> clients = new HashMap<String, String>();
        ServerSocket server = null;
        try {
            server = new ServerSocket(2233);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");
        }
        System.out.println("**  now server ready for accept **");
        while (true) {
            try {
                Socket socket = server.accept();
                System.out.println("** now client connected **");
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }
    }

    static class ServerThread extends Thread {
        String line = null;
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("IO error in server thread");
                e.printStackTrace();
            }
            try {
                line = bufferedReader.readLine();
                while (line.compareTo("QUIT") != 0) {
                    printWriter.println(line);
                    printWriter.flush();
                    System.out.println("Response to Client : " + line);

                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                System.out.println("IO Error/ Client " + line + " terminated abruptly");
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("Connection Closing ... ");
                    if (bufferedReader != null) {
                        bufferedReader.close();
                        System.out.println(" Socket Input Stream Closed");
                    }
                    if (printWriter != null) {
                        printWriter.close();
                        System.out.println("Socket Out Closed");
                    }
                    if (socket != null) {
                        socket.close();
                        System.out.println("Socket Closed");
                    }
                } catch (IOException e) {
                    System.out.println("Socket Close Error");
                    e.printStackTrace();
                }
            }

        }
    }
}
