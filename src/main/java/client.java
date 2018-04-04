import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class client {
    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        BufferedReader bufferedReaderGetText = null, bufferedReaderInputStream = null;
        Socket socket = null;
        PrintWriter printWriterOutputStream = null;
        String line = null;
        try {
            socket = new Socket(address, 2233);
            System.out.println("** start client to connection **");
            bufferedReaderGetText = new BufferedReader(new InputStreamReader(System.in));
            bufferedReaderInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriterOutputStream = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
        String response = null;
        try {
            line = bufferedReaderGetText.readLine();
            while (line.compareTo("QUIT") != 0) {
                printWriterOutputStream.println(line);
                printWriterOutputStream.flush();
                response = bufferedReaderInputStream.readLine();
                System.out.println("Server Response: " + response);
                line = bufferedReaderGetText.readLine();
            }
        } catch (IOException e) {
            System.out.println("Socket read Error");
        } finally {
            bufferedReaderInputStream.close();
            printWriterOutputStream.close();
            bufferedReaderGetText.close();
            socket.close();
            System.out.println("Connection Closed");
        }
    }
}
