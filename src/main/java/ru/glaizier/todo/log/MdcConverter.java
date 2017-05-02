package ru.glaizier.todo.log;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.Map;

/**
 * Code of this converter was provided by WPP-team to implement unified logging style
 */
public class MdcConverter extends ch.qos.logback.classic.pattern.MDCConverter {

    @Override
    public String convert(ILoggingEvent event) {
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        return (mdcPropertyMap == null || mdcPropertyMap.isEmpty()) ? ""
                : generateOutput(mdcPropertyMap);
    }

    /**
     * if no key is specified, return all the values present in the MDC, in the
     * format "[k1::v1][k2::v2], ..."
     */
    private String generateOutput(Map<String, String> mdcPropertyMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
            sb.append('[').append(entry.getKey()).append("::").append(entry.getValue()).append(']');
        }
        return sb.toString();
    }
}