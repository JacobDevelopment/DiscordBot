package me.jacob.discordbot.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class StringUtil {
    private StringUtil() {
    }

    public static <E> String toString(Collection<E> collection) {
        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }


}
