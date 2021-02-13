package com.techelevator.vending;

public class Chip extends Item implements Consumable {

    @Override
    public String printSound() {
        return "Crunch Crunch, Yum!";
    }

}
