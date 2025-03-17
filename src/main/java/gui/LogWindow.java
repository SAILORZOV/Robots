package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, Savable {
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private static final String SAVE_FILE_PATH = System.getProperty("user.home") + "/RobotsSaveData/logwindowsave.txt";

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        setLocation(10, 10);
        setMinimumSize(new Dimension(200, 500));
        setSize(300, 800);
        pack();
        loadState();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void saveState() {
        try {
            if (isClosed()) {
                return;
            }

            Path dir = Paths.get(System.getProperty("user.home"), "RobotsSaveData");
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }

            int width, height, x, y;
            boolean wasMaximized = false;

            if (isMaximum()) {
                wasMaximized = true;
                setMaximum(false);
            }

            width = getWidth();
            height = getHeight();
            x = getX();
            y = getY();

            if (wasMaximized) {
                setMaximum(true);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE_PATH))) {
                writer.write(width + "\n");
                writer.write(height + "\n");
                writer.write(x + "\n");
                writer.write(y + "\n");
                writer.write((isIcon() ? "0" : (wasMaximized ? "2" : "1")) + "\n");
            }
        } catch (IOException | PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadState() {
        try {
            if (isClosed()) {
                return;
            }

            File file = new File(SAVE_FILE_PATH);
            if (!file.exists()) {
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                int width = Integer.parseInt(reader.readLine());
                int height = Integer.parseInt(reader.readLine());
                int x = Integer.parseInt(reader.readLine());
                int y = Integer.parseInt(reader.readLine());
                int state = Integer.parseInt(reader.readLine());

                setSize(new Dimension(width, height));
                setLocation(x, y);

                if (state == 0) {
                    setIcon(true);
                } else {
                    setIcon(false);
                }

                if (state == 2) {
                    setMaximum(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
