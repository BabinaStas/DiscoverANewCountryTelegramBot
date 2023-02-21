package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class UpdateCityCommand {

    @Autowired
    CityRepository cityRepository;

    public City findByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }
    public boolean findCity(String name, String country) {
        return Optional.ofNullable(findByNameAndCountry(name,country)).isPresent();
    }

    public void updateCityCommand(String newName, String newCountry, String name, String country,
                                  String newPopulation, String newDateOfFoundation) {
        City newCity = new City();
        newCity.setId(findByNameAndCountry(name,country).getId());
        newCity.setName(newName);
        newCity.setPopulation(Double.parseDouble(newPopulation));
        String [] number = newDateOfFoundation.split("\\.+");
        List<String> dateForBd = new ArrayList<>();
        Collections.addAll(dateForBd, number);
        newCity.setFoundationYear(new java.sql.Date(Integer.parseInt(dateForBd.get(0)),Integer.parseInt(dateForBd.get(1)),
                Integer.parseInt(dateForBd.get(2))));
        newCity.setCountry(newCountry);
        newCity.setChartId(findByNameAndCountry(name,country).getChartId());
        cityRepository.delete(findByNameAndCountry(name,country));
        cityRepository.save(newCity);
    }
}
