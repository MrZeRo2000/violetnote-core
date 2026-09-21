package com.romanpulov.violetnotecore.Model;

import java.util.Objects;

public final class Attribute {
    public static final String ATTR_NAME = "name";
    public static final String ATTR_VALUE = "value";

    private final String name;
    private final String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return "Attribute{name='" + name + "', value='" + value + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;
        Attribute other = (Attribute) o;
        return java.util.Objects.equals(name, other.name)
                && java.util.Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}