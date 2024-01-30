package org.server.database;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game")
public class Game {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    // Statyczna instancja klasy Game
    private static Game instance;

    // Prywatny konstruktor klasy Game
    /*
    private Game() {

    }

     */

    // Publiczna metoda do uzyskania dostępu do instancji klasy Game
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public Long getId() {
        return id;
    }

    // Konstruktory, gettery i settery
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

