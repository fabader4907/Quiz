package Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Hauptmenu extends JFrame {

    private String aktuellerAvatar = "Quiz/avatars/avatar1.jpg";
    private final Color backgroundColorStart = new Color(30, 30, 50);
    private final Color backgroundColorEnd = new Color(50, 50, 80);
    private final Color buttonColorPrimary = new Color(100, 149, 237);
    private final Color buttonColorSecondary = new Color(128, 128, 180);
    private final Color buttonTextColor = new Color(255, 255, 255);
    private final Color logoutColor = new Color(220, 70, 70);
    private final Color textColor = new Color(230, 230, 230);
    private final Color usernameColor = new Color(255, 215, 0);

    public Hauptmenu(String benutzername) {
        init(benutzername);
    }

    public Hauptmenu(String benutzername, String avatarPfad) {
        this.aktuellerAvatar = avatarPfad;
        init(benutzername);
    }

    private void init(String benutzername) {
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
        mainPanel.add(createCenterPanel(benutzername), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTopPanel(String benutzername) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel welcomeLabel = new JLabel("Willkommen bei unserem Musik-Quiz", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 42));
        welcomeLabel.setForeground(textColor);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(benutzername, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 48));
        nameLabel.setForeground(usernameColor);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton avatarButton = new JButton();
        avatarButton.setPreferredSize(new Dimension(120, 120));
        avatarButton.setMaximumSize(new Dimension(120, 120));
        avatarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        avatarButton.setContentAreaFilled(false);
        avatarButton.setFocusPainted(false);
        avatarButton.setOpaque(false);

        ImageIcon icon = new ImageIcon(aktuellerAvatar);
        Image image = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        avatarButton.setIcon(new ImageIcon(image));

        avatarButton.addActionListener(e -> {
            dispose();
            new AvatarAuswahl(benutzername);
        });

        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(avatarButton);

        return panel;
    }

    private JPanel createCenterPanel(String benutzername) {
        JPanel centerPanel = new JPanel(new GridBagLayout()) {
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
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        JButton quizBtn = createStyledButton("Rock das Quiz – Los geht's!", buttonColorPrimary, new Dimension(800, 140));
        centerPanel.add(quizBtn, gbc);

        gbc.gridy = 1;
        JPanel buttonRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonRow2.setOpaque(false);
        JButton leaderboardBtn = createStyledButton("Leaderboard - Die Besten der Besten", buttonColorSecondary, new Dimension(550, 120));
        JButton playerCountBtn = createStyledButton("Spieleranzahl", buttonColorSecondary, new Dimension(550, 120));
        buttonRow2.add(leaderboardBtn);
        buttonRow2.add(playerCountBtn);
        centerPanel.add(buttonRow2, gbc);

        gbc.gridy = 2;
        JButton logoutBtn = createStyledButton("Abmelden - und Tschüss", logoutColor, new Dimension(500, 80));
        centerPanel.add(logoutBtn, gbc);

        quizBtn.addActionListener(e -> {
            dispose();
            new QuizGame_Start(benutzername, 0);
        });

        leaderboardBtn.addActionListener(e -> {
            dispose();
            new Leaderboard(benutzername);
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
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fill(new RoundRectangle2D.Double(5, 5, getWidth() - 1, getHeight() - 1, 20, 20));
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