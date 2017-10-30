package com.github.darrmirr.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.Duration;

/*
 * @author Darr Mirr
 */

public enum UserStatusEnum {
    ONLINE(5), AWAY(30), OFFLINE(60), NONE(0);

    @JsonValue
    private String name;
    private Duration duration;

    UserStatusEnum(long duration) {
        this.name = this.name().toLowerCase();
        this.duration = Duration.ofMinutes(duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDurationSeconds() {
        return duration.getSeconds();
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
