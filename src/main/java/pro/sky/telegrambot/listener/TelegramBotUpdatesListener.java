package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Service.TaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TaskService taskService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TaskService taskService) {
        this.telegramBot = telegramBot;
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            LOG.info("Processing update: {}", update);
            // Process your updates here
            // Send messages
            // обрабатываем ситуацию, когда строка соответствует паттерну
            if (update.message() != null) {
                long chatId = update.message().chat().id();
                Message message = update.message();
                // SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello!"));
                switch (message.text()) {
                    case "/start":
                        SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello gays!"));
                        break;

                    default: {
                        Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");
                        Matcher matcher = pattern.matcher(message.text());

                        if (matcher.matches()) {
                            LocalDateTime localDateTime = parserLocalDateTime(matcher.group(1));
                            if (!Objects.isNull(localDateTime)) {
                                String msg = matcher.group(3);
                                taskService.addTask(localDateTime, msg, chatId);
                                new SendMessage(chatId, "Задача запланирована!");
                            } else {
                                SendResponse response1 = telegramBot.execute(new SendMessage(chatId, "Сообщение в неверном формате!"));
                            }
                        } else {
                            SendResponse response1 = telegramBot.execute(new SendMessage(chatId, "Не найдено сообщение!"));
                        }
                    }
                }
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Nullable
    private LocalDateTime parserLocalDateTime(String datetime) {
        try {
            return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @Scheduled(fixedDelay = 60 * 1_000)
    public void runShedulledMsg() {
        taskService.findTaskForSend().forEach(tableTask -> {
                    telegramBot.execute(
                            new SendMessage(tableTask.getIdChat(), "Напоминание: " + tableTask.getMessage())
                    );
                    taskService.killTask(tableTask);
                });
    }


}
