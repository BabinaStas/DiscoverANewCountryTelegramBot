package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllCitesCommand {
    @Autowired
    private CityRepository cityRepository;

    public String allCitesCommand() {
        return cityRepository.findAll().toString();
    }
}
