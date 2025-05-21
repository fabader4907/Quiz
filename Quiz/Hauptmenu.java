package Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Hauptmenu extends JFrame {

    // Farben
    private final Color backgroundColorStart = new Color(30, 30, 50);         // Dunkler Hintergrund Start
    private final Color backgroundColorEnd = new Color(50, 50, 80);           // Dunkler Hintergrund Ende
    private final Color buttonColorPrimary = new Color(100, 149, 237);        // Blau mit Grauanteil
    private final Color buttonColorSecondary = new Color(128, 128, 180);      // Grau mit Blauanteil
    private final Color buttonTextColor = new Color(255, 255, 255);           // Weiße Schrift
    private final Color logoutColor = new Color(220, 70, 70);                 // Rot
    private final Color textColor = new Color(230, 230, 230);                 // Helle Schrift
    private final Color usernameColor = new Color(255, 215, 0);               // Gold für Benutzername

    public Hauptmenu(String benutzername) {
        setTitle("Hauptmenü");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, backgroundColorStart, width, height, backgroundColorEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };

        mainPanel.add(createTopPanel(benutzername), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTopPanel(String benutzername) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 10)); // 2 Zeilen, 1 Spalte
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel welcomeLabel = new JLabel("Willkommen bei unserem Musik-Quiz", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 42));
        welcomeLabel.setForeground(textColor);

        JLabel nameLabel = new JLabel(benutzername, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48)); // Hervorgehobener Benutzername
        nameLabel.setForeground(usernameColor);

        panel.add(welcomeLabel);
        panel.add(nameLabel);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 50), width, height, new Color(50, 50, 80));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Zeile 1: Quiz starten
        gbc.gridy = 0;
        JButton quizBtn = createStyledButton("Rock das Quiz – Los geht's!", buttonColorPrimary, new Dimension(800, 140));
        centerPanel.add(quizBtn, gbc);

        // Zeile 2: Leaderboard & Spieleranzahl
        JPanel buttonRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonRow2.setOpaque(false);
        JButton leaderboardBtn = createStyledButton("Leaderboard - Die Besten der Besten", buttonColorSecondary, new Dimension(550, 120));
        JButton playerCountBtn = createStyledButton("Spieleranzahl", buttonColorSecondary, new Dimension(550, 120));
        buttonRow2.add(leaderboardBtn);
        buttonRow2.add(playerCountBtn);
        gbc.gridy = 1;
        centerPanel.add(buttonRow2, gbc);

        // Zeile 3: Abmelden – breiter und rot
        gbc.gridy = 2;
        JButton logoutBtn = createStyledButton("Abmelden - und Tschüss", logoutColor, new Dimension(500, 80));
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
            new Leaderboard();
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
