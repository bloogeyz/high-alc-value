package com.highalchighlight.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HighlightStyle
{
    OUTLINE("Outline"),
    UNDERLINE("Underline"),
    FILL("Fill");

    private final String name;

    @Override
    public String toString()
    {
        return name;
    }
}
