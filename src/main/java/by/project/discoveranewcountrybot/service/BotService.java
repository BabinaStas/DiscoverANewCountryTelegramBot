package by.project.discoveranewcountrybot.service;

import by.project.discoveranewcountrybot.config.BotConfig;
import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.service.commands.AboutBotCommand;
import by.project.discoveranewcountrybot.service.commands.AddCityCommand;
import by.project.discoveranewcountrybot.service.commands.AllCitesCommand;
import by.project.discoveranewcountrybot.service.commands.DeleteCityCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.*;


//Аннотация позволяет подключать логирование.
@Slf4j
//Аннотация позваляет автоматически Spring создать экземпляр класса.
@Component
// Для того что бы Telegram общался с BackEnd необходимо насловаться от данной библиотеки.
public class BotService extends TelegramLongPollingBot {

    final BotConfig CONFIG;
    private final AboutBotCommand ABOUTBOTCOMMAND;
    private final AddCityCommand ADDCITYCOMMAND;
    private final AllCitesCommand ALLCITESCOMMAND;

    private final DeleteCityCommand DELETECITYCOMMAND;

    public BotService(BotConfig config, AboutBotCommand aboutBotCommand, AddCityCommand addCityCommand,
                      AllCitesCommand allCitesCommand,DeleteCityCommand deleteCityCommand) {
        this.CONFIG = config;
        this.ABOUTBOTCOMMAND = aboutBotCommand;
        this.ADDCITYCOMMAND = addCityCommand;
        this.ALLCITESCOMMAND = allCitesCommand;
        this.DELETECITYCOMMAND = deleteCityCommand;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.matches("^/[a-zA-Z_]*$")) {
                switch (messageText) {
                    case "/start":
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/all_cites":
                        allCitesCommandReceived(chatId);
                        break;
                    case "/add_city":
                        addCityCommandReceived(chatId);
                        break;
                    case "/correction_information_of_city":
                        correctionInformationOfCityCommandReceived(chatId, update);
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
                } else if (messageText.matches("^([a-zA-Z]*)(\\s)([a-zA-Z]*)(\\s)([0-9]*[.,][0-9]*)(\\s)([0-9]*[.,][0-9]*[.,][0-9]*)$")) {
                    parsMessageForAdd(chatId, messageText);
                } else if (messageText.matches("^([a-zA-Z]*)(\\s)([a-zA-Z]*)$")){
                    parseMessageForDelete(messageText);
                }else{
                    sendMessage(chatId, "Sorry, data entered incorrectly");
            }
        }
    }
    private void startCommandReceived(Long chatId, String nameUser){
        log.info("Replied to user: " + nameUser);
        sendMessage(chatId, "Hi, " + nameUser + ", nice to meet you!");
    }
    private void allCitesCommandReceived(Long chatId){
        sendMessage(chatId, "All cites in a db: " + ALLCITESCOMMAND.allCitesCommand());
    }
    private void addCityCommandReceived(Long chatId) {
        sendMessage(chatId, "Please add new city:");
    }

    private void correctionInformationOfCityCommandReceived(Long chatId, Update update) {
        sendMessage(chatId, "What would you like to change in the city? ");
        }
    private void deleteCityCommandReceived(Long chatId){
        sendMessage(chatId, "What city would you like to delete information about?");

    }
    private void aboutBotCommandReceived(long chatId){
        sendMessage(chatId, ABOUTBOTCOMMAND.getINFORMATIZATION());
    }
    private void parsMessageForAdd(long chatId, String messageText) {
        String [] words = messageText.split("\\s+");
        List<String> cityNew = new ArrayList<>();
        Collections.addAll(cityNew, words);
            City cityForDB = new City();
            cityForDB.setChartId((int) chatId);
            cityForDB.setId((int) (1000000 *  Math.random()));
            cityForDB.setName(cityNew.get(0));
            cityForDB.setCountry(cityNew.get(1));
            cityForDB.setPopulation(Double.valueOf(cityNew.get(2)));
            String [] number = cityNew.get(3).split("\\.+");
            List<String> dateForBd = new ArrayList<>();
            Collections.addAll(dateForBd, number);
            cityForDB.setFoundationYear(new java.sql.Date(Integer.parseInt(dateForBd.get(0)),Integer.parseInt(dateForBd.get(1)),
                    Integer.parseInt(dateForBd.get(2))));
            ADDCITYCOMMAND.addCityCommand(cityForDB);
            log.info("City add to DB: " + cityForDB);
        }
    private void parseMessageForDelete(String messageText) {
        String [] words = messageText.split("\\s+");
        List<String> nameOfCity = new ArrayList<>();
        Collections.addAll(nameOfCity, words);
        DELETECITYCOMMAND.deleteCityCommand(nameOfCity.get(0),nameOfCity.get(1));
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
