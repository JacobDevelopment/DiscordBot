package me.jacob.discordbot.pojo;

public class GuildData {

    /**
     * <strong>
     * This class serves a POJO for custom-usage rather than dealing with the custom
     * generated JOOQ POJO which can resolve into annoying convictions
     * within the caching system.
     * </strong>
     */

    private Long guildId;
    private Long ownerId;
    private String prefix;
    private Long moderatorId;
    private Long mutedId;
    private Long logsId;
    private Boolean autoDelete;

    public GuildData(Long guildId, Long ownerId, String prefix, Long moderatorId, Long mutedId, Long logsId, boolean autoDelete) {
        this.guildId = guildId;
        this.ownerId = ownerId;
        this.prefix = prefix;
        this.moderatorId = moderatorId;
        this.mutedId = mutedId;
        this.logsId = logsId;
        this.autoDelete = autoDelete;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(Long moderatorId) {
        this.moderatorId = moderatorId;
    }

    public Long getMutedId() {
        return mutedId;
    }

    public void setMutedId(Long mutedId) {
        this.mutedId = mutedId;
    }

    public Long getLogsId() {
        return logsId;
    }

    public void setLogsId(Long logsId) {
        this.logsId = logsId;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}
