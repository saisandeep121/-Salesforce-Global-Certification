package com.bulb;

public class Bulb {
    private final int id;
    private BulbState state;

    public Bulb(int id) {
        this.id = id;
        this.state = BulbState.OFF;
    }

    public int getId() {
        return id;
    }

    public BulbState getState() {
        return state;
    }

    public void toggle() {
        this.state = (this.state == BulbState.OFF) ? BulbState.ON : BulbState.OFF;
    }

    @Override
    public String toString() {
        return "Bulb " + id + " : " + state;
    }
}
