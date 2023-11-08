package diploma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientWrite extends Thread {
    private final PrintWriter out;
    private final BufferedReader reader;

    public ClientWrite(PrintWriter out, BufferedReader reader) {
        this.out = out;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    String answer = reader.readLine();

                    out.println(answer);
                }
            } catch (IOException ignored) {
            } finally {
                out.close();
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
