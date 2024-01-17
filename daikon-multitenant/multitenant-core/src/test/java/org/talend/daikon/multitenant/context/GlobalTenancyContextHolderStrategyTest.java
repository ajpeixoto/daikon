// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.talend.daikon.multitenant.provider.DefaultTenant;

/**
 * @author agonzalez
 */
public class GlobalTenancyContextHolderStrategyTest extends InheritableThreadLocalTenancyContextHolderStrategyTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        TenancyContextHolder.setStrategyName(TenancyContextHolder.MODE_GLOBAL);
        TenancyContext tc = new DefaultTenancyContext();
        tc.setTenant(new DefaultTenant("id", "myTenant"));
        TenancyContextHolder.setContext(tc);

    }

    @AfterEach
    @Override
    public void tearDown() {
        TenancyContextHolder.setStrategyName(TenancyContextHolder.MODE_THREADLOCAL);
    }
}
