package com.nemesismate.piksel.trial.util.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import com.nemesismate.piksel.trial.util.UUIDUtils;

import java.io.IOException;
import java.util.UUID;

public class NoDashUUIDSerializer extends UUIDSerializer {

    @Override
    public void serialize(UUID value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(UUIDUtils.toStringNoDashes(value).toCharArray(), 0, 32);
    }

}
