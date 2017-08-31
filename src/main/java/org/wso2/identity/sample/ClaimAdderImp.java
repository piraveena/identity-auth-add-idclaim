package org.wso2.identity.sample;

import org.wso2.carbon.identity.oauth2.ClaimAdder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.authz.OAuthAuthzReqMessageContext;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeRespDTO;
import org.wso2.carbon.identity.oauth2.model.HttpRequestHeader;
import org.wso2.carbon.identity.oidc.session.OIDCSessionState;
import org.wso2.carbon.identity.oidc.session.util.OIDCSessionManagementUtil;



import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;


public class ClaimAdderImp implements ClaimAdder {

    private static Log log = LogFactory.getLog(ClaimAdder.class);
    private int claimValue;
    private String name;


    @Override
    public Map<String, Object> getAdditionalClaims(OAuthAuthzReqMessageContext oAuthAuthzReqMessageContext,
                                                   OAuth2AuthorizeRespDTO oAuth2AuthorizeRespDTO)
            throws IdentityOAuth2Exception {

        Map<String, Object> addtionalClaims = new HashMap<>();

        this.name = "sid";
        Cookie opbsCookie= oAuthAuthzReqMessageContext.getAuthorizationReqDTO().getObpscookie();
        if (opbsCookie == null) {
            log.info("no obps cookie");
            this.claimValue = (int) (Math.random() * 1000000 + 10000000L);
            log.info("claim sid: " + claimValue);


        }
        else {
            OIDCSessionState previousSessionState =
                    OIDCSessionManagementUtil.getSessionManager()
                            .getOIDCSessionState(opbsCookie.getValue());
            claimValue=previousSessionState.getSidClaim();
        }


//        for( HttpRequestHeader header: requestHeaders) {
//            St
// ring name = header.getName();
//            log.info(name);
//            if (header.getName().equals("cookie")) {
//                String[] values = header.getValue();
//                for (String value : values) {
//                    if (!value.contains("opbs")) {
//                        this.claimValue = (int) (Math.random() * 1000000 + 10000000L);
//                        log.info("claim sid: "+ value);
//                        this.name="sid";
//                        break;
//
//
//                    }
//                    else{
//                        Sess
//                    }
//                }
//            }

        addtionalClaims.put(name, claimValue);
        return addtionalClaims;


    }
}