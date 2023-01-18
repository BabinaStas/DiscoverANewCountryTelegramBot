package by.project.discoveranewcountrybot.service;

import by.project.discoveranewcountrybot.config.BotConfig;
import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.service.commands.AboutBotCommand;
import by.project.discoveranewcountrybot.service.commands.AddCityCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Аннотация позволяет подключать логирование.
@Slf4j
//Аннотация позваляет автоматически Spring создать экземпляр класса.
@Component
// Для того что бы Telegram общался с BackEnd необходимо насловаться от данной библиотеки.
public class BotService extends TelegramLongPollingBot {

    final BotConfig CONFIG;
    final AboutBotCommand ABOUTBOTCOMMAND;

    final AddCityCommand addCityCommand;

    public BotService(BotConfig config, AboutBotCommand aboutBotCommand, AddCityCommand addCityCommand) {
        this.CONFIG = config;
        this.ABOUTBOTCOMMAND = aboutBotCommand;
        this.addCityCommand = addCityCommand;
        //В конструкторе создаеться лист, который в дальнейшем передаеться для создания меню бота.
       List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Command get a welcome message"));
        listOfCommands.add(new BotCommand("/all_cites", "Command get a list of cites"));
        listOfCommands.add(new BotCommand("/add_city", "Command add city for database"));
        listOfCommands.add(new BotCommand("/correction_information_of_city", "Command for correcting " +
                "reference information about city."));
        listOfCommands.add(new BotCommand("/delete_city", "Command delete city for database"));
        listOfCommands.add(new BotCommand("/about_bot", "Command informing about the use of the bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return CONFIG.getBotName();
    }

    @Override
    public String getBotToken() {
        return CONFIG.getToken();
    }

    //Метод описывающий все изменения в боте. От отправки сообщений до изменения настроек.
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
                case "/all_cites":
                    allCitesCommandReceived(chatId);
                    break;
                case "/add_city":
                    addCityCommandReceived(chatId, update);
                    break;
                case "/correction_information_of_city":
                    correctionInformationOfCityCommandReceived(chatId);
                    break;
                case "/delete_city":
                    deleteCityCommandReceived(chatId);
                    break;
                case "/about_bot":
                    aboutBotCommandReceived(chatId);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }
    }
    private void startCommandReceived(Long chatId, String nameUser){
        String answer = "Hi, " + nameUser + ", nice to meet you!";
        log.info("Replied to user: " + nameUser);
        sendMessage(chatId, answer);
    }
    private void allCitesCommandReceived(Long chatId){
        String answer = "All cites:";
        sendMessage(chatId, answer);
    }
    private void addCityCommandReceived(Long chatId, Update update) {
        String answer = "Please add new city:";
        sendMessage(chatId, answer);
            City city = new City();
            city.setId((long) (Math.random() * 100));
        String name = "Please add name city:";
        sendMessage(chatId, name);
            city.setName(update.getMessage().getText());
        String country = "Please add country city:";
        sendMessage(chatId, country);
            city.setCountry(update.getMessage().getText());
        String population = "Please add population of city:";
        sendMessage(chatId, population);
            city.setPopulation(Double.parseDouble(update.getMessage().getText()));
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy");
            try {
                Date docDate = format.parse(update.getMessage().getText());
                String foundationYear = "Please add year of foundation city:";
                sendMessage(chatId, foundationYear);
                city.setFoundationYear(docDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            addCityCommand.addCityCommand(city);
        }


    private void correctionInformationOfCityCommandReceived(Long chatId){
        String answer = "What would you like to change in the city? ";
        sendMessage(chatId, answer);
    }
    private void deleteCityCommandReceived(Long chatId){
        String answer = "What city would you like to delete information about?";
        sendMessage(chatId, answer);
    }
    private void aboutBotCommandReceived(Long chatId){
        String answer = ABOUTBOTCOMMAND.getINFORMATIZATION();
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String sendMessageForUser) {
        SendMessage messageForUser = new SendMessage();
        messageForUser.setChatId(String.valueOf(chatId));
        messageForUser.setText(sendMessageForUser);
        try {
            execute(messageForUser);
        }catch (TelegramApiException e){
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
