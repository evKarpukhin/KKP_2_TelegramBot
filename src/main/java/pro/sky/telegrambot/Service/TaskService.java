package pro.sky.telegrambot.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.TableTask;
import pro.sky.telegrambot.repositiry.TastRepositiry;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskService {

    private final TastRepositiry tastRepositiry;

    public TaskService(TastRepositiry tastRepositiry) {
        this.tastRepositiry = tastRepositiry;
    }

    @Transactional
    public void addTask(LocalDateTime localDateTime, String msg, Long idChat) {
        TableTask tableTask = new TableTask();
        tableTask.setTimeSend(localDateTime);
        tableTask.setMessage(msg);
        tableTask.setIdChat(idChat);
        tastRepositiry.save(tableTask);
    }

    public List<TableTask> findTaskForSend() {
        return tastRepositiry.findTableTaskByTimeSend(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    @Transactional
    public void killTask(TableTask tableTask) {
        tastRepositiry.delete(tableTask);
    }

}
