import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frage1 extends Basis {

    private JLabel countdownLabel;
    private Timer countdownTimer;
    private int countdownValue = 3;

    public Frage1() {
        super("Erkenne das Intro!",
                new String[]{"A", "B", "C", "D"}
                // "bild.jpg",
                // "sound.wav"
        );

        countdownLabel = new JLabel("" + countdownValue, SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(countdownLabel, gbc);

        for (JButton button : antwortButtons) {
            button.setVisible(false);
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
        countdownLabel.setText("");

        for (JButton button : antwortButtons) {
            button.setVisible(true);

            final JButton currentButton = button;

            currentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String ausgewaehlt = currentButton.getText();
                    boolean istRichtig = ausgewaehlt.equals("D");

                    if (istRichtig) {
                        currentButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                    } else {
                        currentButton.setBorder(BorderFactory.createLineBorder(Color.RED, 4));

                        // Richtige Antwort markieren
                        for (JButton b : antwortButtons) {
                            if (b.getText().equals("D")) {
                                b.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                            }
                        }
                    }

                    // Buttons deaktivieren
                    for (JButton b : antwortButtons) {
                        b.setEnabled(false);
                    }
                }
            });
        }
    }
}
