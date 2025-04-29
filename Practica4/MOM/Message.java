package MOM;

import java.util.Date;

public class Message {
    private String content;
    private Date timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = new Date();
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
