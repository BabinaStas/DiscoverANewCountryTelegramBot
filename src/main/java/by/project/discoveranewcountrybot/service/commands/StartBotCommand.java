package by.project.discoveranewcountrybot.service.commands;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class StartBotCommand {

    private final String MESSAGEFORUSER =  "Maybe you would like to find information about the city? \n" +
            "Please write the name of the city or town that you would like to find information about? ";

    private final String MESSAGEFORUSERELSE =  "Something else?";
}
