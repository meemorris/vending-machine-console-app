package com.techelevator.vending;

import java.math.BigDecimal;

public abstract class Item implements Consumable {

    private String slotNumber;
    private String name;
    private BigDecimal price;
    private int inventory;
    private int purchases;
    private static final int STARTING_INVENTORY = 5;

    public Item() {
        inventory = STARTING_INVENTORY;
        purchases = 0;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getPurchases() {
        return purchases;
    }

    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }

    @Override
    public abstract String printSound();

    public String purchase() {
        String message = "";
        inventory--;
        purchases++;
        message = printSound();
        return message;
    }

}
