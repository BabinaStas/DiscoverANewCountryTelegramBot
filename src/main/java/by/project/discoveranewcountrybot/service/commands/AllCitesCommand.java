package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AllCitesCommand {
    @Autowired
    private CityRepository cityRepository;

    public List<String> allCitesCommand() {
        List<String> allCites = new ArrayList<>();
        allCites.addAll(Collections.singleton(cityRepository.findAll().toString()));
        return allCites;
    }
}
