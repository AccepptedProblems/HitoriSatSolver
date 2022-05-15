package com.example.view;

public class Game {
    private String displayName;
    private String fileName;

    public Game(String filename, int index) {
        this.fileName = filename;
        this.displayName = String.format("Game %d", index);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
