public class Frage {
    public String frageText;
    public String[] antworten = new String[4]; // A, B, C, D
    public String richtigeAntwort; // "A", "B", "C", "D"

    public Frage(String frageText, String[] antworten, String richtigeAntwort) {
        this.frageText = frageText;
        this.antworten = antworten;
        this.richtigeAntwort = richtigeAntwort;
    }
}
