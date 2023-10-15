package diploma;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class FilesReaderServiceTest {
    public FilesReaderService filesReaderService = mock(FilesReaderService.class);
    public final String expectedPortKey = "port";
    public final String expectedPortValue = "9000";
    public final String expectedLogFileKey = "log";
    public final String expectedLogFileValue = "file_test.log";
    public final String settingsFileName = "settings_test.txt";
    public final File settingsFile = new File(settingsFileName);
    public final File logFile = new File(expectedLogFileValue);



    @BeforeEach
    public void initTest() {
        final StringBuilder settings = new StringBuilder();
        final String settingsColon = ":";
        final String settingsCarriage = "\n";

        settings.append(expectedPortKey);
        settings.append(settingsColon);
        settings.append(expectedPortValue);
        settings.append(settingsCarriage);
        settings.append(expectedLogFileKey);
        settings.append(settingsColon);
        settings.append(expectedLogFileValue);
        settings.append(settingsCarriage);

        try {
            if (settingsFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(settingsFileName)) {
                    byte[] bytes = settings.toString().getBytes();
                    fos.write(bytes, 0, bytes.length);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            logFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void finalizeTest() {
        if (settingsFile.isFile()) {
            settingsFile.delete();
        }

        if (logFile.isFile()) {
            logFile.delete();
        }
    }

    @Test
    public void testGetSettingByKey() {
        // when
        final String actualSettingValue = FilesReaderService.getSettingByKey(settingsFileName, expectedPortKey);

        // assert
        Assertions.assertEquals(expectedPortValue, actualSettingValue);
    }

    @Test
    public void testSaveMessageToScvFile() {
        // given
        List<Message> actualMessages = null;
        final Message expectedMessage = new Message(
            MessageType.SERVER,
            "test",
            "message",
            new Date().toString()
        );
        final List<Message> expectedMessages = new ArrayList<>();
        expectedMessages.add(expectedMessage);

        // when
        FilesReaderService.saveMessageToScvFile(settingsFileName, expectedLogFileKey, expectedMessage);

        try (CSVReader csvReader = new CSVReader(new FileReader(expectedLogFileValue))) {
            ColumnPositionMappingStrategy<Message> strategy = new ColumnPositionMappingStrategy<>();
            final String[] columnMapping = {"type", "dateTimeStamp", "nickname", "message"};

            strategy.setType(Message.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Message> csv = new CsvToBeanBuilder<Message>(csvReader).withMappingStrategy(strategy).build();

            actualMessages = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // assert
        Assertions.assertEquals(expectedMessages.toString(), actualMessages.toString());
    }
}
