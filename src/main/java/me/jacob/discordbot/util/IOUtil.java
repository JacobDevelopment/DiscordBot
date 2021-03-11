package me.jacob.discordbot.util;


import okio.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class IOUtil {

    private IOUtil() {
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        return ByteString.read(inputStream, inputStream.available()).string(StandardCharsets.UTF_8);
    }
}
