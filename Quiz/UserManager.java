package Quiz;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Die Klasse {@code UserManager} stellt Methoden zur Verfügung, um Benutzerinformationen
 * aus einer Datei zu laden. Die Benutzer werden im Format "Benutzername:Passwort" gespeichert.
 */
public class UserManager {

    /**
     * Lädt die Anzahl der registrierten Benutzer aus der Datei "users.txt".
     *
     * @return Die Anzahl der Benutzer. Gibt 0 zurück, wenn die Datei nicht existiert.
     */
    public static int ladeBenutzeranzahl() {
        File file = new File("Quiz/users.txt");
        if (!file.exists()) return 0;

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) count++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Lädt alle Benutzerinformationen aus der Datei "users.txt" in eine Map.
     * Die Datei muss im Format "Benutzername:Passwort" pro Zeile aufgebaut sein.
     *
     * @return Eine Map mit Benutzernamen als Schlüssel und Passwörtern als Werte.
     *         Gibt eine leere Map zurück, wenn die Datei nicht existiert oder fehlerhaft ist.
     */
    public static Map<String, String> ladeBenutzer() {
        Map<String, String> userMap = new HashMap<>();
        File file = new File("Quiz/users.txt");
        if (!file.exists()) return userMap;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split(":");
                if (teile.length == 2) {
                    userMap.put(teile[0], teile[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userMap;
    }
}
