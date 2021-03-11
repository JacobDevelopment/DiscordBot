package me.jacob.discordbot.util;

import me.jacob.discordbot.entity.command.type.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.util.concurrent.TimeUnit;

public class MsgUtil {
    private MsgUtil() {
    }


    public static void reply(TextChannel textChannel, Member member, String content) {
        textChannel.sendMessage(EmbedUtil.getDefault(member, content).build())
                .delay(30, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }

    public static void reply(TextChannel textChannel, EmbedBuilder embedBuilder) {
        textChannel.sendMessage(embedBuilder.build())
                .delay(30, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }

    public static void replyError(TextChannel textChannel, Member member, ErrorType errorType, Object... objects) {
        textChannel.sendMessage(EmbedUtil.getError(member, errorType, objects).build())
                .delay(30, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }

    public static void replyError(TextChannel textChannel, Member member, ErrorType errorType) {
        textChannel.sendMessage(EmbedUtil.getError(member, errorType).build())
                .delay(30, TimeUnit.SECONDS)
                .flatMap(Message::delete)
                .queue();
    }

    public static void sendPrivateMessage(User user, String content) {
        sendPrivateMessageFormatted(user, content);
    }

    public static void sendPrivateMessageFormatted(User user, String content, Object... objects) {
        user.openPrivateChannel()
                .flatMap(privateChannel -> privateChannel.sendMessageFormat(content, objects))
                .queue(
                        null,
                        new ErrorHandler().ignore(ErrorResponse.CANNOT_SEND_TO_USER)
                );
    }

}
