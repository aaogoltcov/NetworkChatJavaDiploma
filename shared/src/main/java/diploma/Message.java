package diploma;

import java.util.Date;

public class Message {
    public MessageType type;
    public String dateTimeStamp;
    public String nickname;
    public String message;

    public Message() {
    }

    public Message(MessageType type, String nickname, String message, String dateTimeStamp) {
        this.type = type;
        this.dateTimeStamp = dateTimeStamp;
        this.nickname = nickname;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
            "type=" + type +
            ", dateTimeStamp='" + dateTimeStamp + '\'' +
            ", nickname='" + nickname + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
