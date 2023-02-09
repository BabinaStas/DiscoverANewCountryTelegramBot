package by.project.discoveranewcountrybot.model;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.sql.Date;


//Аннотация связывает таблицу с даннам классом.
@Entity(name = "cites_data_table")
@Data
@Table
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String country;
    private Double population;
    private Date foundationYear;

    private Integer chartId;

    @Override
    public String toString() {
        return "\nName of a city: " + name + "\n" +
                "Country: " + country + "\n" +
                "Population: " + population + "\n" +
                "Foundation: "  + foundationYear + "\n";

    }
}
