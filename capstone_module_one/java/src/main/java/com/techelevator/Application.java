package com.techelevator;

import com.techelevator.util.VmLog;
import com.techelevator.vending.VendingMachine;
import com.techelevator.view.Menu;
import com.techelevator.view.MenuDrivenCLI;

public class Application {

    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String MAIN_MENU_OPTION_SALES_REPORT = Menu.HIDDEN_OPTION; //static constant
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT};
    private static final String PURCHASE_MENU_OPTION_DEPOSIT_MONEY = "Deposit Money";
    private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
    private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
    private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_DEPOSIT_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION};

    private final MenuDrivenCLI ui = new MenuDrivenCLI();
    private VendingMachine vm = new VendingMachine("inventory.txt");


    public static void main(String[] args) {
        Application application = new Application();
        application.run();
    }

    public void run() {
        VmLog.log(" Vending Machine Accessed");
        vm.loadInventory();
        boolean running = true;

        while (running) {
            String selection = ui.promptForSelection(MAIN_MENU_OPTIONS);
            if (selection.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                ui.output("Displaying available inventory");
                ui.output(vm.displayInventory());
                ui.pauseOutput();
            } else if (selection.equals(MAIN_MENU_OPTION_PURCHASE)) {
                handlePurchaseMenu();
            } else if (selection.equals(MAIN_MENU_OPTION_EXIT)) {
                running = false;
            } else if (selection.equals(MAIN_MENU_OPTION_SALES_REPORT)) {
                ui.output(vm.createAndDisplaySalesReport());
                ui.pauseOutput();
            }
        }
    }

    private void handlePurchaseMenu() {
        boolean running = true;
        while (running) {
            String selection = ui.promptForSelection(PURCHASE_MENU_OPTIONS);

            if (selection.equals(PURCHASE_MENU_OPTION_DEPOSIT_MONEY)) {
                handleDeposit();
                ui.output("Current Balance: " + vm.displayCustomerBalance());
            } else if (selection.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
                ui.output(vm.displayInventory());
                ui.output("Your current balance is: " + vm.displayCustomerBalance());
                String userSelection = ui.promptForString("Make your selection: ").toUpperCase();
                ui.output(vm.purchaseItem(userSelection));
                ui.output("Your new balance is: " + vm.displayCustomerBalance());
                ui.pauseOutput();
            } else if (selection.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
                ui.output(vm.returnChange());
                ui.pauseOutput();
                running = false;

            }
        }
    }

    private void handleDeposit() {
        boolean finished = false;
        while (!finished) {
            String money = ui.promptForString("How much would you like to deposit? ");
            try {
                vm.depositMoney(money);
                finished = true;
            } catch (IllegalArgumentException e) {
                ui.output("*** " + money + " is not a valid option. Please enter a positive whole number amount ***\n");
            }
        }
    }

}
