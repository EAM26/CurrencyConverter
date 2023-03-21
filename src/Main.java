import java.sql.SQLOutput;
import java.util.*;


class Main {

    // Main menu options
    static String[] mainMenuOptions = {"Quit", "Add new currency", "Convert amount"};

    // List of currencies
    static String[] currencyNames = {"EUR","USD","JPY","BGN","CZK","DKK","GBP","HUF","PLN","RON","SEK","CHF","ISK",
            "NOK","TRY","AUD","BRL","CAD","CNY","HKD","IDR","ILS","INR","KRW","MXN","MYR","NZD","PHP","SGD",
            "THB","ZAR"};


    // List of currency-rates
    static double[] currencyRates = {1.0, 1.0937, 140.45, 1.9558,23725, 7.4443, 0.89250, 386.58, 4.6920, 4.9020, 11.3323,
            0.9989, 153.70, 10.9783, 20.5806, 1.5499, 5.5414, 1.4616, 7.3689, 8.5802, 16312.70, 3.7207, 89.5920,
            1346.17, 20.4625, 4.6570, 1.6886,58721, 1.4331,36114, 18.7624};

    // Create currencies starters-arraylist that holds all currencies  with currencyNames and currencyRates
    static  ArrayList<Currency> currencies = new ArrayList<>();
    static {
        for (int i = 0; i < currencyNames.length; i++) {
            currencies.add(new Currency(currencyNames[i], currencyRates[i]));
        }
        // Sort currencies by name
//        Collections.sort(currencies, Comparator.comparing(Currency::getName));
        sortCurrencies();
    }

    // Show main menu options
    public static void sortCurrencies() {
        Collections.sort(currencies, Comparator.comparing(Currency::getName));
    }
    public static void showMainMenu() {
        System.out.println("\nMAIN MENU\n");
        for (int i = 0; i < mainMenuOptions.length ; i++) {
            System.out.println((i + 1) + ". " + mainMenuOptions[i]);
        }
    }

    // Show sorted menu of all currencies
    public static void showCurrencyMenu() {
        System.out.println("\nCURRENCY MENU\n");
        int countToFive = 0;
        int index = 0;
        while(index < currencies.size()) {
            System.out.print((index + 1) + "." + currencies.get(index).getName() + "\t");
            index++;
            countToFive++;
            if(countToFive == 6) {
                System.out.print("\n");
                countToFive = 0;
            }
        }
        System.out.println();
    }

    // Checks if scanner input is a double
    private static double checkForDouble(Scanner scanner, String prompt) {
        double number;
        while(true) {
            System.out.print(prompt);
            try {
                number = scanner.nextDouble();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Not a valid input. Try again.");
                scanner.nextLine();
            }
        }
        return number;
    }

    // Checks if scanner input is an int
    private static int checkForInt(Scanner scanner, String prompt) {
        int number;
        while(true) {
            System.out.print(prompt);
            try {
                number = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Not a valid input. Try again.");
                scanner.nextLine();
            }
        }
        return number;
    }

    // Check if number is in bounds
    private static boolean checkValidInt(int number, ArrayList optionsList) {
        if(number < 0 || number >= optionsList.size()) {
            System.out.println("That is not a valid input. Try again.");
            return false;
        }
        return true;
    }

    // Returns the currency chosen from menu
    // Credits to Nick: scanner prompt as parameters
    public static Currency getCurrency(Scanner scanner, String prompt, ArrayList<Currency> currencies) {
        int number = -1;
        boolean isValidNumber = false;
        while(!isValidNumber) {
            number = checkForInt(scanner,prompt );
            isValidNumber = checkValidInt(number, currencies);
        }
        return currencies.get(number -1);
    }

    // Returns validated amount
    // Credits to Nick: scanner prompt as parameters
    public static double getAmount(Scanner scanner, String prompt) {
        double amount = 0;
        boolean isValidDouble = false;
        while(!isValidDouble) {
            amount = checkForDouble(scanner, prompt);
            if(amount > 0) {
                return amount;
            } else {
                System.out.println("Not a legal amount. Try again");
            }
        }
       return 0;
    }

    public static double convertAmount(Currency startCurr, Currency goalCurr, double Amount) {
        double tempAmount = Amount / startCurr.getRate();
        return tempAmount * goalCurr.getRate();
    }

    // Pause method
    public static void pauseLoop(Scanner scanner) {
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    // Create and add new currency with name and rate
    public static void addCurrency(Scanner scanner, String askName, String askRate) {
        boolean nameNotValid = true;
        String name = "This is never shown!";
        while(nameNotValid) {
            System.out.print(askName);
            name = checkNameLength(scanner).toUpperCase();
            nameNotValid  = checkNameExists(name);
        }

        double rate = checkForDouble(scanner, askRate);
        currencies.add(new Currency(name, rate));
        sortCurrencies();
    }

    // Check if name of new currency already exists
    private static boolean checkNameExists(String name) {
        for (Currency curr: currencies
             ) {
            if(curr.getName().equals(name)) {
                System.out.println("Name already exists. Try again.");
                return true;
            }
        }
        return false;
    }

    // Check if name new currency has three chars
    private static String checkNameLength(Scanner scanner) {
        String name;
        while(true) {
            name = scanner.nextLine();
            if(name.length() != 3) {
                System.out.println("The name should have three characters.");
            } else {
                return name;
            }
        }
    }

    // Credits to Jorge: clean main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        boolean mainMenuIsRunning = true;

        showMainMenu();

        boolean isAddingCurrency = true;

        addCurrency(scanner, "\nEnter name of currency (3 chars): ",
                "Enter the value: ");

        showCurrencyMenu();

        boolean conversionIsRunning = false;


        while (conversionIsRunning) {
            showCurrencyMenu();

            // Prompt for start currency from options menu, Credits to Nick: scanner prompt as parameters
            Currency startCurrency = getCurrency(scanner, "Enter nr of your Start currency: " ,currencies);

            // Prompt for amount, Credits to Nick: scanner prompt as parameters
            double amount = getAmount(scanner, "Enter the amount: ");

            // Prompt for goal currency from options menu,  Credits to Nick: scanner prompt as parameters
            Currency goalCurrency = getCurrency(scanner, "Enter nr of your Goal currency: ",currencies);

            // Convert and print result
            double result = (convertAmount(startCurrency, goalCurrency, amount));
            System.out.println(amount + " " + startCurrency.getName() + " = " + result +
                    " " + goalCurrency.getName() );

            pauseLoop(scanner);
        }
    }

}