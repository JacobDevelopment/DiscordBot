package me.jacob.discordbot.entity.command;


import me.jacob.discordbot.entity.command.type.Category;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand {

    private final String invoke;
    private final String description;
    private Category category;
    private PermissionSet permissionSet;
    private String longDescription;
    private List<String> aliases;
    private List<String> usages;
    private int cooldown;

    public AbstractCommand(String invoke, String description, Category category) {
        this.invoke = invoke;
        this.description = description;
        this.category = category;
        this.permissionSet = PermissionSet.ofDefault();
        this.longDescription = "";
        this.aliases = Collections.emptyList();
        this.usages = Collections.emptyList();
        this.cooldown = 0;
    }

    public AbstractCommand(String invoke, String description, Category category, PermissionSet permissionSet, String longDescription, List<String> aliases, List<String> usages, int cooldown) {
        this.invoke = invoke;
        this.description = description;
        this.category = category;
        this.permissionSet = permissionSet;
        this.longDescription = longDescription;
        this.aliases = aliases;
        this.usages = usages;
        this.cooldown = cooldown;
    }

    public abstract void runCommand(CommandExecutor executor, List<String> args);

    public String getInvoke() {
        return invoke;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PermissionSet getPermissionSet() {
        return permissionSet;
    }

    public void setPermissionSet(PermissionSet permissionSet) {
        this.permissionSet = permissionSet;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String LongDescription) {
        this.longDescription = LongDescription;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getUsages() {
        return usages;
    }

    public void setUsages(List<String> usages) {
        this.usages = usages;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String toString() {
        return getInvoke();
    }

}

