package by.project.discoveranewcountrybot.service.commands;

import by.project.discoveranewcountrybot.model.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class AllCitesCommand {
    @Autowired
    private CityRepository cityRepository;

    public List<String> allCitesCommand() {
        return new ArrayList<>(Collections.singleton(cityRepository.findAll().toString()
                .replace("[", " ").replace("]", " ")));
    }
}
