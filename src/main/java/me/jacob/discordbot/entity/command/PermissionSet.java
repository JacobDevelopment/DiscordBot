package me.jacob.discordbot.entity.command;

import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

public class PermissionSet {

    private final EnumSet<Permission> selfPerms;
    private final EnumSet<Permission> userPerms;

    private PermissionSet(EnumSet<Permission> selfPerms, EnumSet<Permission> userPerms) {
        this.selfPerms = selfPerms;
        this.userPerms = userPerms;
    }

    public static PermissionSet of(EnumSet<Permission> selfPerms, EnumSet<Permission> userPerms) {
        return new PermissionSet(selfPerms, userPerms);
    }

    public static PermissionSet ofDefault() {
        return new PermissionSet(EnumSet.of(Permission.MESSAGE_WRITE), null);
    }

    public EnumSet<Permission> getSelfPerms() {
        return selfPerms;
    }

    public EnumSet<Permission> getUserPerms() {
        return userPerms;
    }
}
