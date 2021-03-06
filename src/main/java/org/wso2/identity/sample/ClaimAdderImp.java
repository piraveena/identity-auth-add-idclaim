package org.wso2.identity.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.authz.OAuthAuthzReqMessageContext;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenRespDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeRespDTO;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oidc.session.OIDCSessionConstants;
import org.wso2.carbon.identity.oidc.session.OIDCSessionState;
import org.wso2.carbon.identity.oidc.session.cache.OIDCBackChannelAuthCodeCache;
import org.wso2.carbon.identity.oidc.session.cache.OIDCBackChannelAuthCodeCacheEntry;
import org.wso2.carbon.identity.oidc.session.cache.OIDCBackChannelAuthCodeCacheKey;
import org.wso2.carbon.identity.oidc.session.util.OIDCSessionManagementUtil;
import org.wso2.carbon.identity.openidconnect.ClaimAdder;


import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ClaimAdderImp implements ClaimAdder {

    private static Log log = LogFactory.getLog(ClaimAdderImp.class);
    private String claimValue;
    private String name;


    @Override
    public Map<String, Object> getAdditionalClaims(OAuthAuthzReqMessageContext oAuthAuthzReqMessageContext,
                                                   OAuth2AuthorizeRespDTO oAuth2AuthorizeRespDTO)
            throws IdentityOAuth2Exception {
        //Adding sid claim to ID token
        Map<String, Object> addtionalClaims = new HashMap<>();
        this.name = "sid";
        //Create a new sid value when new session is initiated
        if (getSessionState(oAuthAuthzReqMessageContext) == null) {
            claimValue = UUID.randomUUID().toString();
            if (log.isDebugEnabled()) {
                log.debug("sid claim found for Back channel Implicit flow ");
            }
        //Get sid claim from OIDCSessionState when a session is already existing
        } else {
            claimValue = getSessionState(oAuthAuthzReqMessageContext).getSidClaim();
            if (log.isDebugEnabled()) {
                log.debug("sid claim found for Back channel Implicit flow ");
            }
        }


        addtionalClaims.put(name, claimValue);
        return addtionalClaims;


    }


    @Override
    public Map<String, Object> getAdditionalClaims(OAuthTokenReqMessageContext oAuthTokenReqMessageContext,
                                                   OAuth2AccessTokenRespDTO oAuth2AccessTokenRespDTO)
            throws IdentityOAuth2Exception {
        //Adding sid claim to ID token
        Map<String, Object> addtionalClaims = new HashMap<>();
        String accessCode = oAuthTokenReqMessageContext.getOauth2AccessTokenReqDTO().getAuthorizationCode();
        OIDCBackChannelAuthCodeCacheEntry cacheEntry = getSessionIdFromCache(accessCode);
        if (cacheEntry != null) {
            claimValue = cacheEntry.getSessionId();
        }
        if (claimValue != null) {
            if (log.isDebugEnabled()) {
                log.debug("sid claim found for Back channel Authorization code flow ");
            }

            addtionalClaims.put("sid", claimValue);
            return addtionalClaims;
        }
        return null;


    }


    public OIDCSessionState getSessionState(OAuthAuthzReqMessageContext oAuthAuthzReqMessageContext) {

        Cookie[] cookies = oAuthAuthzReqMessageContext.getAuthorizationReqDTO().getCookie();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(OIDCSessionConstants.OPBS_COOKIE_ID)) {
                OIDCSessionState previousSessionState = OIDCSessionManagementUtil.getSessionManager()
                        .getOIDCSessionState(cookie.getValue());
                return previousSessionState;

            }

        }
        return null;
    }

    private OIDCBackChannelAuthCodeCacheEntry getSessionIdFromCache(String authCode) {

        OIDCBackChannelAuthCodeCacheKey cacheKey = new OIDCBackChannelAuthCodeCacheKey(authCode);
        OIDCBackChannelAuthCodeCacheEntry cacheEntry = OIDCBackChannelAuthCodeCache.getInstance().getValueFromCache
                (cacheKey);
        return cacheEntry;
    }


}