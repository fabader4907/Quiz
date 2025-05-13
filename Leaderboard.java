import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Die Klasse {@code Leaderboard} stellt ein eigenständiges GUI-Fenster dar,
 * das eine sortierte Bestenliste (Leaderboard) aus einer Datei anzeigt.
 */
public class Leaderboard {

    /**
     * Öffnet das Leaderboard in einem neuen eigenen JFrame.
     */
    public Leaderboard() {
        JFrame fenster = new JFrame("Leaderboard");
        fenster.setSize(400, 300);
        fenster.setLocationRelativeTo(null);
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Jetzt als Hauptfenster

        String[] spalten = {"Spieler", "Punkte"};
        Object[][] daten = ladeUndSortiereDaten("scores.txt");

        JTable tabelle = new JTable(new DefaultTableModel(daten, spalten));
        JScrollPane scrollPane = new JScrollPane(tabelle);

        tabelle.setFillsViewportHeight(true);
        tabelle.setRowHeight(30);
        tabelle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelle.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        fenster.add(scrollPane, BorderLayout.CENTER);
        fenster.setVisible(true);
    }

    /**
     * Liest die Punktzahlen aus der angegebenen Datei und sortiert sie absteigend.
     *
     * @param dateiname Name der Datei (z. B. "scores.txt")
     * @return 2D-Array zur Darstellung in der Tabelle
     */
    private Object[][] ladeUndSortiereDaten(String dateiname) {
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
            JOptionPane.showMessageDialog(null, "Fehler beim Laden der Datei!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }

        datenListe.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

        Object[][] daten = new Object[datenListe.size()][2];
        for (int i = 0; i < datenListe.size(); i++) {
            daten[i][0] = datenListe.get(i)[0];
            daten[i][1] = datenListe.get(i)[1];
        }
        return daten;
    }


}
