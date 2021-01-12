package bd.org.coronacare.models;

public class Chat {
    private String id;
    private String message;
    private String type;
    private String sender;
    private String receiver;
    private long time;

    public Chat() {

    }

    public Chat(String id, String message, String type, String sender, String receiver, long time) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
