package com.bulb;

import java.util.ArrayList;
import java.util.List;

public class ToggleEngine {
    private final List<Bulb> bulbs;

    public ToggleEngine() {
        this.bulbs = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Bulb bulb = new Bulb(i);
            if (i % 2 != 0) { // Bulbs 1, 3, 5, 7
                bulb.toggle(); // Set to ON
            }
            this.bulbs.add(bulb);
        }
    }

    public List<Bulb> getBulbs() {
        return bulbs;
    }

    public void applyOperation(OperationType type) {
        if (type == null) return;
        
        if (type == OperationType.A) {
            // Enforce Odd Series: 1, 3, 5, 7 ON and 2, 4, 6 OFF
            for (Bulb b : bulbs) {
                if (b.getId() % 2 != 0) {
                    if (b.getState() == BulbState.OFF) b.toggle();
                } else {
                    if (b.getState() == BulbState.ON) b.toggle();
                }
            }
        } else if (type == OperationType.B) {
            // Enforce Even Series: 2, 4, 6 ON and 1, 3, 5, 7 OFF
            for (Bulb b : bulbs) {
                if (b.getId() % 2 == 0) {
                    if (b.getState() == BulbState.OFF) b.toggle();
                } else {
                    if (b.getState() == BulbState.ON) b.toggle();
                }
            }
        }
    }

    public void turnAllOff() {
        for (Bulb b : bulbs) {
            if (b.getState() == BulbState.ON) {
                b.toggle();
            }
        }
    }
}
