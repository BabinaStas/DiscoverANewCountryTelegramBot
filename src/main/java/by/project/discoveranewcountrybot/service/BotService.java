package by.project.discoveranewcountrybot.service;

import by.project.discoveranewcountrybot.config.BotConfig;
import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.service.commands.*;
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
    private final UpdateCityCommand UPDATECITYCOMMAND;
    private final StartBotCommand STARTBOTCOMMAND;


    public BotService(BotConfig config, AboutBotCommand aboutBotCommand, AddCityCommand addCityCommand,
                      AllCitesCommand allCitesCommand,DeleteCityCommand deleteCityCommand, UpdateCityCommand updateCityCommand,
                      StartBotCommand startBotCommand) {
        this.CONFIG = config;
        this.ABOUTBOTCOMMAND = aboutBotCommand;
        this.ADDCITYCOMMAND = addCityCommand;
        this.ALLCITESCOMMAND = allCitesCommand;
        this.DELETECITYCOMMAND = deleteCityCommand;
        this.UPDATECITYCOMMAND = updateCityCommand;
        this.STARTBOTCOMMAND = startBotCommand;
        //В конструкторе создаеться лист, который в дальнейшем передаеться для создания меню бота.
       List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Command get a welcome message"));
        listOfCommands.add(new BotCommand("/all_cites", "Command get a list of cites"));
        listOfCommands.add(new BotCommand("/add_city", "Command add city for database"));
        listOfCommands.add(new BotCommand("/correction_information_of_city", "Command for correcting " +
                "reference information about city."));
        listOfCommands.add(new BotCommand("/delete_city", "Command delete city for database"));
        listOfCommands.add(new BotCommand("/about_bot", "Command informing about the use of the bot"));
        listOfCommands.add(new BotCommand("/bye", "Command get a goodbye message"));
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
                        correctionInformationOfCityCommandReceived(chatId);
                        break;
                    case "/delete_city":
                        deleteCityCommandReceived(chatId);
                        break;
                    case "/about_bot":
                        aboutBotCommandReceived(chatId);
                        break;
                    case "/bye":
                        byeCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    default:
                        sendMessage(chatId, "Sorry, command was not recognized");
                    }
                } else if (messageText.matches("^([a-zA-Z]*)(\\s)([a-zA-Z]*)(\\s)([0-9]*[.,][0-9]*)(\\s)([0-9]*[.,][0-9]*[.,][0-9]*)$")) {
                    parsMessageForAdd(chatId, messageText);
                } else if (messageText.matches("^([a-zA-Z]*)(\\s)([a-zA-Z]*)$")){
                    parseMessageForDelete(messageText, chatId);
                }else if (messageText.matches("^([a-zA-Z]*)(\\s)([a-zA-Z]*)(\\s)(\\\\)(\\s)([a-zA-Z]*)(\\s)([a-zA-Z]*)(\\s)([0-9]*[.,][0-9]*)(\\s)([0-9]*[.,][0-9]*[.,][0-9]*)$")){
                parseMessageForUpdate(messageText, chatId);
            } else if (messageText.matches("^([a-zA-Z]*)$")){
                parseMessageForSuch(messageText, chatId);
                }else{
                    sendMessage(chatId, "Sorry, data entered incorrectly");
            }
        }
    }
    private void startCommandReceived(Long chatId, String nameUser){
        sendMessage(chatId, "Hi, " + nameUser + ", nice to meet you! \n"
                + STARTBOTCOMMAND.getMESSAGEFORUSER());
        log.info("Replied to user: " + nameUser);
    }
    private void byeCommandReceived(long chatId, String firstName) {
        sendMessage(chatId, "Bye, " + firstName + ", see you late!");
    }
    private void parseMessageForSuch(String messageText, long chartId) {
        sendMessage(chartId, ALLCITESCOMMAND.findByName(messageText).toString() +
                "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
        log.info("User such city in DB: " + messageText);
    }
    private void allCitesCommandReceived(Long chatId){
        sendMessage(chatId, "All cites in a DB: " + ALLCITESCOMMAND.allCitesCommand().toString()
                .replace("[", " ").replace("]", " ") +
                "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
        log.info("The user requested information about the cities in the database.");
    }
    private void addCityCommandReceived(Long chatId) {
        sendMessage(chatId, ADDCITYCOMMAND.getMESSAGEFORUSER());
    }
    private void correctionInformationOfCityCommandReceived(Long chatId) {
        sendMessage(chatId, UPDATECITYCOMMAND.getMESSAGEFORUSER());
    }
    private void deleteCityCommandReceived(Long chatId){
        sendMessage(chatId, DELETECITYCOMMAND.getMESSAGEFORUSER());
    }
    private void aboutBotCommandReceived(long chatId){
        sendMessage(chatId, ABOUTBOTCOMMAND.getINFORMATIZATION());
        log.info("The user requested information about the bot.");
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
            cityForDB.setFoundationYear(new java.sql.Date(Integer.parseInt(dateForBd.get(0)) - 1900,
                    Integer.parseInt(dateForBd.get(1)), Integer.parseInt(dateForBd.get(2))));
        if(ADDCITYCOMMAND.findCity(cityForDB.getName(),cityForDB.getCountry())){
            sendMessage(chatId, "The city is already in the database." +
                    "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
        }else{
            ADDCITYCOMMAND.addCityCommand(cityForDB);
            sendMessage(chatId, "City add to DB." +
                    "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
            log.info("City add to DB: " + cityForDB);
        }
    }
    private void parseMessageForDelete(String messageText, long chatId) {
        String[] words = messageText.split("\\s+");
        List<String> nameOfCity = new ArrayList<>();
        Collections.addAll(nameOfCity, words);
        if (!DELETECITYCOMMAND.findCity(nameOfCity.get(0), nameOfCity.get(1))) {
            sendMessage(chatId, "The city doesn't already in the database." +
                    "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
        } else {
            DELETECITYCOMMAND.deleteCityCommand(nameOfCity.get(0), nameOfCity.get(1));
            sendMessage(chatId, "City delete of DB." +
                    "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
            log.info("City delete of DB: " + nameOfCity.get(0));
        }
    }
    private void parseMessageForUpdate(String messageText, long chatId) {
        String[] words = messageText.split("\\s+");
        List<String> nameOfCity = new ArrayList<>();
        Collections.addAll(nameOfCity, words);
        if (!UPDATECITYCOMMAND.findCity(nameOfCity.get(0), nameOfCity.get(1))) {
            sendMessage(chatId, "The city doesn't already in the database." +
                    "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
        } else {
            UPDATECITYCOMMAND.updateCityCommand(nameOfCity.get(0), nameOfCity.get(1), nameOfCity.get(3),
                    nameOfCity.get(4), nameOfCity.get(5), nameOfCity.get(6));
            sendMessage(chatId, "City update of DB." +
                    "\n" + STARTBOTCOMMAND.getMESSAGEFORUSERELSE());
            log.info("City update of DB: " + nameOfCity.get(3));
        }
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
