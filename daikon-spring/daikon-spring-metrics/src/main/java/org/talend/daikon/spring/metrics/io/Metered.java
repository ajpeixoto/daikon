// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.metrics.io;

public interface Metered {

    enum Type {
        IN("in"),
        OUT("out");

        private String tag;

        Type(String tag) {
            this.tag = tag;
        }

        String getMeterTag() {
            return tag;
        }

        public String getTag() {
            return tag;
        }
    }

    long getVolume();

    Type getType();

}
