package Quiz;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code FragenLoeschenStart} stellt ein GUI-Fenster bereit, um eine Frage
 * aus einer gegebenen Textdatei auszuwählen und zu löschen.
 * <p>
 * Jede Frage wird als Textblock im Format gespeichert:
 * <pre>
 * Frage: ...
 * A: ...
 * B: ...
 * C: ...
 * D: ...
 * Richtig: ...
 * </pre>
 * mit einer Leerzeile nach jedem Block.
 */
public class FragenLoeschenStart extends JFrame {

    private Clip clip;
    private JComboBox<String> frageDropdown;
    private String dateiname;
    private List<String> fragenBlöcke;

    /**
     * Erstellt das Fenster zur Anzeige und Auswahl von Fragen, die aus der Datei gelöscht werden können.
     *
     * @param dateiname Der Pfad zur Datei mit den gespeicherten Fragen.
     */
    public FragenLoeschenStart(String dateiname) {
        this.dateiname = dateiname;
        this.fragenBlöcke = new ArrayList<>();
        // Musik starten
        starteMusik("Quiz/music/Quiz-show.wav"); // Pfad zur WAV-Datei anpassen

        setTitle("Frage löschen");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titel = new JLabel("Frage aus Datei löschen", SwingConstants.CENTER);
        titel.setFont(new Font("Arial", Font.BOLD, 32));
        titel.setForeground(new Color(200, 200, 200));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        frageDropdown = new JComboBox<>();
        frageDropdown.setPreferredSize(new Dimension(600, 40));
        JLabel dropdownLabel = createLabel("Wähle eine Frage:");
        add(dropdownLabel, gbc);
        gbc.gridx = 1;
        add(frageDropdown, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton loeschenBtn = new JButton("Frage löschen");
        loeschenBtn.setBackground(new Color(220, 70, 70));
        loeschenBtn.setForeground(Color.WHITE);
        loeschenBtn.setFont(new Font("Arial", Font.BOLD, 22));
        loeschenBtn.setPreferredSize(new Dimension(300, 60));
        add(loeschenBtn, gbc);

        loeschenBtn.addActionListener(e -> frageLoeschen());

        gbc.gridx = 1;
        JButton zurueckBtn = new JButton("Zurück");
        zurueckBtn.setBackground(new Color(100, 149, 237));
        zurueckBtn.setForeground(Color.WHITE);
        zurueckBtn.setFont(new Font("Arial", Font.BOLD, 22));
        zurueckBtn.setPreferredSize(new Dimension(300, 60));
        add(zurueckBtn, gbc);

        zurueckBtn.addActionListener(e -> {
            dispose();
            new AdminThemenauswahl();
        });

        ladeFragen();
        setVisible(true);
    }

    /**
     * Erstellt ein standardisiertes JLabel mit heller Schrift für Eingabefelder.
     *
     * @param text Der anzuzeigende Text.
     * @return Ein fertig konfiguriertes JLabel.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(220, 220, 220));
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }

    /**
     * Lädt alle Fragenblöcke aus der Datei und befüllt das Dropdown-Menü
     * mit den Fragezeilen (erste Zeile jedes Blocks).
     */
    private void ladeFragen() {
        fragenBlöcke.clear();
        frageDropdown.removeAllItems();

        try (BufferedReader reader = new BufferedReader(new FileReader(dateiname))) {
            StringBuilder block = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                block.append(line).append("\n");
                if (line.trim().isEmpty()) {
                    String blockText = block.toString().trim();
                    if (blockText.startsWith("Frage: ")) {
                        String frageZeile = blockText.split("\n")[0];
                        frageDropdown.addItem(frageZeile.replace("Frage: ", ""));
                        fragenBlöcke.add(blockText);
                    }
                    block.setLength(0); // zurücksetzen
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Datei.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Löscht die aktuell ausgewählte Frage aus der Datei.
     * Die Datei wird anschließend mit den verbleibenden Fragen neu geschrieben.
     */
    private void frageLoeschen() {
        int index = frageDropdown.getSelectedIndex();
        if (index < 0 || index >= fragenBlöcke.size()) {
            JOptionPane.showMessageDialog(this, "Bitte eine gültige Frage auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        fragenBlöcke.remove(index);

        try (PrintWriter writer = new PrintWriter(new FileWriter(dateiname))) {
            for (String block : fragenBlöcke) {
                writer.println(block);
                writer.println(); // Leerzeile zwischen Blöcken
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Schreiben der Datei.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Frage gelöscht!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        ladeFragen(); // Dropdown neu laden
    }
    private void starteMusik(String dateipfad) {
        try {
            File musikDatei = new File(dateipfad);
            if (!musikDatei.exists()) {
                System.err.println("Musikdatei nicht gefunden: " + dateipfad);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musikDatei);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Musik in Dauerschleife abspielen
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Beendet die aktuell laufende Hintergrundmusik, falls vorhanden.
     */
    private void stoppeMusik() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Überschreibt die dispose()-Methode, um beim Schließen die Musik zu stoppen.
     */
    @Override
    public void dispose() {
        stoppeMusik();
        super.dispose();
    }

}
