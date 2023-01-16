package by.project.discoveranewcountrybot.service.commands;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AboutBotCommand {

    private final String INFORMATIZATION = "The bot provides information about the city " +
            "(population, which country it belongs to, year of foundation, etc.)" +
            " and a list of cities that it knows about. The bot has the ability to add, " +
            "edit and delete information entered into the database using the bot.";
}
