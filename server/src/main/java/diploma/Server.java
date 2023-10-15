package diploma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    private static final String SETTINGS = "settings.txt";
    private static final String PORT = FilesReaderService.getSettingByKey(SETTINGS, "port");
    static final LinkedList<Connection> connectionsList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        try (
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PORT))
        ) {
            System.out.println("Сервер запущен!");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    Connection connection = new Connection(clientSocket, out, in);

                    connectionsList.add(connection);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println("Сервер закрыт!");
        }
    }
}