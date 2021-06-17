import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import java.awt.BorderLayout;

public class Client extends JFrame {
    Socket socket;

    BufferedReader bufferedReader; // for reading
    PrintWriter printWriter; // for writting

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    Client() {
        try {
            System.out.println("Establishing connection with server ....");
            socket = new Socket("127.0.0.1", 5555);
            System.out.println("Connnection done  ! ....");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // input stream
            printWriter = new PrintWriter(socket.getOutputStream()); // output stream

            createGUI();
            handleEvents();

            // invoke both methods simultaneously by Multi-threading
            readingDataFromServer();
            writingDataFromClientToServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        this.setTitle("Client Messager[END]");
        this.setSize(400, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("gig.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(heading, BorderLayout.NORTH);
        this.add(messageArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

    }

    public void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

                System.out.println("Key release " + e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("you have pressed Enter button");

                    String ContentToSend = messageInput.getText();
                    messageArea.append("Me : " + ContentToSend + "\n");
                    printWriter.println(ContentToSend);
                    printWriter.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }

            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

        });

    }

    public void readingDataFromServer() {
        Runnable runnable1 = () -> {

            System.out.println("Reader started ! ....");

            while (true) {
                try {

                    String ServerMessage = bufferedReader.readLine();

                    if (ServerMessage.equals("exit") || ServerMessage.equals("bye")) {
                        System.out.println("Server terminated the chat ! ...");
                        JOptionPane.showMessageDialog(this, "server terminated the chat");
                        messageInput.setEnabled(false);
                        break;
                    }

                    // System.out.println("Server : " + ServerMessage);

                    messageArea.append("server :" + ServerMessage + "\n");
                    JOptionPane.showMessageDialog(this, "server terminated the chat");

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
