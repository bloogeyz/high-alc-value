package com.highalchighlight.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FireRuneSource {
    STAFF("Fire Staff"),
    TOME("Tome of Fire"),
    RUNES("Fire Runes");

    private final String name;

    @Override
    public String toString()
    {
        return name;
    }
}
