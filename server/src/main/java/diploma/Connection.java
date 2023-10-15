package diploma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class Connection extends Thread {
    private static final String SETTINGS = "settings.txt";
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Logger logger = Logger.getInstance();
    private final String connectionStartMessage;
    private final String connectionClosedMessage;
    private final String PREFIX = FilesReaderService.getSettingByKey(SETTINGS, "prefix");

    public Connection(Socket clientSocket, PrintWriter out, BufferedReader in) throws InterruptedException {
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;

        connectionStartMessage = "Новое соединение установлено. Порт: " + clientSocket.getPort() + ".";
        connectionClosedMessage = "Соединение закрыто. Порт: " + clientSocket.getPort() + ".";

        start();
    }

    @Override
    public void run() {
        try {
            try {
                System.out.println(connectionStartMessage);

                String username = "";
                String message;

                while (true) {
                    if (username.isEmpty()) {
                        out.println("Write your name: ");

                        username = in.readLine();

                        this.logger.log(new Message(
                            MessageType.SERVER,
                            username,
                            connectionStartMessage,
                            new Date().toString()));

                        for (Connection connection : Server.connectionsList) {
                            connection.send(username, "подключился!");
                        }
                    } else {
                        out.println("Write your message: ");

                        message = in.readLine();

                        for (Connection connection : Server.connectionsList) {
                            connection.send(username, message);
                        }

                        if (message.equals("exit")) {
                            out.write("closed");

                            this.logger.log(new Message(
                                MessageType.SERVER,
                                username,
                                connectionClosedMessage,
                                new Date().toString()));

                            break;
                        }

                        this.logger.log(new Message(
                            MessageType.CLIENT,
                            username,
                            message,
                            new Date().toString()));
                    }
                }
            } finally {
                clientSocket.close();
                in.close();
                out.close();

                System.out.println(connectionClosedMessage);
            }
        } catch (IOException ignored) {
        }
    }

    private void send(String username, String message) {
        out.println(PREFIX + " " + username + ": " + message);
    }
}