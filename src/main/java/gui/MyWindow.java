package gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class MyWindow extends JInternalFrame implements Savable {
    private static final String SAVE_FILE_PATH = System.getProperty("user.home") + "/RobotsSaveData/save_data.json";
    private final String windowKey;

    protected MyWindow (String title, String windowKey) {
        super(title, true, true, true, true);
        this.windowKey = windowKey;
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

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode root = objectMapper.createObjectNode();
            File file = new File(SAVE_FILE_PATH);
            if (file.exists()) {
                root = (ObjectNode) objectMapper.readTree(file);
            }

            ObjectNode windowState = objectMapper.createObjectNode();
            windowState.put("width", width);
            windowState.put("height", height);
            windowState.put("x", x);
            windowState.put("y", y);
            windowState.put("state", isIcon() ? 0 : (wasMaximized ? 2 : 1));

            root.set(windowKey, windowState);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
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

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode root = (ObjectNode) objectMapper.readTree(file);

            ObjectNode windowState = (ObjectNode) root.get(windowKey);
            if (windowState == null) {
                return;
            }

            int width = windowState.get("width").asInt();
            int height = windowState.get("height").asInt();
            int x = windowState.get("x").asInt();
            int y = windowState.get("y").asInt();
            int state = windowState.get("state").asInt();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}