package org.server.database;

import org.server.gameLogic.StoneColor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "move")
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public StoneColor getStoneColor() {
        return stoneColor;
    }

    public void setStoneColor(StoneColor stoneColor) {
        this.stoneColor = stoneColor;
    }

    public Date getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(Date moveTime) {
        this.moveTime = moveTime;
    }

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "position_x")
    private int positionX;

    @Column(name = "position_y")
    private int positionY;

    @Column(name = "stone_color")
    @Enumerated(EnumType.STRING)
    private StoneColor stoneColor;

    @Column(name = "move_time")
    private Date moveTime;

    // Konstruktory, gettery i settery
}
