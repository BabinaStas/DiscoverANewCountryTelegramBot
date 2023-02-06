package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteCityCommand {

    @Autowired
    CityRepository cityRepository;

    public City findByNameAndCountry(String name) {
        return cityRepository.findByNameAndCountry(name);
    }

    public void deleteCityCommand(String messageOfUser){
        cityRepository.delete(findByNameAndCountry(messageOfUser));
        }
    }

