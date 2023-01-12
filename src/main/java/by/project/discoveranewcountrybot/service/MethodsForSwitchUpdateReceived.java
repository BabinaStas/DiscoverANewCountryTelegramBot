package by.project.discoveranewcountrybot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MethodsForSwitchUpdateReceived {

    static void startCommandReceived(Long chatId, String nameUser){
        String answer = "Hi, " + nameUser + ", nice to meet you!";
        sendMessage(chatId, answer);
    }
    static void sendMessage(Long chatId, String sendMessageForUser) {
        SendMessage messageForUser = new SendMessage();
        messageForUser.setChatId(String.valueOf(chatId));
        messageForUser.setText(sendMessageForUser);
    }
}
