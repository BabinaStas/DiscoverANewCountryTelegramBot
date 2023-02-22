package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Data
public class UpdateCityCommand {

    @Autowired
    CityRepository cityRepository;

    private final String MESSAGEFORUSER =  "What would you like to change in the city? \n" +
            "Below is an example of filling out a form to add a city. \n" +
            "\"London UK \\ London GB 8.70 0047.01.01\" or \n\"London UK \\ Birmengem UK 3.20 0518.01.01\"";

    public City findByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }
    public boolean findCity(String name, String country) {
        return Optional.ofNullable(findByNameAndCountry(name,country)).isPresent();
    }

    public void updateCityCommand(String name, String country, String newName, String newCountry,
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
