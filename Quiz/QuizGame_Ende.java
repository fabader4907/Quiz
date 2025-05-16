package Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuizGame_Ende extends Basis {

    private List<Frage> fragenListe;
    private int aktuelleFrage = 0;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private int countdownValue = 5;
    private JLabel titelLabel;
    private int totalPoints; // Variable to store total points
    private JLabel pointsLabel; // Label to display total points

    public QuizGame_Ende(int totalPoints) {
        super("Lyric!", new String[]{"A", "B", "C", "D"});
        this.totalPoints = totalPoints;

        try {
            fragenListe = FragenLader.ladeFragen("Quiz/lyrics.txt");
            // Shuffle the list and pick 5 random questions
            Collections.shuffle(fragenListe);
            fragenListe = fragenListe.stream().limit(5).collect(Collectors.toList());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Fragen: " + e.getMessage());
            return;
        }

        // Set a dark background for the main frame
        getContentPane().setBackground(new Color(30, 30, 30));

        // Titel
        titelLabel = new JLabel("Beweise dein können! Vervollständige die Lyrics", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titelLabel.setForeground(new Color(200, 200, 200)); // Light gray text
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(titelLabel, gbc);

        // Total Points Label
        pointsLabel = new JLabel("Punkte: " + totalPoints, SwingConstants.LEFT);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pointsLabel.setForeground(new Color(255, 105, 180)); // Pink color for points
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 0);
        add(pointsLabel, gbc);

        // Countdown
        countdownLabel = new JLabel("" + countdownValue, SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 60));
        countdownLabel.setForeground(new Color(173, 216, 230)); // Light blue color
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 20, 10);
        add(countdownLabel, gbc);

        frageLabel.setVisible(false);
        frageLabel.setForeground(new Color(200, 200, 200)); // Light gray text for question
        for (JButton button : antwortButtons) {
            button.setVisible(false);
            button.setBackground(new Color(70, 70, 70)); // Dark gray background for buttons
            button.setForeground(new Color(255, 255, 255)); // White text for buttons
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        countdownTimer = new Timer(1000, e -> {
            countdownValue--;
            if (countdownValue > 0) {
                countdownLabel.setText("" + countdownValue);
            } else if (countdownValue == 0) {
                countdownLabel.setText("Los!");
                countdownTimer.stop();
                zeigeAntworten();
                new Timer(1000, e1 -> {});
            }
        });
        countdownTimer.setInitialDelay(1000);
        countdownTimer.start();
    }

    private void zeigeAntworten() {
        if (aktuelleFrage >= fragenListe.size()) {
            JOptionPane.showMessageDialog(this, "Du hast es geschafft!", "Fertig", JOptionPane.INFORMATION_MESSAGE);
            new Leaderboard();
            frageLabel.setVisible(false);
            button.setVisible(false);
            return;
        }

        countdownLabel.setText("");
        frageLabel.setVisible(true);
        titelLabel.setVisible(false);

        Frage frage = fragenListe.get(aktuelleFrage);
        frageLabel.setText(frage.frageText);

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
                    totalPoints += points; // Increment total points
                    pointsLabel.setText("Punkte: " + totalPoints); // Update points label
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

                // Add a delay before showing the points dialog
                Timer delayTimer = new Timer(3000, ev -> {
                    // Punkte zeigen und nächste Frage
                    JOptionPane.showMessageDialog(
                            QuizGame_Ende.this,
                            "Du hast " + points + " Punkte erreicht!",
                            "Ergebnis",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    aktuelleFrage++;
                    for (int btnNr = 0; btnNr < antwortButtons.length; btnNr++) {
                        antwortButtons[btnNr].setEnabled(true);
                        antwortButtons[btnNr].setBorder(null);
                    }
                    zeigeAntworten();
                });
                delayTimer.setRepeats(false); // Ensure the timer only fires once
                delayTimer.start();
            });
        }
    }
}
