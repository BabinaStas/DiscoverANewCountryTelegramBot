package by.project.discoveranewcountrybot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CityRepository extends CrudRepository<City,Long> {

    @Query
    City findByNameAndCountry(String name);
}
