package com.pds.localpos.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CorrelationIdHolder {

    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

    public void set(String id) {
        correlationId.set(id);
    }

    public String get() {
        return correlationId.get();
    }

    public void clear() {
        correlationId.remove();
    }
}
