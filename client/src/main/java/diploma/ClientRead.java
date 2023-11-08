package diploma;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientRead extends Thread {
    private final BufferedReader in;

    public ClientRead(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    String resp = in.readLine();

                    if (resp == null) {
                        break;
                    }

                    if (resp.equals("closed")) {
                        break;
                    }

                    System.out.println(resp);
                }
            } catch (IOException ignored) {
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
