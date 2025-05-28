package Quiz;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Die Klasse {@code FragenLader} dient zum Einlesen von Fragen aus einer Textdatei
 * und erstellt daraus {@link Frage}-Objekte.
 */
public class FragenLader {

    /**
     * Lädt eine Liste von {@link Frage}-Objekten aus einer angegebenen Textdatei.
     * Die Datei muss im folgenden Format geschrieben sein:
     * <pre>
     * Frage: ...
     * A: ...
     * B: ...
     * C: ...
     * D: ...
     * Richtig: A
     *
     * Frage: ...
     * ...
     * </pre>
     *
     * @param dateiname Der Pfad zur Datei, die Fragen enthält
     * @return Eine Liste mit {@link Frage}-Objekten
     * @throws IOException Wenn beim Lesen der Datei ein Fehler auftritt
     */
    public static List<Frage> ladeFragen(String dateiname) throws IOException {
        // Lese gesamten Dateiinhalt als String
        String content = Files.readString(Path.of(dateiname));
        List<Frage> fragenListe = new ArrayList<>();

        // Regex zum Extrahieren der einzelnen Fragenblöcke
        Pattern pattern = Pattern.compile(
                "Frage: (.*?)\\s*A: (.*?)\\s*B: (.*?)\\s*C: (.*?)\\s*D: (.*?)\\s*Richtig: (\\w)",
                Pattern.DOTALL);

        Matcher matcher = pattern.matcher(content);

        // Extrahiere alle Übereinstimmungen und erstelle Frage-Objekte
        while (matcher.find()) {
            String frage = matcher.group(1).trim();
            String[] antworten = {
                    matcher.group(2).trim(),
                    matcher.group(3).trim(),
                    matcher.group(4).trim(),
                    matcher.group(5).trim()
            };
            String richtig = matcher.group(6).trim();
            fragenListe.add(new Frage(frage, antworten, richtig));
        }

        return fragenListe;
    }
}
