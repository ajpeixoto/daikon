package org.talend.daikon.spring.reactive.sat.authentication;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class TalendJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public static final String TALEND_ENTITLEMENTS_FIELD = "entitlements";

    public static final String TALEND_PERMISSIONS_FIELD = "permissions";

    public static final String TALEND_SAT_PERMISSIONS_FIELD = "https://talend.cloud/permissions";

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> ents = Arrays.asList(((String) jwt.getClaims().getOrDefault(TALEND_ENTITLEMENTS_FIELD, "")).split(","));
        List<String> permsList = (List<String>) jwt.getClaims().getOrDefault(TALEND_PERMISSIONS_FIELD, Collections.emptyList());
        List<String> satPerms = (List<String>) jwt.getClaims().getOrDefault(TALEND_SAT_PERMISSIONS_FIELD,
                Collections.emptyList());
        List<String> authorities = Stream.of(permsList.stream(), ents.stream(), satPerms.stream()).flatMap(i -> i)
                .filter(s -> !s.isEmpty()).toList();
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
