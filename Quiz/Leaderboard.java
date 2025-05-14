package Quiz;

import java.io.*;
import java.util.*;

public class Leaderboard {

    private static final String SCORE_FILE = "Quiz/scores.txt";

    /**
     * Fügt den Spieler mit seinen Punkten hinzu oder aktualisiert seine Punktzahl, wenn der Spieler bereits existiert.
     */
    public static void updateLeaderboard(String username, int newScore) {
        // Daten einlesen und aktualisieren
        List<String[]> datenListe = ladeDaten(SCORE_FILE);
        boolean benutzerGefunden = false;

        // Überprüfen, ob der Benutzer bereits in der Liste ist und seine Punktzahl aktualisieren
        for (String[] eintrag : datenListe) {
            if (eintrag[0].equals(username)) {
                eintrag[1] = String.valueOf(newScore);  // Punktzahl aktualisieren
                benutzerGefunden = true;
                break;
            }
        }

        // Wenn der Benutzer nicht gefunden wurde, füge einen neuen Eintrag hinzu
        if (!benutzerGefunden) {
            datenListe.add(new String[]{username, String.valueOf(newScore)});
        }

        // Liste nach Punkten sortieren
        datenListe.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

        // Daten zurück in die Datei schreiben
        schreibeDaten(SCORE_FILE, datenListe);
    }

    /**
     * Lädt die Punktedaten aus der Datei.
     */
    private static List<String[]> ladeDaten(String dateiname) {
        List<String[]> datenListe = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dateiname))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split(":");
                if (teile.length == 2) {
                    datenListe.add(new String[]{teile[0], teile[1]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datenListe;
    }

    /**
     * Schreibt die Punktedaten in die Datei.
     */
    private static void schreibeDaten(String dateiname, List<String[]> datenListe) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dateiname))) {
            for (String[] eintrag : datenListe) {
                writer.write(eintrag[0] + ":" + eintrag[1]);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Öffnet das Leaderboard in einem neuen JFrame.
     */
    public Leaderboard() {
        // Hier wird das GUI mit dem Leaderboard angezeigt
        // Details wie im vorherigen Code
    }

    /**
     * Liest und sortiert die Punktdaten für die Anzeige.
     */
    private Object[][] ladeUndSortiereDaten(String dateiname) {
        List<String[]> datenListe = ladeDaten(dateiname);

        // Sortieren der Liste nach Punkten in absteigender Reihenfolge
        datenListe.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

        Object[][] daten = new Object[datenListe.size()][2];
        for (int i = 0; i < datenListe.size(); i++) {
            daten[i][0] = datenListe.get(i)[0];
            daten[i][1] = datenListe.get(i)[1];
        }
        return daten;
    }
}
