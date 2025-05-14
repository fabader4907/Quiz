package Quiz;

import javax.swing.*;
import java.awt.*;

public class Hauptmenü extends JFrame {

    private final Color bgColor = new Color(245, 250, 255);
    private final Color buttonIndigo = new Color(99, 102, 241);
    private final Color buttonGreen = new Color(16, 185, 129);
    private final Color buttonYellow = new Color(245, 158, 11);
    private final Color buttonRed = new Color(239, 68, 68);

    public Hauptmenü(String benutzername) {
        setTitle("Hauptmenü");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        mainPanel.add(createTopPanel(benutzername), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTopPanel(String benutzername) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(bgColor);

        JLabel nameLabel = new JLabel("Hallo, " + benutzername);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));

        ImageIcon avatarIcon = new ImageIcon("Quiz/img/avatar.jpg");
        Image avatarImage = avatarIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel avatarLabel = new JLabel(new ImageIcon(avatarImage));

        panel.add(avatarLabel);
        panel.add(Box.createHorizontalStrut(14));
        panel.add(nameLabel);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Zeile 1: Quiz & Leaderboard
        JPanel buttonRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        buttonRow1.setBackground(bgColor);
        JButton quizBtn = createStyledButton("🎯 Quiz starten", buttonIndigo);
        JButton leaderboardBtn = createStyledButton("🏆 Leaderboard anzeigen", buttonGreen);
        buttonRow1.add(quizBtn);
        buttonRow1.add(leaderboardBtn);
        gbc.gridy = 0;
        centerPanel.add(buttonRow1, gbc);

        // Zeile 2: Spieleranzahl
        gbc.gridy++;
        JButton playerCountBtn = createStyledButton("👥 Spieleranzahl anzeigen", buttonYellow);
        centerPanel.add(playerCountBtn, gbc);

        // Zeile 3: Abmelden – zentriert und separat
        gbc.gridy++;
        JButton logoutBtn = createStyledButton("🚪 Abmelden", buttonRed);
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        logoutBtn.setPreferredSize(new Dimension(300, 60));
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

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        btn.setPreferredSize(new Dimension(300, 70));
        return btn;
    }
}
