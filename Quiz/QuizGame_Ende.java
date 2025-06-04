package Quiz;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Die Klasse QuizGame_Ende stellt das finale Quiz-Fenster dar, in dem der Benutzer
 * Fragen beantwortet und Punkte sammelt.
 * Nach der Beantwortung der Fragen werden die Punkte gespeichert und ein Highscore-Bildschirm angezeigt.
 */
public class QuizGame_Ende extends Basis {

    /** Liste der Fragen, die im Quiz gestellt werden */
    private List<Frage> fragenListe;

    /** Aktuelle Frageindex */
    private int aktuelleFrage = 0;

    /** Countdown-Label vor Start der Fragenanzeige */
    private JLabel countdownLabel;

    /** Swing-Timer für den Countdown */
    private Timer countdownTimer;

    /** Aktueller Countdown-Zähler */
    private int countdownValue = 5;

    /** Titel-Label des Quiz */
    private JLabel titelLabel;

    /** Gesamtpunktestand des Spielers */
    private int totalPoints;

    /** Label zur Anzeige des aktuellen Punktestands */
    private JLabel pointsLabel;

    /** Textbereich für die gestellte Frage */
    private JTextArea frageTextArea;

    /** Benutzername des Spielers */
    private String benutzername;

    /** Pfad zum Avatar des Spielers */
    private String avatarPfad;

    /** Musik */
    private Clip clip;

    /**
     * Konstruktor für das Quiz-Fenster.
     *
     * @param benutzername Name des Spielers
     * @param totalPoints  Startpunktestand
     * @param avatarPfad   Pfad zum Avatarbild
     */
    public QuizGame_Ende(String benutzername, int totalPoints, String avatarPfad) {
        super("Lyric!", new String[]{"A", "B", "C", "D"});
        this.benutzername = benutzername;
        this.totalPoints = totalPoints;
        this.avatarPfad = avatarPfad;
        starteMusik("Quiz/music/come-on-boy.wav");

        // UI-Design anpassen
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.background", new Color(40, 40, 40));
        UIManager.put("Panel.background", new Color(40, 40, 40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        try {
            fragenListe = FragenLader.ladeFragen("Quiz/lyrics.txt");
            Collections.shuffle(fragenListe);
            fragenListe = fragenListe.stream().limit(5).collect(Collectors.toList());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Fragen: " + e.getMessage());
            return;
        }

        getContentPane().setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();

        // Titelanzeige
        titelLabel = new JLabel("Beweise dein Können! Vervollständige die Lyrics", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titelLabel.setForeground(new Color(200, 200, 200));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(titelLabel, gbc);

        // Punkteanzeige
        pointsLabel = new JLabel("Punkte: " + totalPoints, SwingConstants.LEFT);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pointsLabel.setForeground(new Color(255, 105, 180));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 0);
        add(pointsLabel, gbc);

        // Quiz abbrechen-Button
        JButton btnAbbrechen = new JButton("Quiz abbrechen");
        btnAbbrechen.setBackground(new Color(200, 0, 0));
        btnAbbrechen.setForeground(Color.WHITE);
        btnAbbrechen.setFont(new Font("Arial", Font.BOLD, 12));
        btnAbbrechen.setFocusPainted(false);
        btnAbbrechen.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnAbbrechen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnAbbrechen.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Die Punkte werden nicht gespeichert.\nWillst du das Quiz wirklich abbrechen?",
                    "Abbrechen bestätigen",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (option == JOptionPane.YES_OPTION) {
                new Hauptmenu(benutzername);
                this.dispose();
            }
        });

        GridBagConstraints gbcAbbruch = new GridBagConstraints();
        gbcAbbruch.gridx = 1;
        gbcAbbruch.gridy = 1;
        gbcAbbruch.anchor = GridBagConstraints.NORTHEAST;
        gbcAbbruch.insets = new Insets(10, 0, 0, 10);
        add(btnAbbrechen, gbcAbbruch);

        // Countdown-Anzeige
        countdownLabel = new JLabel("" + countdownValue, SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 60));
        countdownLabel.setForeground(new Color(173, 216, 230));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 20, 10);
        add(countdownLabel, gbc);

        // Frage-Textanzeige
        frageTextArea = new JTextArea();
        frageTextArea.setEditable(false);
        frageTextArea.setLineWrap(true);
        frageTextArea.setWrapStyleWord(true);
        frageTextArea.setOpaque(false);
        frageTextArea.setForeground(new Color(200, 200, 200));
        frageTextArea.setFont(new Font("Arial", Font.PLAIN, 24));
        frageTextArea.setVisible(false);

        GridBagConstraints gbcTextArea = new GridBagConstraints();
        gbcTextArea.gridx = 0;
        gbcTextArea.gridy = 3;
        gbcTextArea.gridwidth = GridBagConstraints.REMAINDER;
        gbcTextArea.fill = GridBagConstraints.HORIZONTAL;
        gbcTextArea.weightx = 1.0;
        gbcTextArea.anchor = GridBagConstraints.NORTH;
        gbcTextArea.insets = new Insets(5, 10, 20, 10);
        add(frageTextArea, gbcTextArea);

        frageLabel.setVisible(false);

        // Antwortbuttons anpassen und verstecken
        for (JButton button : antwortButtons) {
            button.setVisible(false);
            button.setBackground(new Color(70, 70, 70));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        for (int i = 0; i < antwortButtons.length; i++) {
            GridBagConstraints buttonGbc = new GridBagConstraints();
            buttonGbc.gridx = i % 2;
            buttonGbc.gridy = 4 + (i / 2);
            buttonGbc.gridwidth = 1;
            buttonGbc.fill = GridBagConstraints.HORIZONTAL;
            buttonGbc.weightx = 0.5;
            buttonGbc.insets = new Insets(5, 10, 10, 10);
            add(antwortButtons[i], buttonGbc);
        }

        // Musikhinweis unten rechts
        JLabel musikHinweis = new JLabel("Musik: MondayHopes", SwingConstants.RIGHT);
        musikHinweis.setFont(new Font("Arial", Font.ITALIC, 10));
        musikHinweis.setForeground(new Color(180, 180, 180));
        GridBagConstraints gbcMusik = new GridBagConstraints();
        gbcMusik.gridx = 1;
        gbcMusik.gridy = 3;
        gbcMusik.anchor = GridBagConstraints.SOUTHEAST;
        gbcMusik.insets = new Insets(0, 0, 5, 10);
        add(musikHinweis, gbcMusik);


        // Countdown starten
        countdownTimer = new Timer(1000, e -> {
            countdownValue--;
            if (countdownValue > 0) {
                countdownLabel.setText("" + countdownValue);
            } else if (countdownValue == 0) {
                countdownLabel.setText("Los!");
                countdownTimer.stop();
                zeigeAntworten();
            }
        });
        countdownTimer.setInitialDelay(1000);
        countdownTimer.start();
    }

    /**
     * Zeigt die aktuellen Fragen und Antwortoptionen an.
     * Startet einen Punktetimer und verarbeitet die Antwortlogik.
     */
    private void zeigeAntworten() {
        if (aktuelleFrage >= fragenListe.size()) {
            speicherePunkte();
            return;
        }

        countdownLabel.setText("");
        frageTextArea.setVisible(true);
        titelLabel.setVisible(false);

        Frage frage = fragenListe.get(aktuelleFrage);
        frageTextArea.setText(frage.frageText);

        for (int i = 0; i < antwortButtons.length; i++) {
            JButton button = antwortButtons[i];
            button.setVisible(true);
            button.setText(frage.antworten[i]);
        }

        PointHandler ph = new PointHandler();
        ph.start();

        for (int i = 0; i < antwortButtons.length; i++) {
            JButton currentButton = antwortButtons[i];
            String antwortBuchstabe = switch (i) {
                case 0 -> "A";
                case 1 -> "B";
                case 2 -> "C";
                case 3 -> "D";
                default -> "";
            };

            for (ActionListener al : currentButton.getActionListeners()) {
                currentButton.removeActionListener(al);
            }

            currentButton.addActionListener(e -> {
                for (JButton b : antwortButtons) {
                    b.setEnabled(false);
                }

                boolean istRichtig = antwortBuchstabe.equals(frage.richtigeAntwort);
                int points;

                if (istRichtig) {
                    currentButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 10));
                    points = ph.getPoints();
                    totalPoints += points;
                    pointsLabel.setText("Punkte: " + totalPoints);
                } else {
                    points = 0;
                    currentButton.setBorder(BorderFactory.createLineBorder(Color.RED, 10));
                    int richtigeIndex = switch (frage.richtigeAntwort) {
                        case "A" -> 0;
                        case "B" -> 1;
                        case "C" -> 2;
                        case "D" -> 3;
                        default -> -1;
                    };
                    if (richtigeIndex != -1) {
                        antwortButtons[richtigeIndex].setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
                    }
                }

                Timer delayTimer = new Timer(3000, ev -> {
                    JOptionPane.showMessageDialog(
                            QuizGame_Ende.this,
                            "Du hast " + points + " Punkte erreicht!",
                            "Ergebnis",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    aktuelleFrage++;
                    for (JButton b : antwortButtons) {
                        b.setEnabled(true);
                        b.setBorder(null);
                    }
                    zeigeAntworten();
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            });
        }
    }

    /**
     * Speichert die erreichten Punkte des Benutzers in einer Datei.
     * Wenn bereits Punkte vorhanden sind, werden sie nur überschrieben,
     * wenn die neuen höher sind.
     */
    private void speicherePunkte() {
        List<String> lines = new ArrayList<>();
        String currentUserLine = benutzername + ":" + totalPoints;
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Quiz/scores.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(benutzername + ":")) {
                    int existingPoints = Integer.parseInt(line.split(":")[1]);
                    if (totalPoints > existingPoints) {
                        lines.add(currentUserLine);
                        updated = true;
                    } else {
                        lines.add(line);
                    }
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Lesen der Punkte: " + e.getMessage());
            return;
        }

        if (!updated) {
            lines.add(currentUserLine);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Quiz/scores.txt"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Punkte: " + e.getMessage());
        }

        JOptionPane.showMessageDialog(this, "Du hast es geschafft!", "Fertig", JOptionPane.INFORMATION_MESSAGE);
        new Leaderboard(benutzername, avatarPfad);
        this.dispose();
    }

    /**
     * Startet die Hintergrundmusik aus einer angegebenen WAV-Datei.
     * <p>
     * Die Musik wird geladen und in einer Endlosschleife abgespielt (Dauerschleife).
     * Es wird empfohlen, eine unkomprimierte WAV-Datei zu verwenden (z. B. PCM).
     * </p>
     *
     * @param dateipfad Der Pfad zur Musikdatei im WAV-Format.
     *                  Dies kann ein relativer Pfad (z. B. "musik.wav")
     *                  oder ein absoluter Pfad (z. B. "C:\\Musik\\titel.wav") sein.
     *
     * @throws IllegalArgumentException wenn die Datei nicht existiert oder
     *         ein Problem beim Abspielen auftritt.
     */
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
