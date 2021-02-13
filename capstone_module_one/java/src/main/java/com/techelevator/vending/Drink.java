package com.techelevator.vending;

public class Drink extends Item implements Consumable {

    @Override
    public String printSound() {
        return "Glug Glug, Yum!";
    }

}
