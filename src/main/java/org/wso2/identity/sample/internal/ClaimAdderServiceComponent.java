/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package org.wso2.identity.sample.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.identity.sample.ClaimAdderImp;
import org.wso2.carbon.identity.oauth2.ClaimAdder;


/**
 * @scr.component name="org.wso2.identity.sample.component" immediate="true"
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic" bind="setRegistryService"
 * unbind="unsetRegistryService"
 */
public class ClaimAdderServiceComponent {

    private static Log log = LogFactory.getLog(ClaimAdderServiceComponent.class);

    private static RealmService realmService;
    private static RegistryService registryService = null;

    protected void activate(ComponentContext ctxt) {
        log.info("activated");

        //register UserOperationEventListener implementation
        ClaimAdderImp claimAdderImp = new ClaimAdderImp();
        ctxt.getBundleContext().registerService(ClaimAdder.class.getName(),new ClaimAdderImp(),null);

        if (log.isDebugEnabled()) {
            log.debug("SCIM ID Remover bundle is activated");
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.debug("SCIM ID Remover bundle is deactivated");
        }
    }

    public static RealmService getRealmService() {
        return realmService;
    }

    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting the Realm Service");
        }
        ClaimAdderServiceComponent.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Realm Service");
        }
        ClaimAdderServiceComponent.realmService = null;
    }

    public static RegistryService getRegistryService() {
        return registryService;
    }

    protected void setRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting the Registry Service");
        }
        ClaimAdderServiceComponent.registryService = registryService;
    }

    protected void unsetRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Registry Service");
        }
        ClaimAdderServiceComponent.registryService = null;
    }

}
