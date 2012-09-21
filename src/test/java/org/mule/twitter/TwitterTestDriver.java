/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.twitter;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import twitter4j.internal.http.alternative.MuleHttpClient;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.callback.SourceCallback;
import org.mule.tck.junit4.AbstractMuleContextTestCase;

import twitter4j.Status;
import twitter4j.TwitterException;

public class TwitterTestDriver extends AbstractMuleContextTestCase
{

    private TwitterConnector connector;

    public TwitterTestDriver()
    {
        super();
    }
    
    @Before
    public void doSetUp() throws Exception
    {
    	
        connector = new TwitterConnector();
        connector.setConsumerKey(System.getenv("consumerKey"));
        connector.setConsumerSecret(System.getenv("consumerSecret"));
        connector.setAccessToken(System.getenv("accessToken"));
        connector.setAccessTokenSecret(System.getenv("accessTokenSecret"));
        connector.setUseSSL(true);
        connector.init();
        MuleHttpClient.setMuleContext(muleContext);
    }
    
    @Test
    public void testShowUsersIsWorking() throws TwitterException
    {
    	connector.showUser(null);
    }

    @Test
    public void testSearch() throws Exception
    {
    	assertNotNull(connector.search(null, "world", null, null, 0L, 0, 0, "2012-04-23", 0L,
            "37.781157,-122.398720", "25", "mi", null, "mixed"));
    }

    @Test
    public void testGetTrends() throws Exception
    {
        assertNotNull(connector.getLocationTrends(null, 1));
    }

    @Test
    public void testPublicTimeline() throws Exception
    {
        assertNotNull(connector.getPublicTimeline(null));
    }

    @Test
    public void testSearchPlaces() throws Exception
    {
        assertNotNull(connector.searchPlaces(null, 50.0, 50.0, null));
    }

    @Test
    public void testGetUserInfo() throws Exception
    {
        System.out.println(connector.showUser(null));
    }

    @Test
    public void testUpdateStatus() throws Exception
    {
        long id = connector.updateStatus(null, "Foo bar baz " + new Date(), -1, null, null).getId();
        assertTrue(connector.showStatus(null, id).getText().contains("Foo bar baz"));
    }
    
    @Test
    /** Run only one of those tests per connector instance */
    public void testSampleStream() throws Exception
    {
        connector.sampleStream(new SourceCallback()
        {
            @Override
            public Object process() throws Exception
            {
                return null;
            }

            @Override
            public Void process(Object payload)
            {
                assertNotNull(payload);
                assertTrue(payload instanceof Status);
                System.out.println("Sample: " + payload);
                return null;
            }

            public Object process(Object payload, Map<String, Object> properties) throws Exception
            {
                System.out.println(payload);
                return null;
            }
        });
        Thread.sleep(10000);
    }

    /** Run only one of those tests per connector instance */
    public void ignoreTestFilteredStream() throws Exception
    {
        connector.filteredStream(0, null, Arrays.asList("mulesoft"), new SourceCallback()
        {
            @Override
            public Object process() throws Exception
            {
                return null;
            }

            @Override
            public Void process(Object payload)
            {
                assertNotNull(payload);
                assertTrue(payload instanceof Status);
                System.out.println("Filtered: " + payload);
                return null;
            }

            public Object process(Object payload, Map<String, Object> properties) throws Exception
            {
                return null;
            }
        });
        Thread.sleep(20000);
    }

    /** Run only one of those tests per connector instance */
    public void ignoreTestUserStream() throws Exception
    {
        connector.userStream(null, new SourceCallback()
        {
            @Override
            public Object process() throws Exception
            {
                return null;
            }

            @Override
            public Void process(Object payload)
            {
                assertNotNull(payload);
                assertTrue(payload instanceof UserEvent);
                System.out.println("User: " + payload);
                return null;
            }

            public Object process(Object payload, Map<String, Object> properties) throws Exception
            {
                return null;
            }
        });

        connector.updateStatus(null, "Foobar " + new Date(), -1, null, null);
        Thread.sleep(1000);

        connector.updateStatus(null, "Foobar " + new Date(), -1, null, null);
        Thread.sleep(1000);

        connector.updateStatus(null, "Foobar " + new Date(), -1, null, null);
        Thread.sleep(1000);

        connector.updateStatus(null, "Foobar " + new Date(), -1, null, null);
        Thread.sleep(10000);
    }
}
