package com.snipers.wheel.model;

public class Prize {
    private String imageUrl;
    private boolean winnable;
    private String name;

    // Default Constructor
    public Prize() {}

    // Constructor with all fields
    public Prize(String imageUrl, boolean winnable, String name) {
        this.imageUrl = imageUrl;
        this.winnable = winnable;
        this.name = name;
    }

    // Getter and Setter for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter and Setter for winnable
    public boolean isWinnable() {
        return winnable;
    }

    public void setWinnable(Object winnable) {
        if (winnable instanceof Boolean) {
            this.winnable = (Boolean) winnable;
        } else if (winnable instanceof String) {
            this.winnable = Boolean.parseBoolean((String) winnable);
        } else {
            this.winnable = false; // Default or handle as per your need
        }
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
