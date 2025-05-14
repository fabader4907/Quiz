package Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Hauptmenu extends JFrame {

    // Farben
    private final Color backgroundColor = new Color(30, 30, 30);              // Dunkler Hintergrund
    private final Color buttonColor = new Color(100, 149, 237);               // Cornflower Blue
    private final Color buttonColorSecondary = new Color(70, 130, 180);       // Steel Blue
    private final Color buttonTextColor = new Color(255, 255, 255);           // Weiße Schrift
    private final Color logoutColor = new Color(220, 70, 70);                 // Rot
    private final Color textColor = new Color(230, 230, 230);                 // Helle Schrift

    public Hauptmenu(String benutzername) {
        setTitle("Hauptmenü");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);

        mainPanel.add(createTopPanel(benutzername), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTopPanel(String benutzername) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel nameLabel = new JLabel("Hallo, " + benutzername);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 42));
        nameLabel.setForeground(textColor);

        panel.add(nameLabel);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Zeile 1: Quiz & Leaderboard
        JPanel buttonRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonRow1.setBackground(backgroundColor);
        JButton quizBtn = createStyledButton("Quiz starten", buttonColor, new Dimension(450, 100));
        JButton leaderboardBtn = createStyledButton("Leaderboard", buttonColor, new Dimension(450, 100));
        buttonRow1.add(quizBtn);
        buttonRow1.add(leaderboardBtn);
        gbc.gridy = 0;
        centerPanel.add(buttonRow1, gbc);

        // Zeile 2: Spieleranzahl
        gbc.gridy++;
        JButton playerCountBtn = createStyledButton("Spieleranzahl", buttonColorSecondary, new Dimension(300, 70));
        centerPanel.add(playerCountBtn, gbc);

        // Zeile 3: Abmelden – kleiner und rot
        gbc.gridy++;
        JButton logoutBtn = createStyledButton("Abmelden", logoutColor, new Dimension(300, 70));
        centerPanel.add(logoutBtn, gbc);

        // Aktionen
        quizBtn.addActionListener(e -> {
            dispose();
            new QuizGame_Start();
        });

        leaderboardBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Leaderboard kommt noch.");
        });

        playerCountBtn.addActionListener(e -> {
            int count = UserManager.ladeBenutzeranzahl();
            JOptionPane.showMessageDialog(this, "Spieleranzahl: " + count);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new ModernQuizLogin();
        });

        return centerPanel;
    }

    private JButton createStyledButton(String text, Color bgColor, Dimension size) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Schatteneffekt
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fill(new RoundRectangle2D.Double(5, 5, getWidth() - 1, getHeight() - 1, 20, 20));

                // Button-Hintergrund
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setForeground(buttonTextColor);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        btn.setPreferredSize(size);

        return btn;
    }
}
