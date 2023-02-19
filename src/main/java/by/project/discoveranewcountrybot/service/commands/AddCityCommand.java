package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddCityCommand {
    @Autowired
    private CityRepository cityRepository;

    public City findByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }

    public void addCityCommand(City city) {
            cityRepository.save(city);
    }
}
