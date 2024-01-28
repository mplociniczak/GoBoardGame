package org.server.deepCopy;

import org.server.gameLogic.Stone;

public class DeepCopy {

    public static Stone[][] deepCopy(Stone[][] original) {
        if(original == null) {
            return null;
        }

        Stone[][] copy = new Stone[original.length][];

        for(int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }

        return copy;
    }
}
