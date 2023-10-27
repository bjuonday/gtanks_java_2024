package amalgama.utils;

import amalgama.network.Type;

public class TypeUtils {
    public static Type typeOf(String type) {
        Type t;
        try {
            t = Type.valueOf(type);
        } catch (IllegalArgumentException e) {
            t = Type.UNKNOWN;
        }
        return t;
    }
}
