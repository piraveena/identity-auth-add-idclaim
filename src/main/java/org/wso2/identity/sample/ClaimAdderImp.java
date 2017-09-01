package org.wso2.identity.sample;

import com.hazelcast.web.SessionState;
import org.wso2.carbon.identity.oauth2.ClaimAdder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.authz.OAuthAuthzReqMessageContext;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeRespDTO;
import org.wso2.carbon.identity.oauth2.model.HttpRequestHeader;
import org.wso2.carbon.identity.oidc.session.OIDCSessionConstants;
import org.wso2.carbon.identity.oidc.session.OIDCSessionState;
import org.wso2.carbon.identity.oidc.session.util.OIDCSessionManagementUtil;



import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ClaimAdderImp implements ClaimAdder {

    private static Log log = LogFactory.getLog(ClaimAdder.class);
    private int claimValue;
    private String name;
    private Cookie obpsCookie;
    private String value;


    @Override
    public Map<String, Object> getAdditionalClaims(OAuthAuthzReqMessageContext oAuthAuthzReqMessageContext,
                                                   OAuth2AuthorizeRespDTO oAuth2AuthorizeRespDTO)
            throws IdentityOAuth2Exception {

        Map<String, Object> addtionalClaims = new HashMap<>();

        this.name = "sid";
        if (getSessionState(oAuthAuthzReqMessageContext) == null) {
       //     this.claimValue = (int) (Math.random() * 1000000 + 10000000L);
            String value= UUID.randomUUID().toString();
            log.info("claim sid: " + claimValue);
        } else {
            claimValue = getSessionState(oAuthAuthzReqMessageContext).getSidClaim();
        }

        addtionalClaims.put(name, value);
        return addtionalClaims;


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
}