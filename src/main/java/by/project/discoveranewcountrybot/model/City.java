package by.project.discoveranewcountrybot.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

//Аннотация связывает таблицу с даннам классом.
@Entity(name = "cites_data_table")
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private Double population;
    private Timestamp foundationYear;
}
