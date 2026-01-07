package com.schwink.schwinkmod.server.shufflebag;

public class ShuffleBagEntry{
    private int amount;
    private String type;
    private String name;

    public ShuffleBagEntry(int amount, String type, String name) {
        this.amount = amount;
        this.type = type;
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getName() { return name; }
}
