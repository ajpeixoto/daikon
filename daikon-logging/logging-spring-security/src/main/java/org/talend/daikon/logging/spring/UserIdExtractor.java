// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.logging.spring;

import java.util.Optional;

public interface UserIdExtractor {

    Optional<String> extract();
}
