// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.java8;

import java.io.Serializable;

/** Adds Serializable to the Function interface. */
public interface SerializableConsumer<T> extends Consumer<T>, Serializable {

}