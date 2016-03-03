package com.example.tereza.rxtests;

import java.util.Random;

/**
 * Created by tereza on 2/27/16.
 */
public class Generator {

    private Random rnd;

    public Generator() {
        this.rnd = new Random();
    }

    public int getInt() {
        return this.rnd.nextInt();
    }

    public int getIntFromInterval(int a, int b) {
        return a + this.rnd.nextInt(b);
    }

}
