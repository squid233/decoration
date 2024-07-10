package io.github.squid233.decoration.item;

import net.minecraft.util.StringIdentifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author squid233
 * @since 0.1.0
 */
public enum WireType implements StringIdentifiable {
    OVERHEAD_LINE("overhead_line"),
    WIRE("wire");

    public static final List<WireType> LIST = List.of(values());
    private static final Map<String, WireType> MAP;
    private final String stringValue;

    static {
        final var map = new HashMap<String, WireType>();
        for (WireType wireType : LIST) {
            map.put(wireType.asString(), wireType);
        }
        MAP = Collections.unmodifiableMap(map);
    }

    WireType(String stringValue) {
        this.stringValue = stringValue;
    }

    public static WireType fromString(String s) {
        return MAP.getOrDefault(s, OVERHEAD_LINE);
    }

    @Override
    public String asString() {
        return stringValue;
    }
}
