package org.talend.daikon.spring.auth.model.token;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.talend.daikon.spring.auth.common.model.userdetails.AuthUserDetails;

public class IamBearerTokenAuthentication extends BearerTokenAuthentication {

    private static final String USER_DETAILS_NAME_SA_SUFFIX = " - Service Account";

    private AuthUserDetails authUserDetails;

    public IamBearerTokenAuthentication(OAuth2AuthenticatedPrincipal principal, OAuth2AccessToken credentials,
                                        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);

        if (this.getPrincipal() instanceof AuthUserDetails) {
            this.authUserDetails = (AuthUserDetails) this.getPrincipal();
        }
    }

    @Override
    public String getName() {
        if (isSat()) {
            return computeName();
        } else {
            return super.getName();
        }
    }

    private boolean isSat() {
        return Objects.nonNull(this.authUserDetails) && this.authUserDetails.getUsername().contains(USER_DETAILS_NAME_SA_SUFFIX);
    }

    private String computeName() {
        if (this.authUserDetails.getTenantName() != null) {
            return this.authUserDetails.getUsername() + "@" + this.authUserDetails.getTenantName();
        } else {
            return this.authUserDetails.getUsername();
        }
    }
}
