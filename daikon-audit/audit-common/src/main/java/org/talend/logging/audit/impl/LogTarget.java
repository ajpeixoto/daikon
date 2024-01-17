// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit.impl;

/**
 *
 */
public enum LogTarget {
    OUTPUT("System.out"),
    ERROR("System.err");

    private final String target;

    LogTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
