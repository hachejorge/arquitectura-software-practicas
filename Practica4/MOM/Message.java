package MOM;

import java.util.Date;
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

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
