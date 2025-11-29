package com.example.lifegen;

public class Player {
    private final long id;
    private final String name;
    private final int maxHp;

    public Player(long id, String name, int maxHp) {
        this.id = id;
        this.name = name;
        this.maxHp = maxHp;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }
}
