/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.model.authorize;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.log.Log;
import org.xdi.oxauth.model.registration.Client;
import org.xdi.oxauth.service.ScopeService;

import java.util.HashSet;
import java.util.Set;

/**
 * Validates the scopes received for the authorize web service.
 *
 * @author Yuriy Zabrovarnyy
 * @author Yuriy Movchan
 * @version June 3, 2015
 */
@Scope(ScopeType.STATELESS)
@Name("scopeChecker")
@AutoCreate
public class ScopeChecker {

    @Logger
    private Log log;

    public Set<String> checkScopesPolicy(Client client, String scope) {
        log.debug("Checking scopes policy for: " + scope);
        Set<String> grantedScopes = new HashSet<String>();

        ScopeService scopeService = ScopeService.instance();

        final String[] scopesRequested = scope.split(" ");
        final String[] scopesAllowed = client.getScopes();

        for (String scopeRequested : scopesRequested) {
            if (StringUtils.isNotBlank(scopeRequested)) {
                for (String scopeAllowedDn : scopesAllowed) {
                    org.xdi.oxauth.model.common.Scope scopeAllowed = scopeService.getScopeByDnSilently(scopeAllowedDn);
                    if (scopeAllowed != null) {
                        String scopeAllowedName = scopeAllowed.getDisplayName();
                        if (scopeRequested.equals(scopeAllowedName)) {
                            grantedScopes.add(scopeRequested);
                        }
                    }
                }
            }
        }

        log.debug("Granted scopes: " + grantedScopes);

        return grantedScopes;
    }

    /**
     * Get ScopeChecker instance
     *
     * @return ScopeChecker instance
     */
    public static ScopeChecker instance() {
        boolean createContexts = !Contexts.isEventContextActive() && !Contexts.isApplicationContextActive();
        if (createContexts) {
            Lifecycle.beginCall();
        }

        return (ScopeChecker) Component.getInstance(ScopeChecker.class);
    }

}