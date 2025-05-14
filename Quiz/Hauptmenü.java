package Quiz;

import javax.swing.*;
import java.awt.*;

public class Hauptmenü extends JFrame {

    public Hauptmenü(String benutzername) {
        setTitle("Quiz TUFF");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bgColor = new Color(240, 248, 255);
        Color buttonColor = new Color(0, 123, 255);
        Color sidebarColor = new Color(220, 235, 250);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // Top Left: TUFF Logo
        JLabel tuffLabel = new JLabel("TUFF");
        tuffLabel.setFont(new Font("Segoe UI", Font.ITALIC, 28));
        tuffLabel.setForeground(new Color(60, 60, 60));
        JPanel tuffPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tuffPanel.setBackground(bgColor);
        tuffPanel.add(tuffLabel);
        mainPanel.add(tuffPanel, BorderLayout.NORTH);

        // Sidebar left (Leaderboard, Spieleranzahl, Abmelden)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(sidebarColor);
        leftPanel.setPreferredSize(new Dimension(250, getHeight()));

        JButton leaderboardBtn = createSidebarButton("🏆 Leaderboard", buttonFont);
        JButton playerCountBtn = createSidebarButton("👥 Spieleranzahl", buttonFont);
        JButton logoutBtn = createSidebarButton("🚪 Abmelden", new Font("Segoe UI", Font.PLAIN, 14));
        logoutBtn.setBackground(new Color(200, 0, 0));

        leaderboardBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Leaderboard folgt noch!");
        });

        playerCountBtn.addActionListener(e -> {
            int count = UserManager.ladeBenutzeranzahl();
            JOptionPane.showMessageDialog(this, "Registrierte Spieler: " + count);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new ModernQuizLogin();
        });

        leftPanel.add(Box.createVerticalStrut(100));
        leftPanel.add(leaderboardBtn);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(playerCountBtn);
        leftPanel.add(Box.createVerticalStrut(200));
        leftPanel.add(logoutBtn);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Center Area mit Quiz-Start-Button & Musikbild
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();

        JButton startBtn = new JButton("🎯 Quiz starten");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        startBtn.setBackground(buttonColor);
        startBtn.setForeground(Color.WHITE);
        startBtn.setPreferredSize(new Dimension(300, 70));
        startBtn.setFocusPainted(false);

        startBtn.addActionListener(e -> {
            dispose();
            new QuizGame_Start();
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(startBtn, gbc);

        // Musik-Bild links neben dem Button
        try {
            ImageIcon musikIcon = new ImageIcon("Quiz/img/musik.jpg"); // dein Bild hier einfügen
            Image scaledImage = musikIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel musikLabel = new JLabel(new ImageIcon(scaledImage));
            gbc.gridx = 0;
            centerPanel.add(musikLabel, gbc);
        } catch (Exception ex) {
            System.out.println("Bild konnte nicht geladen werden.");
        }

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JButton createSidebarButton(String text, Font font) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setFont(font);
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
}
