/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mulesoft.demo.twitter;

import org.mule.api.MuleEvent;
import org.mule.api.transport.PropertyScope;
import org.mule.construct.SimpleFlowConstruct;
import org.mule.tck.FunctionalTestCase;

public class TwitterFunctionalTestDriver extends FunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    public void testUpdateStatus() throws Exception
    {
        MuleEvent event = getTestEvent("");
        event.getMessage().setProperty("status", "Hello world", PropertyScope.INBOUND);
        MuleEvent process = lookupFlowConstruct("UpdateStatus").process(event);
        System.out.println(process.getMessage().getPayload());
    }

    private SimpleFlowConstruct lookupFlowConstruct(final String name)
    {
        return (SimpleFlowConstruct) muleContext.getRegistry().lookupFlowConstruct(name);
    }

}
