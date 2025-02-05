package ro.iacobai.titleManager;

import org.bukkit.permissions.Permission;

import java.util.List;

public class Title {
    private final String name;
    private final Permission permission;
    private final List<String> description;
    private final String title;

    public Title(String name, Permission permission, List<String> description, String title) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
