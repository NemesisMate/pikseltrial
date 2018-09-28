package com.nemesismate.piksel.trial.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class UUIDUtilsTest {

    private final String idDashes   = "00000000-aaaa-bbbb-cccc-000000000000";
    private final String idNoDashes = "00000000aaaabbbbcccc000000000000";

    private final UUID uuid = UUID.fromString(idDashes);

    @Test
    public void fromString() {
        assertEquals(uuid, UUIDUtils.fromString(idDashes));
        assertEquals(uuid, UUIDUtils.fromString(idNoDashes));
    }

    @Test
    public void fromStringNoDashes() {
        assertEquals(uuid, UUIDUtils.fromStringNoDashes(idNoDashes));
    }

    @Test
    public void toStringNoDashes() {
        assertEquals(idNoDashes, UUIDUtils.toStringNoDashes(uuid));
    }

    @Test
    public void fromStringNoDashesToDashes() {
        assertEquals(idDashes, UUIDUtils.fromStringNoDashesToDashes(idNoDashes));
    }
}