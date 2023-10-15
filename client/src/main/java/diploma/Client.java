package diploma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SETTINGS = "settings.txt";
    static final String PORT = FilesReaderService.getSettingByKey(SETTINGS,"port");
    static final String HOST = FilesReaderService.getSettingByKey(SETTINGS,"host");

    public static void main(String[] args) {
        try (
            Socket clientSocket = new Socket(HOST, Integer.parseInt(PORT));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            while (true) {
                String resp = in.readLine();

                if (resp == null) {
                    break;
                }

                if (resp.equals("closed")) {
                    break;
                }

                System.out.println(resp);

                if (!resp.startsWith(">>")) {
                    String answer = reader.readLine();

                    out.println(answer);
                }
            }
        } catch (IOException ignored) {
        } finally {
            System.out.println("Клиент закрыт!");
        }
    }
}