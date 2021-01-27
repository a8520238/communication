package com.wenda.communicationsystem.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Liguangzhe
 * @Date created in 15:03 2020/6/9
 */
public class EventModel {
    private EventType type;
    private int actorId;
    private int entryId;
    private int entryType;
    private int entryOwnerId;

    private Map<String, String> exts = new HashMap<>();

    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String value) {
        return exts.get(value);
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntryId() {
        return entryId;
    }

    public EventModel setEntryId(int entryId) {
        this.entryId = entryId;
        return this;
    }

    public int getEntryType() {
        return entryType;
    }

    public EventModel setEntryType(int entryType) {
        this.entryType = entryType;
        return this;
    }

    public int getEntryOwnerId() {
        return entryOwnerId;
    }

    public EventModel setEntryOwnerId(int entryOwnerId) {
        this.entryOwnerId = entryOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
