package diploma;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

public class FilesReaderService {

    private FilesReaderService() {
    }

    public static String getSettingByKey(String settingsFile, String key) {
        try (FileInputStream settings = new FileInputStream(settingsFile)) {
            Properties properties = new Properties();

            properties.load(settings);

            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveMessageToScvFile(String settingsFile, String key, Message message) {
        final String logFile = getSettingByKey(settingsFile, key);
        final ColumnPositionMappingStrategy<Message> csvStrategy = new ColumnPositionMappingStrategy<>();
        final String[] columnMapping = {"type", "dateTimeStamp", "nickname", "message"};

        csvStrategy.setType(Message.class);
        csvStrategy.setColumnMapping(columnMapping);

        try (Writer writer = new FileWriter(logFile, true)) {
            StatefulBeanToCsv<Message> sbc =
                new StatefulBeanToCsvBuilder<Message>(writer)
                    .withMappingStrategy(csvStrategy)
                    .build();
            sbc.write(message);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }
}
