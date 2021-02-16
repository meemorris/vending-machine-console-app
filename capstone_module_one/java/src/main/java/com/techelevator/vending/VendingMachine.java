package com.techelevator.vending;

import com.techelevator.util.VmLog;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachine {

    public Map<String, Item> allItems = new LinkedHashMap<>();
    private BigDecimal customerBalance = new BigDecimal("0.00");
    private String inventoryFile;
    private BigDecimal totalSales = new BigDecimal("0.00");
    private static final DecimalFormat f = new DecimalFormat("#0.00");
//    private static final MathContext mc = new MathContext(2);
    private static final BigDecimal QUARTER_VALUE = new BigDecimal("0.25");
    private static final BigDecimal DIME_VALUE = new BigDecimal("0.10");
    private static final BigDecimal NICKEL_VALUE = new BigDecimal("0.05");


    public VendingMachine(String inventoryFile) {
        this.inventoryFile = inventoryFile;
    }

    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    public String displayCustomerBalance() {
        return "$" + getCustomerBalance();
    }

    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }

    public Map<String, Item> getAllItems() {
        return allItems;
    }

    public void setInventoryFile(String inventoryFile) {
        this.inventoryFile = inventoryFile;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public void loadInventory() {
        Path path = Path.of(inventoryFile);
        try (Scanner inventoryScanner = new Scanner(path)) {
            while (inventoryScanner.hasNextLine()) {
                String line = inventoryScanner.nextLine();
                String[] items = line.split("\\|");
                String category = items[3];
                Item newItem = null;
                if (category.equals("Candy")) {
                    newItem = new Candy();
                } else if (category.equals("Chip")) {
                    newItem = new Chip();
                } else if (category.equals("Drink")) {
                    newItem = new Drink();
                } else if (category.equals("Gum")) {
                    newItem = new Gum();
                }
                newItem.setName(items[1]);
                BigDecimal pricePrep = new BigDecimal(items[2]);
                newItem.setPrice(pricePrep);
                newItem.setSlotNumber(items[0]);
                allItems.put(items[0], newItem);
            }
        } catch (IOException e) {
            System.out.println("File does not exist");
        }
    }

    public String displayInventory() {
        StringBuilder display = new StringBuilder();
        for (Map.Entry<String, Item> item : allItems.entrySet()) {
            display.append(item.getKey()).append(" ");
            display.append(item.getValue().getName()).append(" ");
            display.append(item.getValue().getPrice());
            if (item.getValue().getInventory() == 0) {
                display.append(" **SOLD OUT**\n");
            } else {
                display.append("\n");
            }
        }
        return display.toString();
    }

    public void depositMoney(String input) {
        if (input.contains(".") || input.contains("-")) {
            throw new IllegalArgumentException();
        }
        BigDecimal money = new BigDecimal(input);
        customerBalance = customerBalance.add(money);
        VmLog.log(" FEED MONEY: $" + f.format(money) + " $" + f.format(getCustomerBalance()));
    }

    public String purchaseItem(String input) {
        String message = "";
        Item item = allItems.get(input);
        if (!allItems.containsKey(input)) {
            message = "*** Item does not exist, please choose another ***";
        } else if (allItems.containsKey(input) && item.getInventory() <= 0) {
            message = "*** Item out of stock, please choose another ***";
        } else if (allItems.containsKey(input) && item.getPrice().compareTo(getCustomerBalance()) > 0) {
            message = "*** Not enough money, please deposit more ***";
        } else {
            message = item.purchase();
            customerBalance = customerBalance.subtract(item.getPrice());
            totalSales = totalSales.add(item.getPrice());
            VmLog.log(" " + item.getName() + " " + item.getSlotNumber() + " $" + item.getPrice() + " $" + getCustomerBalance());
        }
        return message;
    }

    public String returnChange() {
        String result = "";

        BigDecimal balanceAfterQuarters;
        BigDecimal balanceAfterDimes;

        if (customerBalance.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal quarters = customerBalance.divideToIntegralValue(QUARTER_VALUE);
            balanceAfterQuarters = customerBalance.remainder(QUARTER_VALUE);

            BigDecimal dimes = balanceAfterQuarters.divideToIntegralValue(DIME_VALUE);
            balanceAfterDimes = balanceAfterQuarters.remainder(DIME_VALUE);

            BigDecimal nickels = balanceAfterDimes.divideToIntegralValue(NICKEL_VALUE);


            VmLog.log(" GIVE CHANGE: $" + customerBalance + " $" + f.format(customerBalance = BigDecimal.ZERO));
            result = "Your change is:\n" + quarters + " quarter(s)" + "\n" +
                    dimes + " dime(s)" + "\n" + nickels + " nickel(s)";
        }
        return result;
    }

    public String createAndDisplaySalesReport() {
        StringBuilder sales = new StringBuilder();
        try (PrintWriter writer = new PrintWriter("sale_report.txt")) {
            for (Map.Entry<String, Item> item : allItems.entrySet()) {
                Item value = item.getValue();
                writer.println(value.getName() + " | " + value.getPurchases());
                sales.append(value.getName()).append(" | ").append(value.getPurchases()).append("\n");
            }
            writer.println("\n**TOTAL SALES** $" + totalSales);
            sales.append("\n**TOTAL SALES** $").append(totalSales);

            System.out.println();
            System.out.println("Sales Report has been created.");
        } catch (IOException e) {
            System.out.println("Couldn't create or replace file.");
        }
        return sales.toString();
    }

}

