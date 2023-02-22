package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Data
public class DeleteCityCommand {

    @Autowired
    CityRepository cityRepository;

    private final String MESSAGEFORUSER =  "What city would you like to delete information about? \n" +
            "Please write the name and country of the city or town that you would like to delete";

    public City findByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }
    public boolean findCity(String name, String country) {
        return Optional.ofNullable(findByNameAndCountry(name,country)).isPresent();
    }

    public void deleteCityCommand(String name, String country){
        cityRepository.delete(findByNameAndCountry(name, country));
        }
    }

