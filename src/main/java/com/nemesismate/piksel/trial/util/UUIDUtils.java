package com.nemesismate.piksel.trial.util;

import java.util.UUID;

public final class UUIDUtils {

    private UUIDUtils() { }

    public static UUID fromString(String uuid) {
        return uuid.contains("-") ? UUID.fromString(uuid) : fromStringNoDashes(uuid);
    }

    public static UUID fromStringNoDashes(String uuid) {
        return UUID.fromString(fromStringNoDashesToDashes(uuid));
    }

    public static String toStringNoDashes(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public static String fromStringNoDashesToDashes(String uuid) {
        return uuid.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }

}
