// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.access;

import java.util.function.Function;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RequiresTestComponent {

    public static class AlwaysFalse implements Function<ApplicationContext, Boolean> {

        @Override
        public Boolean apply(ApplicationContext applicationContext) {
            return Boolean.FALSE;
        }
    }

    @RequiresAuthority(authority = { "TestComponentExec", "Test 0" }, value = { "Test 1", "Test 2" })
    public String authoritiesValuesPriority() {
        return "secret string";
    }

    @RequiresAuthority(authority = "TestComponentExec", value = { "Test 1", "Test 2" })
    public String authorityValuesPriority() {
        return "secret string";
    }

    @RequiresAuthority(authority = "TestComponentExec", value = { "Test 1", "Test 2" }, activeIf = AlwaysFalse.class)
    public String authorityValuesWithFalsyActiveIf() {
        return "secret string";
    }

    @RequiresAuthority(authority = "", value = { "Test 1",
            "Test 2" }, activeIf = RequiresAuthorityActiveIfDefaults.AlwaysTrue.class)
    public String mssingAuthorityWithTruthyActiveIf() {
        return "secret string";
    }

    @RequiresAuthority(authority = "TestComponentExec", value = { "Test 1",
            "Test 2" }, activeIf = RequiresAuthorityActiveIfDefaults.AlwaysTrue.class)
    public String authorityValuesWithTruthyActiveIf() {
        return "secret string";
    }

    @RequiresAuthority(authority = "", value = { "TestComponentExec", "Test" })
    public String emptyAuthorityValuesPriority() {
        return "secret string";
    }

    @RequiresAuthority({})
    public String emptyValuesPriority() {
        return "secret string";
    }

    @RequiresAuthority({ "TestComponentExec", "Test" })
    public String valuesPriority() {
        return "secret string";
    }

    @RequiresAuthority(authority = "TestComponentExec", value = "Test")
    public String authorityValuePriority() {
        return "secret string";
    }

    @RequiresAuthority(authority = "", value = "TestComponentExec")
    public String emptyAuthorityValuePriority() {
        return "secret string";
    }

    @RequiresAuthority("TestComponentExec")
    public String execute() {
        return "secret string";
    }

    @RequiresAuthority(authority = "TestComponentExec", onDeny = AccessDeniedDefaults.EmptyString.class)
    public String executeWithFallback() {
        return "secret string";
    }

    public String getDetails() {
        return "My details";
    }
}
