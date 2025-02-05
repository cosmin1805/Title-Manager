package ro.iacobai.titleManager;

import java.util.HashSet;
import java.util.Set;

public class TitleHandler {
    // the singleton instance of the class
    private static TitleHandler instance;

    // we create a set of titles
    private final Set<Title> titles = new HashSet<>();

    // we create a private constructor so the class can't be instantiated
    private TitleHandler() {
    }

    // we create a static method to get the instance of the class
    public static TitleHandler getInstance() {
        if (instance == null) {
            instance = new TitleHandler();
        }
        return instance;
    }

    // we create a method to register a title
    public void registerTitle(Title title) {
        titles.add(title);
    }

    // we create a method to get all the titles
    public Set<Title> getTitles() {
        return titles;
    }
}
