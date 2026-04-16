import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private final CashAcceptor cashAcceptor;

    private static boolean isExit = false;

    private final Scanner scanner = new Scanner(System.in);

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
        cashAcceptor = new CashAcceptor(0);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);
        int totalBalance = coinAcceptor.getAmount() + cashAcceptor.getAmount();

        print("Общий баланс: " + totalBalance);

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            print("Выберите способ пополнения: ");
            print("1 - Монетоприемник ");
            print("2 - Купюроприемник ");
            String method = fromConsole();

            if ("1".equals(method)) {
                coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
                print("Вы пополнили баланс на 10");
            } else if ("2".equals(method)) {
                print("Введите сумму для пополнения наличными ");
                try {
                    int cash = Integer.parseInt(fromConsole());
                    cashAcceptor.deposit(cash);
                    print("Вы пополнили баланс на " + cash);
                } catch (NumberFormatException e) {
                    print("Ошибка. Введите число");
                }
            } else {
                print("Ошибка. Для выбора действия выберите быть 1 или 2");
            } return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    int price = products.get(i).getPrice();
                    if (cashAcceptor.getAmount() >= price) {
                        cashAcceptor.setAmount(cashAcceptor.getAmount() - price);
                    } else {
                        int remaining = price - cashAcceptor.getAmount();
                        cashAcceptor.setAmount(0);
                        coinAcceptor.setAmount(coinAcceptor.getAmount() - remaining);
                    }
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попрбуйте еще раз.");
                chooseAction(products);
            }
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return scanner.nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
