// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.ccf.context.provided;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.talend.iam.scim.model.GroupRef;
import org.talend.iam.scim.model.User;

@Service
public class DefaultTenantContextHolder {

    private static final ThreadLocal<DefaultTenantContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private DefaultTenantContextHolder() {
    }

    public static void setTenantContext(DefaultTenantContext defaultTenantContext) {
        CONTEXT_HOLDER.set(defaultTenantContext);
    }

    public static void setContextWithTenantId(String tenantId) {
        CONTEXT_HOLDER.set(DefaultTenantContext.builder().tenantId(tenantId).build());
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    public static void setPublicContext() {
        CONTEXT_HOLDER.set(DefaultTenantContext.builder().tenantId("public").build());
    }

    public static String getCurrentTenantId() {

        return CONTEXT_HOLDER.get() == null ? null : CONTEXT_HOLDER.get().getTenantId();
    }

    public static String getCurrentUserId() {
        return CONTEXT_HOLDER.get().getUserId();
    }

    public static List<String> getGroupIds() {
        return Optional.ofNullable(CONTEXT_HOLDER.get().getCurrentUser()).map(User::getGroups).orElseGet(Collections::emptyList)
                .stream().map(GroupRef::getValue).toList();
    }

    public static DefaultTenantContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void init() {
        CONTEXT_HOLDER.set(DefaultTenantContext.builder().build());
    }

    public static boolean isEmpty() {
        return CONTEXT_HOLDER.get() == null;
    }
}
