package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddCityCommand {
    @Autowired
    private CityRepository cityRepository;

    public void addCityCommand(City city){
       cityRepository.save(city);
    }
}
