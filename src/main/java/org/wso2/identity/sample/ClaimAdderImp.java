package org.wso2.identity.sample;

import org.wso2.carbon.identity.oauth2.ClaimAdder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class ClaimAdderImp implements ClaimAdder{
    private static Log log=LogFactory.getLog(ClaimAdder.class);
    private int sid;
    @Override
    public void addClaim() {
        log.info("claim sid");
        sid=(int)(Math.random()*1000000 + 10000000L);
    }
    public Integer getClaim(){
        return this.sid;
    }
}
