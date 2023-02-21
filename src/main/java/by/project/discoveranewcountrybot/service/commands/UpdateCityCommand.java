package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.City;
import by.project.discoveranewcountrybot.model.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UpdateCityCommand {

    @Autowired
    CityRepository cityRepository;

    public City findByNameAndCountry(String name, String country) {
        return cityRepository.findByNameAndCountry(name, country);
    }

    public void UpdateCityCommand(String name, String country, String newNameOfCountry, String newCountry,
                                  String newPopulation, String newDateOfFoundation) {
        City city = new City();
        city.setId(findByNameAndCountry(name,country).getId());
        city.setName(newNameOfCountry);
        city.setPopulation(Double.parseDouble(newPopulation));
        String [] number = newDateOfFoundation.split("\\.+");
        List<String> dateForBd = new ArrayList<>();
        Collections.addAll(dateForBd, number);
        city.setFoundationYear(new java.sql.Date(Integer.parseInt(dateForBd.get(0)),Integer.parseInt(dateForBd.get(1)),
                Integer.parseInt(dateForBd.get(2))));
        city.setCountry(newCountry);
        city.setChartId(findByNameAndCountry(name,country).getChartId());
        cityRepository.delete(findByNameAndCountry(name, country));
        cityRepository.save(city);
    }
}
