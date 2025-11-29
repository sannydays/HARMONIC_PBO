package com.app.hormontracker.model.base;

import java.util.UUID;

public abstract class BaseModel implements Identifiable {
    protected final String id;

    protected BaseModel() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }
}