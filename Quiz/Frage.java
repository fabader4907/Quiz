package Quiz;

/**
 * Die Klasse {@code Frage} repräsentiert eine einzelne Quizfrage.
 * Sie enthält den Fragetext, vier Antwortmöglichkeiten und die korrekte Antwort.
 */
public class Frage {

    /** Der Text der Frage */
    public String frageText;

    /** Die vier Antwortmöglichkeiten (z. B. A, B, C, D) */
    public String[] antworten = new String[4];

    /** Die richtige Antwort (als Buchstabe: "A", "B", "C" oder "D") */
    public String richtigeAntwort;

    /**
     * Konstruktor zur Erstellung einer Frage mit Antworten und korrekter Lösung.
     *
     * @param frageText        Der Fragetext
     * @param antworten        Ein Array mit vier möglichen Antworten
     * @param richtigeAntwort  Der Buchstabe ("A"–"D"), der die richtige Antwort angibt
     */
    public Frage(String frageText, String[] antworten, String richtigeAntwort) {
        this.frageText = frageText;
        this.antworten = antworten;
        this.richtigeAntwort = richtigeAntwort;
    }
}
