/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.katharsis.servlet;

import io.katharsis.invoker.KatharsisInvokerBuilder;
import io.katharsis.locator.JsonServiceLocator;
import io.katharsis.locator.SampleJsonServiceLocator;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * (Testing purpose) Sample Katharsis integration servlet filter.
 */
public class SampleKatharsisFilter extends AbstractKatharsisFilter {

    private String resourceSearchPackage;
    private String resourceDefaultDomain;

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        resourceSearchPackage = filterConfig.getInitParameter(KatharsisProperties.RESOURCE_SEARCH_PACKAGE);
        resourceDefaultDomain = filterConfig.getInitParameter(KatharsisProperties.RESOURCE_DEFAULT_DOMAIN);
    }

    public String getResourceSearchPackage() {
        return resourceSearchPackage;
    }

    public void setResourceSearchPackage(String resourceSearchPackage) {
        this.resourceSearchPackage = resourceSearchPackage;
    }

    public String getResourceDefaultDomain() {
        return resourceDefaultDomain;
    }

    public void setResourceDefaultDomain(String resourceDefaultDomain) {
        this.resourceDefaultDomain = resourceDefaultDomain;
    }

    /**
     * NOTE: A class extending this must provide a platform specific {@link JsonServiceLocator}
     *       instead of the (testing-purpose) {@link SampleJsonServiceLocator} below
     *       in order to provide advanced dependency injections for the repositories.
     */
    @Override
    protected KatharsisInvokerBuilder createKatharsisInvokerBuilder() {
        return new KatharsisInvokerBuilder()
                .resourceSearchPackage(getResourceSearchPackage())
                .resourceDefaultDomain(getResourceDefaultDomain())
                .jsonServiceLocator(new SampleJsonServiceLocator());
    }

}
