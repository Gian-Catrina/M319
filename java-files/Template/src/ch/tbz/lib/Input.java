package ch.tbz.lib;

import java.util.*;
import java.time.*;
import java.time.format.*;
import java.util.function.Function;

/**
 * Utility-Klasse für einfache Konsolen-Eingaben in Java.
 * 
 * Diese Klasse bietet komfortable Methoden zur Eingabe verschiedener Datentypen
 * (int, double, String, LocalDate, etc.) mit automatischer Validierung und 
 * Fehlerbehandlung.
 * 
 * <h3>Verwendungsbeispiel:</h3>
 * <pre>
 * import static ch.tbz.lib.Input.*;
 * 
 * public class Main {
 *     public static void main(String[] args) {
 *         int alter = inputInt("Wie alt bist du?");
 *         String name = inputString("Wie heißt du?");
 *         LocalDate geburt = inputDate("Geburtsdatum");
 *         
 *         System.out.println(name + " ist " + alter + " Jahre alt.");
 *         Input.close(); // Ressourcen freigeben
 *     }
 * }
 * </pre>
 * 
 * <h3>Konfiguration:</h3>
 * <pre>
 * Input.setAskAgain(false);           // Nur einmal fragen
 * Input.setNumberFormatError("Ungültige Zahl!");
 * Input.setDateFormat("yyyy-MM-dd");  // Datumsformat ändern
 * </pre>
 *
 * @version 0.1 (Oct 8, 2014) Original
 * @version 0.2 (Jan 24, 2020) JavaDoc updated
 * @version 0.3 (Jan 25, 2020) inputDate, inputTime, inputDateTime added
 * @author Philipp Gressly Freimann V0.1
 * @author Michael Kellenberger V0.2 ...
 * @version 1.0 (März 2026) - Vollständig überarbeitete Version
 */
public class Input {
    
    // ============ PRIVATE KONSTANTEN ============
    private static final Scanner sc = new Scanner(System.in);
    
    private static String dateFormat = "dd.MM.yyyy";
    private static String timeFormat = "HH:mm:ss";
    private static String dateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    
    // ============ PRIVATE VARIABLEN ============
    private static boolean askAgain = true;
    private static String addQuestion = "";
    private static String numberFormatErrorMessage = "❌ ERROR: Bitte geben Sie eine gültige Zahl ein!";
    private static String dtFormatErrorMessage = "❌ ERROR: Ungültiges Datums-/Zeitformat!";

    
    // ============ SETTER-METHODEN ============
    
    /**
     * Legt fest, ob der Benutzer bei ungültiger Eingabe erneut gefragt werden soll.
     * 
     * @param askAgain {@code true} = bei Fehler erneut fragen (Standard),
     *                 {@code false} = nur einmal fragen
     */
    public static void setAskAgain(boolean askAgain) {
        Input.askAgain = askAgain;
    }

    /**
     * Setzt die Fehlermeldung für ungültige Zahleneingaben.
     * 
     * @param errorMessage Die anzuzeigende Fehlermeldung
     */
    public static void setNumberFormatError(String errorMessage) {
        Input.numberFormatErrorMessage = errorMessage;
    }

    /**
     * Setzt die Fehlermeldung für ungültige Datums-/Zeiteingaben.
     * 
     * @param errorMessage Die anzuzeigende Fehlermeldung
     */
    public static void setDTFormatError(String errorMessage) {
        Input.dtFormatErrorMessage = errorMessage;
    }

    /**
     * Setzt das Datumsformat für {@link #inputDate(String)}.
     * Beispiele: "dd.MM.yyyy", "yyyy-MM-dd", "dd/MM/yyyy"
     * 
     * @param format Das neue Datumsformat (muss gültig für DateTimeFormatter sein)
     */
    public static void setDateFormat(String format) {
        Input.dateFormat = format;
    }

    /**
     * Setzt das Zeitformat für {@link #inputTime(String)}.
     * Beispiele: "HH:mm:ss", "HH:mm", "HH:mm:ss.SSS"
     * 
     * @param format Das neue Zeitformat
     */
    public static void setTimeFormat(String format) {
        Input.timeFormat = format;
    }

    /**
     * Setzt das Datums-Zeit-Format für {@link #inputDateTime(String)}.
     * Beispiel: "dd.MM.yyyy HH:mm:ss"
     * 
     * @param format Das neue Datums-Zeit-Format
     */
    public static void setDateTimeFormat(String format) {
        Input.dateTimeFormat = format;
    }

    /**
     * Setzt einen zusätzlichen Fragentext, der vor jeder Eingabe angezeigt wird.
     * Der Platzhalter %s wird durch die spezifische Frage ersetzt.
     * 
     * Beispiel: {@code setAddQuestion("Bitte eingeben (%s): ")}
     * 
     * @param addQuestion Text mit %s als Platzhalter (wird automatisch ergänzt, falls nötig)
     */
    public static void setAddQuestion(String addQuestion) {
        if (addQuestion.indexOf("%s") < 0) {
            addQuestion = addQuestion + " (%s): ";
        }
        Input.addQuestion = addQuestion;
    }

    /**
     * Schließt den Scanner und gibt die Ressourcen frei.
     * Sollte am Ende des Programms aufgerufen werden.
     */
    public static void close() {
        if (sc != null) {
            sc.close();
        }
    }

    
    // ============ PRIVATE HILFSMETHODEN ============
    
    /**
     * Zentrale Methode für wiederholte Eingabeversuche mit Fehlerbehandlung.
     * Implementiert das Template-Pattern zur Reduktion von Code-Redundanz.
     * 
     * @param <T> Der Rückgabedatentyp
     * @param question Die Frage für den Benutzer
     * @param errorMessage Die Fehlermeldung bei ungültiger Eingabe
     * @param parser Eine Funktion, die den String in den gewünschten Typ konvertiert
     * @return Der geparste Wert
     */
    private static <T> T retryInput(String question, String errorMessage, Function<String, T> parser) {
        while (true) {
            try {
                String input = inputString(question);
                return parser.apply(input);
            } catch (Exception e) {
                System.out.println(errorMessage);
                if (!askAgain) {
                    return null;
                }
            }
        }
    }

    /**
     * Hilfsmethode: Gibt die Frage aus (entweder direkt oder mit vordefiniertem Format).
     * 
     * @param question Die auszugebende Frage
     */
    private static void printQuestion(String question) {
        if (null == addQuestion || addQuestion.isEmpty()) {
            System.out.print(question + " ");
        } else {
            System.out.printf(addQuestion, question);
        }
    }

    
    // ============ ÖFFENTLICHE EINGABE-METHODEN ============
    
    /**
     * Liest eine Textzeile vom Benutzer ein.
     * Leere Eingaben werden wiederholt abgefragt (bis auf Leerzeichen trimmed).
     * 
     * @param question Die Frage für den Benutzer
     * @return Der eingegebene Text (ohne führende/nachfolgende Leerzeichen)
     * 
     * <pre>
     * String name = inputString("Dein Name");
     * </pre>
     */
    public static String inputString(String question) {
        String input = "";
        while (input.isEmpty()) {
            if (askAgain || input.isEmpty()) {
                printQuestion(question);
            }
            input = sc.nextLine().trim();
            if (!askAgain && input.isEmpty()) {
                return "";
            }
        }
        return input;
    }

    /**
     * Liest eine Ganzzahl (int) vom Benutzer ein.
     * Bei ungültiger Eingabe wird eine Fehlermeldung angezeigt und erneut gefragt.
     * 
     * @param question Die Frage für den Benutzer
     * @return Die eingegebene Ganzzahl
     * 
     * <pre>
     * int alter = inputInt("Wie alt bist du?");
     * </pre>
     */
    public static int inputInt(String question) {
        return retryInput(question, numberFormatErrorMessage, Integer::parseInt);
    }

    /**
     * Liest eine Ganzzahl (long) vom Benutzer ein.
     * Für größere Zahlen als {@link #inputInt(String)}.
     * 
     * @param question Die Frage für den Benutzer
     * @return Die eingegebene lange Ganzzahl
     */
    public static long inputLong(String question) {
        return retryInput(question, numberFormatErrorMessage, Long::parseLong);
    }

    /**
     * Liest eine Ganzzahl (short) vom Benutzer ein.
     * Für kleinere Zahlen (Bereich: -32.768 bis 32.767).
     * 
     * @param question Die Frage für den Benutzer
     * @return Die eingegebene Short-Ganzzahl
     */
    public static short inputShort(String question) {
        return retryInput(question, numberFormatErrorMessage, Short::parseShort);
    }

    /**
     * Liest eine Ganzzahl (byte) vom Benutzer ein.
     * Für sehr kleine Zahlen (Bereich: -128 bis 127).
     * 
     * @param question Die Frage für den Benutzer
     * @return Die eingegebene Byte-Ganzzahl
     */
    public static byte inputByte(String question) {
        return retryInput(question, numberFormatErrorMessage, Byte::parseByte);
    }

    /**
     * Liest eine Dezimalzahl (double) vom Benutzer ein.
     * Verwendet Doppelpräzision (64-Bit).
     * Dezimaltrennzeichen: Punkt (.) oder Komma (,)
     * 
     * @param question Die Frage für den Benutzer
     * @return Die eingegebene Dezimalzahl
     * 
     * <pre>
     * double preis = inputDouble("Preis in CHF");
     * </pre>
     */
    public static double inputDouble(String question) {
        return retryInput(question, numberFormatErrorMessage, s -> {
            s = s.replace(',', '.');  // Komma in Punkt umwandeln
            return Double.parseDouble(s);
        });
    }

    /**
     * Liest eine Dezimalzahl (float) vom Benutzer ein.
     * Verwendet Einfachpräzision (32-Bit).
     * 
     * @param question Die Frage für den Benutzer
     * @return Die eingegebene Dezimalzahl
     */
    public static float inputFloat(String question) {
        return retryInput(question, numberFormatErrorMessage, s -> {
            s = s.replace(',', '.');
            return Float.parseFloat(s);
        });
    }

    /**
     * Liest ein einzelnes Zeichen vom Benutzer ein.
     * Weitere Zeichen in der gleichen Zeile werden ignoriert.
     * Bei Leereingabe wird das Leerzeichen-Zeichen zurückgegeben.
     * 
     * @param question Die Frage für den Benutzer
     * @return Das erste eingegebene Zeichen
     * 
     * <pre>
     * char wahl = inputChar("Wähle (J/N)");
     * </pre>
     */
    public static char inputChar(String question) {
        String input = inputString(question);
        return input.isEmpty() ? ' ' : input.charAt(0);
    }

    /**
     * Liest eine Ja/Nein-Entscheidung vom Benutzer ein.
     * 
     * @param question Die Frage für den Benutzer
     * @return {@code true} wenn die Antwort NICHT mit 'n' oder 'N' beginnt,
     *         {@code false} wenn sie mit 'n' oder 'N' beginnt oder leer ist
     * 
     * <pre>
     * if (inputBoolean("Möchtest du fortfahren?")) {
     *     // Benutzer hat mit Ja geantwortet
     * }
     * </pre>
     */
    public static boolean inputBoolean(String question) {
        String input = inputString(question).toLowerCase();
        return !input.isEmpty() && !input.startsWith("n");
    }

    /**
     * Liest ein Datum vom Benutzer ein.
     * Das Standardformat ist "dd.MM.yyyy" und kann mit {@link #setDateFormat(String)} geändert werden.
     * 
     * @param question Die Frage für den Benutzer
     * @return Das eingegebene Datum als {@link LocalDate}
     * 
     * <pre>
     * LocalDate geburt = inputDate("Geburtsdatum");
     * </pre>
     */
    public static LocalDate inputDate(String question) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormat);
        String hint = String.format(" (%s)", dateFormat);
        return retryInput(question + hint, dtFormatErrorMessage, 
                         s -> LocalDate.parse(s, fmt));
    }

    /**
     * Liest eine Uhrzeit vom Benutzer ein.
     * Das Standardformat ist "HH:mm:ss" und kann mit {@link #setTimeFormat(String)} geändert werden.
     * 
     * @param question Die Frage für den Benutzer
     * @return Die eingegebene Zeit als {@link LocalTime}
     * 
     * <pre>
     * LocalTime start = inputTime("Startzeit");
     * </pre>
     */
    public static LocalTime inputTime(String question) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(timeFormat);
        String hint = String.format(" (%s)", timeFormat);
        return retryInput(question + hint, dtFormatErrorMessage,
                         s -> LocalTime.parse(s, fmt));
    }

    /**
     * Liest ein Datum mit Uhrzeit vom Benutzer ein.
     * Das Standardformat ist "dd.MM.yyyy HH:mm:ss" und kann mit 
     * {@link #setDateTimeFormat(String)} geändert werden.
     * 
     * @param question Die Frage für den Benutzer
     * @return Das eingegebene Datum und die Uhrzeit als {@link LocalDateTime}
     * 
     * <pre>
     * LocalDateTime termin = inputDateTime("Termin");
     * </pre>
     */
    public static LocalDateTime inputDateTime(String question) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateTimeFormat);
        String hint = String.format(" (%s)", dateTimeFormat);
        return retryInput(question + hint, dtFormatErrorMessage,
                         s -> LocalDateTime.parse(s, fmt));
    }

} // end of class Input