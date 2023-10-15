package diploma;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Date;

public class LoggerTest {
    private Logger logger;

    @BeforeEach
    public void initTest() {
        this.logger = Logger.getInstance();
    }

    @AfterEach
    public void finalizeTest() {
        this.logger = null;
    }

    @Test
    public void testLog() {
        // given
        final Message message = new Message(
            MessageType.SERVER,
            "test",
            "message",
            new Date().toString()
        );

        try (MockedStatic<FilesReaderService> filesReaderService = Mockito.mockStatic(FilesReaderService.class)) {
            // when
            filesReaderService.when(() -> FilesReaderService.getSettingByKey(Mockito.anyString(), Mockito.anyString())).thenReturn("file_test.log");
            this.logger.log(message);

            //assert
            filesReaderService.verify(() ->  FilesReaderService.saveMessageToScvFile(Mockito.anyString(), Mockito.anyString(), Mockito.any()));
        }
    }
}
