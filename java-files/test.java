// Defines package
package ch.tbz;

// Imports libraries
import static java.lang.Math.*;   // System-IO Library for abbriviation purpose to static functions
import static java.lang.System.*;     // Mathematic Library for abbriviation purpose to static functions
import java.util.*;                 // Random and Scanner are part of this library

//Every program must be placed in a class ...
public class test {

    // Our main function which runs the program12
    public static void main(String[] args ) {

        // Title
        System.out.println("Dreiecksberechnung:");
        out.println();

        // Input
        double a = inputDouble("Geben Sie die Seite a ein: ");
        double b = inputDouble("Geben Sie die Seite b ein: ");

        // Calculation
        double c = Math.sqrt(Math.pow(a,2) + pow(b,2));

        // System.Out function (Short version)
        out.println("Das Resultat ist: " + c);

        // Use function Random to generate a random Number
        Random random = new Random();
        //Calling the nextInt() method
        System.out.println("Hier noch eine zufällige Ganzzahl: " + random.nextInt());

    }

    private static double inputDouble(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Ungültige Eingabe. Bitte eine Zahl eingeben: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
