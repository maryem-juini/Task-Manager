import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String message;
    Date time;
    String pseudo;
    public Message(String message, Date time, String pseudo) {
        this.message = message;
        this.time = time;
        this.pseudo = pseudo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
