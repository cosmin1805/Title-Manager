package ro.iacobai.titleManager.models;

import ro.iacobai.titleManager.TitleManager;

import java.util.HashSet;
import java.util.Set;

public class TitleHandler {
    // Singleton instance of the class
    private static TitleHandler instance;

    // Set to store all registered titles
    private final Set<Title> titles = new HashSet<>();

    // Private constructor to prevent instantiation
    private TitleHandler() {
    }

    // Method to get the singleton instance of the class
    public static TitleHandler getInstance() {
        if (instance == null) {
            instance = new TitleHandler();
        }
        return instance;
    }

    // Method to register a new title
    public void registerTitle(Title title) {
        titles.add(title);
    }

    // Method to retrieve all registered titles
    public Set<Title> getTitles() {
        return titles;
    }

    // Method to clear all the registered titles, so we can reload them
    public void clearTitles() {
        // Before clearing the titles, we should also remove the permissions from the server
        for (Title title : titles) {
            TitleManager.getInstance().getServer().getPluginManager().removePermission(title.getPermission());
        }

        titles.clear();
    }
}