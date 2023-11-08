package diploma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final Socket clientSocket;
    private final ClientRead clientRead;
    private final ClientWrite clientWrite;
    private static final String SETTINGS = "settings.txt";
    static final String PORT = FilesReaderService.getSettingByKey(SETTINGS,"port");
    static final String HOST = FilesReaderService.getSettingByKey(SETTINGS,"host");

    public Client() throws IOException {
        clientSocket = new Socket(HOST, Integer.parseInt(PORT));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        clientRead = new ClientRead(in);
        clientWrite = new ClientWrite(out, reader);
    }

    public void start() throws IOException {
        clientRead.start();
        clientWrite.start();

        while (true) {
            if (clientSocket.isClosed()) {
                close();

                break;
            }
        }
    }

    public void close() throws IOException {
        clientSocket.close();

        System.out.println("Клиент закрыт!");
    }
}