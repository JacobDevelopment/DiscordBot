package me.jacob.discordbot.entity.command.type;

public enum ErrorType {

    INVALID_ARGS("You did not provide valid command arguments."),
    CANT_INTERACT("**%s** cannot interact with **%s.**"),
    COULD_NOT_FIND("I could not find the following member by ID: **%s**"),
    INVALID_ID("The provided ID was invalid and could not be parsed: **%s.**"),
    GENERIC("**%s**"),
    MISSING_PERMISSION("**%s** is missing the following permission(s): **%s**");

    private final String content;

    ErrorType(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getContentFormatted(Object... objects) {
        return getContent().formatted(objects);
    }

    public String getName() {
        return name();
    }
}
