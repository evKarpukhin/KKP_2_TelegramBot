package pro.sky.telegrambot.repositiry;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.TableTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TastRepositiry extends JpaRepository<TableTask, Long> {

    List<TableTask> findTableTaskByTimeSend(LocalDateTime localDateTime);

}
