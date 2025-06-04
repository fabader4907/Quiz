package Quiz;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Die Klasse {@code QuizGame_Mitte} repräsentiert die mittlere Runde des Quiz-Spiels.
 * In dieser Runde werden Fragen aus der Datei "lieder.txt" geladen.
 * Der Benutzer sammelt Punkte durch richtige Antworten.
 */
public class QuizGame_Mitte extends Basis {

    /** Liste der geladenen Fragen */
    private List<Frage> fragenListe;

    /** Index der aktuell bearbeiteten Frage */
    private int aktuelleFrage = 0;

    /** Label zur Anzeige des Countdowns */
    private JLabel countdownLabel;

    /** Timer zur Steuerung des Countdowns */
    private Timer countdownTimer;

    /** Startwert für den Countdown */
    private int countdownValue = 5;

    /** Label für den Titel der Fragerunde */
    private JLabel titelLabel;

    /** Gesamtpunktestand des Spielers */
    private int totalPoints;

    /** Anzeigeelement für den Punktestand */
    private JLabel pointsLabel;

    /** Benutzername des Spielers */
    private String benutzername;

    /** Pfad zum gewählten Avatar */
    private String avatarPfad;

    /** Musik */
    private Clip clip;

    /**
     * Konstruktor zum Starten der mittleren Quiz-Runde mit übergebenem Punktestand.
     *
     * @param benutzername Der Name des Spielers
     * @param totalPoints  Bisher erreichte Punktzahl
     * @param avatarPfad   Pfad zum Avatarbild
     */
    public QuizGame_Mitte(String benutzername, int totalPoints, String avatarPfad) {
        super("Lyric!", new String[]{"A", "B", "C", "D"});
        this.benutzername = benutzername;
        this.totalPoints = totalPoints;
        this.avatarPfad = avatarPfad;
        starteMusik("Quiz/music/come-on-boy.wav");

        // UI-Einstellungen (Dialog-Stil)
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.background", new Color(40, 40, 40));
        UIManager.put("Panel.background", new Color(40, 40, 40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        // Fragen aus Datei laden
        try {
            fragenListe = FragenLader.ladeFragen("Quiz/lieder.txt");
            Collections.shuffle(fragenListe);
            fragenListe = fragenListe.stream().limit(5).collect(Collectors.toList());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Fragen: " + e.getMessage());
            return;
        }

        getContentPane().setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();

        // Punkte-Anzeige
        pointsLabel = new JLabel("Punkte: " + totalPoints, SwingConstants.LEFT);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pointsLabel.setForeground(new Color(255, 105, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 0);
        add(pointsLabel, gbc);

        // Abbrechen-Button mit Dialog
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
                new Hauptmenu(benutzername, avatarPfad);
                this.dispose();
            }
        });

        GridBagConstraints gbcAbbruch = new GridBagConstraints();
        gbcAbbruch.gridx = 1;
        gbcAbbruch.gridy = 0;
        gbcAbbruch.anchor = GridBagConstraints.NORTHEAST;
        gbcAbbruch.insets = new Insets(10, 0, 0, 10);
        add(btnAbbrechen, gbcAbbruch);

        // Titel
        titelLabel = new JLabel("Jetzt wird es interessanter. In welchem Song heißt es...", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titelLabel.setForeground(new Color(200, 200, 200));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(titelLabel, gbc);

        // Countdown-Anzeige
        countdownLabel = new JLabel("" + countdownValue, SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 60));
        countdownLabel.setForeground(new Color(173, 216, 230));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 20, 10);
        add(countdownLabel, gbc);

        // Frage und Antwort-Buttons vorbereiten
        frageLabel.setVisible(false);
        frageLabel.setForeground(new Color(200, 200, 200));
        frageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        for (JButton button : antwortButtons) {
            button.setVisible(false);
            button.setBackground(new Color(70, 70, 70));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
     * Zeigt die nächste Frage und mögliche Antworten an.
     * Wenn alle Fragen beantwortet wurden, wird zur Endrunde weitergeleitet.
     */
    private void zeigeAntworten() {
        if (aktuelleFrage >= fragenListe.size()) {
            JOptionPane.showMessageDialog(this, "Jetzt sind wir dem Ende ganz nah!", "Weiter", JOptionPane.INFORMATION_MESSAGE);
            new QuizGame_Ende(benutzername, totalPoints, avatarPfad);
            this.dispose();
            return;
        }

        countdownLabel.setText("");
        frageLabel.setVisible(true);
        titelLabel.setVisible(false);

        Frage frage = fragenListe.get(aktuelleFrage);
        frageLabel.setText(frage.frageText);

        // Antwort-Buttons mit Text befüllen
        for (int i = 0; i < antwortButtons.length; i++) {
            JButton button = antwortButtons[i];
            button.setVisible(true);
            button.setText(frage.antworten[i]);
        }

        PointHandler ph = new PointHandler();
        ph.start();

        // ActionListener für Antwortauswahl
        for (int i = 0; i < antwortButtons.length; i++) {
            JButton currentButton = antwortButtons[i];
            String antwortBuchstabe = switch (i) {
                case 0 -> "A";
                case 1 -> "B";
                case 2 -> "C";
                case 3 -> "D";
                default -> "";
            };

            // Vorherige ActionListener entfernen
            for (ActionListener al : currentButton.getActionListeners()) {
                currentButton.removeActionListener(al);
            }

            // Aktion bei Antwortwahl
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

                // Nach 3 Sekunden nächste Frage anzeigen
                Timer delayTimer = new Timer(3000, ev -> {
                    JOptionPane.showMessageDialog(
                            QuizGame_Mitte.this,
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


