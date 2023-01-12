package by.project.discoveranewcountrybot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//Аннотация указывает для Spring что файл является конфигурационным.
@Configuration
//Аннотация создает за нас контрукторы getter и setter.
@Data
//Аннотация указывает путь к файлу свойств.
@PropertySource("application.properties")

public class BotConfig {

    //Аннотация оьбращаеться к конкретному свойству поля. В данном случае bot.name.
    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

}
