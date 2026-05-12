import java.util.Scanner;

public class rechner {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Taschenrechner ===");

        System.out.print("Erste Zahl: ");
        double zahl1 = scanner.nextDouble();

        System.out.print("Operator (+, -, *, /): ");
        char operator = scanner.next().charAt(0);

        System.out.print("Zweite Zahl: ");
        double zahl2 = scanner.nextDouble();

        double ergebnis;

        switch (operator) {
            case '+':
                ergebnis = zahl1 + zahl2;
                break;

            case '-':
                ergebnis = zahl1 - zahl2;
                break;

            case '*':
                ergebnis = zahl1 * zahl2;
                break;

            case '/':
                if (zahl2 != 0) {
                    ergebnis = zahl1 / zahl2;
                } else {
                    System.out.println("Fehler: Division durch 0!");
                    return;
                }
                break;

            default:
                System.out.println("Ungültiger Operator!");
                return;
        }

        System.out.println("Ergebnis: " + ergebnis);

        scanner.close();
    }
}