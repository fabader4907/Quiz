import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frage1 extends Basis {

    private JLabel countdownLabel;
    private Timer countdownTimer;
    private int countdownValue = 3;
    private JLabel titelLabel;


    public Frage1() {
        super("Lyric!",
                new String[]{"A", "B", "C", "D"}
                // "bild.jpg",
                // "sound.wav"
        );

        // Statisches Label "Erkenne die Lyrics"
        titelLabel = new JLabel("Erkenne die Lyrics", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        add(titelLabel, gbc);


        // Dynamisches Countdown-Label
        countdownLabel = new JLabel("" + countdownValue, SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 60));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 20, 10);
        add(countdownLabel, gbc);

        for (JButton button : antwortButtons) {
            button.setVisible(false);
            frageLabel.setVisible(false);
        }

        countdownTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownValue--;
                if (countdownValue > 0) {
                    countdownLabel.setText("" + countdownValue);
                } else if (countdownValue == 0) {
                    countdownLabel.setText("Los!");
                    countdownTimer.stop();
                    new Timer(500, ev -> zeigeAntworten()).start();
                }
            }
        });
        countdownTimer.setInitialDelay(1500);
        countdownTimer.start();
    }

    private void zeigeAntworten() {
        countdownLabel.setText("");  // Countdown ausblenden

        frageLabel.setVisible(true);
        for (JButton button : antwortButtons) {
            button.setVisible(true);
            titelLabel.setVisible(false);
        }

        PointHandler ph = new PointHandler();
        ph.start();

        for (JButton currentButton : antwortButtons) {
            for (ActionListener al : currentButton.getActionListeners()) {
                currentButton.removeActionListener(al);
            }

            currentButton.addActionListener(e -> {
                for (JButton b : antwortButtons) {
                    b.setEnabled(false);
                }

                int points = ph.getPoints();

                String ausgewaehlt = currentButton.getText();
                boolean istRichtig = ausgewaehlt.equals("D");

                if (istRichtig) {
                    currentButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                } else {
                    currentButton.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                    for (JButton b : antwortButtons) {
                        if (b.getText().equals("D")) {
                            b.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                        }
                    }
                }

                new Timer(3000, ev -> {
                    ((Timer) ev.getSource()).stop();

                    JOptionPane.showMessageDialog(
                            Frage1.this,
                            "Du hast " + points + " Punkte erreicht!",
                            "Ergebnis",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    //frageWechseln();
                }).start();
            });
        }
    }

    private void frageWechseln() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
        new Frage2(); // Nächste Frage starten
    }
}