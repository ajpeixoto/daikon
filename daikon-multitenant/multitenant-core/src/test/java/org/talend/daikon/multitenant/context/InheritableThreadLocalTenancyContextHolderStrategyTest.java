// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.talend.daikon.multitenant.provider.DefaultTenant;

public class InheritableThreadLocalTenancyContextHolderStrategyTest extends TenancyContextHolderTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        TenancyContextHolder.setStrategyName(TenancyContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @AfterEach
    @Override
    public void tearDown() {
        TenancyContextHolder.setStrategyName(TenancyContextHolder.MODE_THREADLOCAL);
    }

    @Test
    public void testSpawnThread() throws InterruptedException {
        TenancyContext tc = new DefaultTenancyContext();
        tc.setTenant(new DefaultTenant("id", "myTenant"));
        TenancyContextHolder.setContext(tc);
        StatusHolder statusHolder = new StatusHolder();
        Runnable runnable = () -> {
            statusHolder.assertEquals(tc, TenancyContextHolder.getContext());
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join(60000L);
        statusHolder.assertSuccess();
    }
}
