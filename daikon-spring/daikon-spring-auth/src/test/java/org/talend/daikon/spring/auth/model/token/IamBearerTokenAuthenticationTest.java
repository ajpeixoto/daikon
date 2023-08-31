package org.talend.daikon.spring.auth.model.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.talend.daikon.spring.auth.model.token.SatAuthenticationToken.USER_DETAILS_NAME_SA_SUFFIX;

import java.time.Instant;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.talend.daikon.spring.auth.common.model.userdetails.AuthUserDetails;

public class IamBearerTokenAuthenticationTest {

    private static final String USERNAME = "My awesome service account".concat(USER_DETAILS_NAME_SA_SUFFIX);

    private static final String TENANT_NAME = "tenant.name";

    private static final String TENANT_ID = "ce8bca4e-c6c2-42b5-8705-02b8af3a94bb";

    private static final String PASSWORD = "password";

    private static final String TOKEN_VALUE = "tokenValue";

    private static final String USERNAME_FOR_NON_SAT_TOKEN = "username";

    private AuthUserDetails authUserDetails;

    @BeforeEach
    public void setup() {

        this.authUserDetails = new AuthUserDetails(USERNAME, "password", Collections.emptyList());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("tenantid", TENANT_ID);
        this.authUserDetails.setAttributes(attributes);
    }

    @Test
    void testGetNameForSatWithoutTenantName() {

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "tokenValue", Instant.now(),
                Instant.now().plusSeconds(5));
        IamBearerTokenAuthentication iamBearerTokenAuthentication = new IamBearerTokenAuthentication(this.authUserDetails,
                accessToken, new ArrayList<GrantedAuthority>());

        String name = iamBearerTokenAuthentication.getName();

        assertEquals(USERNAME, name);
    }

    @Test
    void testGetNameForSatWitTenantName() {

        this.authUserDetails.setTenantName(TENANT_NAME);
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "tokenValue", Instant.now(),
                Instant.now().plusSeconds(5));
        IamBearerTokenAuthentication iamBearerTokenAuthentication = new IamBearerTokenAuthentication(this.authUserDetails,
                accessToken, new ArrayList<GrantedAuthority>());

        String name = iamBearerTokenAuthentication.getName();

        assertEquals(USERNAME + "@" + TENANT_NAME, name);
    }

    @Test
    void testGetNameForAnNonSatBearer() {

        AuthUserDetails authUserDetails1 = new AuthUserDetails(USERNAME_FOR_NON_SAT_TOKEN, PASSWORD, Collections.emptyList());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("tenantid", TENANT_ID);
        authUserDetails1.setAttributes(attributes);
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, TOKEN_VALUE, Instant.now(),
                Instant.now().plusSeconds(5));
        IamBearerTokenAuthentication iamBearerTokenAuthentication = new IamBearerTokenAuthentication(authUserDetails1,
                accessToken, new ArrayList<GrantedAuthority>());

        String name = iamBearerTokenAuthentication.getName();

        assertEquals(USERNAME_FOR_NON_SAT_TOKEN, name);
    }
}
