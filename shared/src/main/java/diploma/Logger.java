package diploma;

public class Logger implements Log {
    private static volatile Logger instance;
    private String SETTINGS = "settings.txt";
    private String LOG = "log";

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }

        return instance;
    }

    public void setSETTINGS(String SETTINGS) {
        this.SETTINGS = SETTINGS;
    }

    public void setLOG(String LOG) {
        this.LOG = LOG;
    }

    @Override
    public void log(Message message) {
        FilesReaderService.saveMessageToScvFile(SETTINGS, LOG, message);
    }
}
