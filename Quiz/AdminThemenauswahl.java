package Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class AdminThemenauswahl extends JFrame {

    private final Color backgroundColorStart = new Color(30, 30, 50);
    private final Color backgroundColorEnd = new Color(50, 50, 80);
    private final Color buttonColor = new Color(100, 149, 237);
    private final Color buttonTextColor = new Color(255, 255, 255);
    private final Color deleteColor = new Color(220, 70, 70);

    public AdminThemenauswahl() {
        setTitle("Themenbereiche – Admin");
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

        JLabel titleLabel = new JLabel("Admin – Themenauswahl", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JPanel buttonGroupPanel = new JPanel(new GridLayout(1, 2, 80, 0));
        buttonGroupPanel.setOpaque(false);
        buttonGroupPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));

        // Linke Seite: Hinzufügen
        JPanel addPanel = createThemenbereichPanel("Fragen hinzufügen", buttonColor, true);
        // Rechte Seite: Löschen
        JPanel deletePanel = createThemenbereichPanel("Fragen löschen", deleteColor, false);

        buttonGroupPanel.add(addPanel);
        buttonGroupPanel.add(deletePanel);

        // Abmelden-Button hinzufügen
        JButton logoutButton = new JButton("Abmelden");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 20));
        logoutButton.setBackground(new Color(200, 80, 80));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(200, 50));
        logoutButton.addActionListener((ActionEvent e) -> {
            dispose();
            new ModernQuizLogin();
        });

        JPanel logoutPanel = new JPanel();
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        logoutPanel.add(logoutButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonGroupPanel, BorderLayout.CENTER);
        mainPanel.add(logoutPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createThemenbereichPanel(String titel, Color btnColor, boolean istHinzufuegen) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.CENTER;

        JLabel bereichLabel = new JLabel(titel, SwingConstants.CENTER);
        bereichLabel.setFont(new Font("Arial", Font.BOLD, 28));
        bereichLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(bereichLabel, gbc);

        String[] themen = {"Themenbereich 1", "Themenbereich 2", "Themenbereich 3"};
        String[] dateien = {"Quiz/allgemein.txt", "Quiz/lieder.txt", "Quiz/lyrics.txt"};

        for (int i = 0; i < themen.length; i++) {
            gbc.gridy = i + 1;
            JButton btn = createStyledButton(themen[i], btnColor);
            int index = i;
            btn.addActionListener(e -> {
                dispose();
                if (istHinzufuegen) {
                    new FragenerstellenStart(dateien[index]);
                } else {
                    new FragenLoeschenStart(dateien[index]); // Diese Klasse musst du noch erstellen
                }
            });
            panel.add(btn, gbc);
        }

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
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
        btn.setPreferredSize(new Dimension(400, 100));

        return btn;
    }
}
