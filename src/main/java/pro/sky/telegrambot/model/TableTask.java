package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class TableTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_chat")
    private Long idChat;

    private String message;

    @Column(name = "time_send")
    private LocalDateTime timeSend;

    public TableTask(Long id, Long idChat, String message, LocalDateTime timeSend) {
        this.id = id;
        this.idChat = idChat;
        this.message = message;
        this.timeSend = timeSend;
    }

    public TableTask() {
    }

    @Override
    public String toString() {
        return "TableTask{" +
                "id=" + id +
                ", idChat=" + idChat +
                ", message='" + message + '\'' +
                ", timeSend=" + timeSend +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableTask tableTask = (TableTask) o;
        return id.equals(tableTask.id) && idChat.equals(tableTask.idChat) && Objects.equals(message, tableTask.message) && Objects.equals(timeSend, tableTask.timeSend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idChat, message, timeSend);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(LocalDateTime timeSend) {
        this.timeSend = timeSend;
    }
}
