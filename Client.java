import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    Socket socket;

    BufferedReader bufferedReader; // for reading
    PrintWriter printWriter; // for writting

    Client() {
        try {
            System.out.println("Establishing connection with server ....");
            socket = new Socket("127.0.0.1", 5555);
            System.out.println("Connnection done  ! ....");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // input stream
            printWriter = new PrintWriter(socket.getOutputStream()); // output stream

            // invoke both methods simultaneously by Multi-threading
            readingDataFromServer();
            writingDataFromClientToServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingDataFromServer() {
        Runnable runnable1 = () -> {

            System.out.println("Reader started ! ....");

            while (true) {
                try {

                    String ServerMessage = bufferedReader.readLine();

                    if (ServerMessage.equals("exit") || ServerMessage.equals("bye")) {
                        System.out.println("Server terminated the chat ! ...");
                        break;
                    }

                    System.out.println("Server : " + ServerMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        new Thread(runnable1).start();

    }

    public void writingDataFromClientToServer() {

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
        System.out.println("this is client");

        new Client();

    }

}
