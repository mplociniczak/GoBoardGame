package org.server;

import javax.persistence.*;

@Entity
@Table(name = "moves")
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameThread game;

    private int turnNumber;
    private int xCoordinate;
    private int yCoordinate;
    private StoneColor playerColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameThread getGame() {
        return game;
    }

    public void setGame(GameThread game) {
        this.game = game;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public StoneColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(StoneColor playerColor) {
        this.playerColor = playerColor;
    }
}
