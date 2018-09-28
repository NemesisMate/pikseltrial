package com.nemesismate.piksel.trial.util.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.nemesismate.piksel.trial.util.UUIDUtils;

import java.io.IOException;
import java.util.UUID;

public class NoDashUUIDDeserializer extends UUIDDeserializer {

    @Override
    protected UUID _deserialize(String value, DeserializationContext ctxt) throws IOException {
        String dashedValue = value.contains("-") ? value : UUIDUtils.fromStringNoDashesToDashes(value);
        return super._deserialize(dashedValue, ctxt);
    }

}
