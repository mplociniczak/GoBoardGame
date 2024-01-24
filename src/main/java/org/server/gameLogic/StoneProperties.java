package org.server.gameLogic;

public class StoneProperties {
    private StoneColor color;
    private IntersectionState intersectionState;
    public StoneProperties() {
        this.color = StoneColor.N;
        this.intersectionState = IntersectionState.EMPTY;
    }
    public StoneColor getColor() {
        return color;
    }
    public void setColor(StoneColor color) { this.color = color; }
    public IntersectionState getState() {
        return intersectionState;
    }
    public void setState(IntersectionState state) { this.intersectionState = state; }
}