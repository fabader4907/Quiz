package Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Die Klasse {@code QuizGame_Start} stellt den Startbildschirm eines Quiz-Spiels dar.
 * Sie zeigt Fragen an, verwaltet die Punkte und steuert den Spielfortschritt.
 */
public class QuizGame_Start extends Basis {

    /** Benutzername des Spielers */
    private String benutzername;

    /** Pfad zum gewählten Avatarbild */
    private String avatarPfad;

    /** Liste der geladenen Fragen */
    private List<Frage> fragenListe;

    /** Index der aktuell angezeigten Frage */
    private int aktuelleFrage = 0;

    /** Label zur Anzeige des Countdowns vor Start */
    private JLabel countdownLabel;

    /** Timer zur Steuerung des Countdowns */
    private Timer countdownTimer;

    /** Aktueller Countdown-Wert */
    private int countdownValue = 5;

    /** Überschrift des Spiels */
    private JLabel titelLabel;

    /** Aktuell erreichte Gesamtpunktzahl */
    private int totalPoints = 0;

    /** Label zur Anzeige der aktuellen Punkte */
    private JLabel pointsLabel;

    /**
     * Konstruktor: Initialisiert das Quiz-Spiel.
     *
     * @param benutzername Name des Spielers
     * @param i nicht verwendet (reserviert für zukünftige Erweiterungen)
     * @param avatarPfad Pfad zum Avatarbild des Spielers
     */
    public QuizGame_Start(String benutzername, int i, String avatarPfad) {
        super("Lyric!", new String[]{"A", "B", "C", "D"});
        this.benutzername = benutzername;
        this.avatarPfad = avatarPfad;

        // UI-Styling für Dialoge
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.background", new Color(40, 40, 40));
        UIManager.put("Panel.background", new Color(40, 40, 40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        try {
            // Fragen aus Datei laden und zufällig mischen
            fragenListe = FragenLader.ladeFragen("Quiz/allgemein.txt");
            Collections.shuffle(fragenListe);
            fragenListe = fragenListe.stream().limit(10).collect(Collectors.toList());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Fragen: " + e.getMessage());
            return;
        }

        // Benutzeroberfläche gestalten
        getContentPane().setBackground(new Color(30, 30, 30));

        // Titel
        titelLabel = new JLabel("Zeige dein Musikwissen", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titelLabel.setForeground(new Color(200, 200, 200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(titelLabel, gbc);

        // Punktestand-Anzeige
        pointsLabel = new JLabel("Punkte: " + totalPoints, SwingConstants.LEFT);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pointsLabel.setForeground(new Color(255, 105, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 0);
        add(pointsLabel, gbc);

        // Abbrechen-Button
        JButton btnAbbrechen = new JButton("Quiz abbrechen");
        btnAbbrechen.setBackground(new Color(200, 0, 0));
        btnAbbrechen.setForeground(Color.WHITE);
        btnAbbrechen.setFont(new Font("Arial", Font.BOLD, 12));
        btnAbbrechen.setFocusPainted(false);
        btnAbbrechen.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Aktion für Abbrechen-Button
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

        // Position des Abbrechen-Buttons
        GridBagConstraints gbcAbbruch = new GridBagConstraints();
        gbcAbbruch.gridx = 1;
        gbcAbbruch.gridy = 0;
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

        // Einstellungen für Frage und Antwortbuttons
        frageLabel.setVisible(false);
        frageLabel.setForeground(new Color(200, 200, 200));
        frageLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        for (JButton button : antwortButtons) {
            button.setVisible(false);
            button.setBackground(new Color(70, 70, 70));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        // Countdown-Timer starten
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
     * Zeigt die aktuelle Frage und deren Antwortmöglichkeiten an.
     * Prüft, ob das Spiel fortgesetzt oder beendet werden soll,
     * und aktualisiert die Benutzeroberfläche entsprechend.
     */
    private void zeigeAntworten() {
        if (aktuelleFrage >= fragenListe.size()) {
            JOptionPane.showMessageDialog(this, "Wir kommen dem Ende näher!", "Weiter", JOptionPane.INFORMATION_MESSAGE);
            new QuizGame_Mitte(benutzername, totalPoints, avatarPfad);
            this.dispose();
            return;
        }

        countdownLabel.setText("");
        frageLabel.setVisible(true);
        titelLabel.setVisible(false);

        Frage frage = fragenListe.get(aktuelleFrage);
        frageLabel.setText(frage.frageText);

        // Antwortmöglichkeiten anzeigen
        for (int i = 0; i < antwortButtons.length; i++) {
            JButton button = antwortButtons[i];
            button.setVisible(true);
            button.setText(frage.antworten[i]);
        }

        PointHandler ph = new PointHandler();
        ph.start();

        // ActionListener für Antwortbuttons
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

            // Neuer ActionListener für Antwortauswahl
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

                // Verzögerung bis zur nächsten Frage
                Timer delayTimer = new Timer(3000, ev -> {
                    JOptionPane.showMessageDialog(
                            QuizGame_Start.this,
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
}
