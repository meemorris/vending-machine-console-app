package com.techelevator.vending;

public class Gum extends Item implements Consumable {

    @Override
    public String printSound() {
        return "Chew Chew, Yum!";
    }

}
