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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.identity.sample.ClaimAdderImp;
import org.wso2.carbon.identity.openidconnect.ClaimAdder;

@Component(
        name = "org.wso2.identity.sample.internal.ClaimAdderServiceComponent",
        immediate = true
)
public class ClaimAdderServiceComponent {

    private static Log log = LogFactory.getLog(ClaimAdderServiceComponent.class);

    private static RealmService realmService;
    private static RegistryService registryService = null;

    @Activate
    protected void activate(ComponentContext ctxt) {

        ClaimAdderImp claimAdderImp = new ClaimAdderImp();
        ctxt.getBundleContext().registerService(ClaimAdder.class.getName(), claimAdderImp, null);

        if (log.isDebugEnabled()) {
            log.debug("ClaimAdder bundle is activated");
        }
    }

    protected void deactivate(ComponentContext ctxt) {

        if (log.isDebugEnabled()) {
            log.debug("ClaimAdder bundle is deactivated");
        }
    }


}
