import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {

    ServerSocket serverSocket; // server
    Socket socket; // client

    BufferedReader bufferedReader; // for reading
    PrintWriter printWriter; // for writting

    Server() {
        try {

            serverSocket = new ServerSocket(5555);
            System.out.println("waiting for connection ......");

            socket = serverSocket.accept(); // establishing connection from client to server ...
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // input stream
            printWriter = new PrintWriter(socket.getOutputStream()); // output stream

            // invoke both methods simultaneously by Multi-threading
            readingDataFromClient();
            writingDataFromServerToClient();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readingDataFromClient() {
        Runnable runnable1 = () -> {

            System.out.println("Reader started ! ....");

            while (true) {
                try {

                    String clientMessage = bufferedReader.readLine();

                    if (clientMessage.equals("exit") || clientMessage.equals("bye")) {
                        System.out.println("Client terminated the chat ! ...");
                        break;
                    }

                    System.out.println("Client : " + clientMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        new Thread(runnable1).start();

    }

    public void writingDataFromServerToClient() {

        Runnable runnable2 = () -> {

            System.out.println("Writter started ! ...");

            while (true) {
                try {
                    BufferedReader readFromConsole = new BufferedReader(new InputStreamReader(System.in)); // reading
                                                                                                           // from
                                                                                                           // keyboard
                    String serverMessage = readFromConsole.readLine();
                    printWriter.println(serverMessage);
                    printWriter.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable2).start();
    }

    public static void main(String[] args) {
        new Server();

    }
}
