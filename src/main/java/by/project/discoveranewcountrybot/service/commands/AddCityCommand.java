package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@Data
public class AddCityCommand {
    @Autowired
    private CityRepository cityRepository;

    private final String MESSAGEFORUSER =  "Please add new city: \n " +
            "Below is an example of filling out a form to add a city. \n" +
            "\"Minsk Belarus 1.99 1067.01.01\"";

    public City findByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }

    public boolean findCity(String name, String country) {
        return Optional.ofNullable(findByNameAndCountry(name,country)).isPresent();
    }

    public void addCityCommand(City city) {
            cityRepository.save(city);
    }
}
