/*
 * Copyright 2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.cassandra.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

/**
 * @author Scott Marlow
 */
@RunWith(Arquillian.class)
@DefaultDeployment
public class CassandraArquillianTest {

    @ArquillianResource
    InitialContext context;

    @Test
    public void resourceLookup() throws Exception {
        Object cassandra = context.lookup("java:jboss/cassandradriver/test");
        assertNotNull(cassandra);
    }
    
    @Inject
    @Named("cassandratestprofile")
    private Cluster connection;
   
    @Test
    public void injectDatabaseConnection() throws Exception {
       assertNotNull(connection);
    }

    @EJB(lookup = "java:global/CassandraArquillianTest/StatefulTestBean")
        private StatefulTestBean statefulTestBean;

    @Test
    public void testSimpleCreateAndLoadEntities() throws Exception {
        Row row = statefulTestBean.query();
        String lastName = row.getString("lastname");
        assertEquals(lastName,"Smith");
        int age = row.getInt("age");
        assertEquals(age,36);
    }

    @Test
    public void testAsyncQuery() throws Exception {
        Row row = statefulTestBean.asyncQuery();
        String lastName = row.getString("lastname");
        assertEquals(lastName,"Smith");
        int age = row.getInt("age");
        assertEquals(age,36);
    }

}
