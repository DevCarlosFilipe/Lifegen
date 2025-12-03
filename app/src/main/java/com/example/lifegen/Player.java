package com.example.lifegen;

public class Player {
    private final long id;
    private String name;
    private int maxHp;
    private int currentHp;

    public Player(long id, String name, int maxHp) {
        this.id = id;
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp; // Vida atual começa igual à máxima
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }
}
