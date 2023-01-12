package by.project.discoveranewcountrybot.service;

import by.project.discoveranewcountrybot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static by.project.discoveranewcountrybot.service.MethodsForSwitchUpdateReceived.sendMessage;
import static by.project.discoveranewcountrybot.service.MethodsForSwitchUpdateReceived.startCommandReceived;

//Аннотация позваляет автоматически Spring создать экземпляр класса.
@Component
// Для того что бы Telegram общался с BackEnd необходимо насловаться от данной библиотеки.
public class BotService extends TelegramLongPollingBot {

    final BotConfig config;

    public BotService(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Данный метод описывает действия которые будут выполняться если бот получит какое-то сообщение.
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }
    }
}
