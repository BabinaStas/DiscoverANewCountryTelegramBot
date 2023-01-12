package by.project.discoveranewcountrybot.config;

import by.project.discoveranewcountrybot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    @Autowired
    BotService botService;

    @EventListener({ContextRefreshedEvent.class})
    public void botInit() throws TelegramApiException {
        //Создаем новую сессию подклюбчения к Telegram.
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(botService);
        } catch (TelegramApiException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
