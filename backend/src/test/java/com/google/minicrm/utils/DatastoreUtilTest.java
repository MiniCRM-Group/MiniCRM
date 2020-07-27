// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.minicrm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Campaign;
import com.google.minicrm.data.Form;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides Unit Tests for the DatastoreUtil
 */
@RunWith(JUnit4.class)
public class DatastoreUtilTest {
  private static final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private static final String TEST_USER_ID = "testUserId";
  private static final User testUser = new User("email", "authDomain", TEST_USER_ID);
  private static final Key advertiserKey = Advertiser.generateKey(testUser);

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void datastoreUtilExists_whenEntityDoesExist_returnsTrue() throws Exception {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Form existingForm = new Form(advertiserKey, 1, "1");
    datastore.put(existingForm.asEntity());

    assertTrue(DatastoreUtil.exists(existingForm.asEntity().getKey()));
  }

  @Test
  public void datastoreUtilExists_whenEntityDoesNotExist_returnsFalse() throws Exception {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Form existingForm = new Form(advertiserKey, 1, "1");
    datastore.put(existingForm.asEntity());
    Campaign nonexistentCampaign = new Campaign(advertiserKey, 1, "1");

    assertFalse(DatastoreUtil.exists(nonexistentCampaign.asEntity().getKey()));
  }

  @Test
  public void datastoreUtilGet_withValidEntity_getsTheCorrectEntity() throws Exception {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Advertiser advertiser = new Advertiser(testUser);
    datastore.put(advertiser.asEntity());

    Advertiser returnedAdvertiser = new Advertiser(DatastoreUtil.get(advertiser.asEntity().getKey()));

    assertEquals(advertiser, returnedAdvertiser);
  }

  @Test
  public void datastoreUtilGet_nonexistentEntity_returnsNull() throws Exception {
    assertNull(DatastoreUtil.get(advertiserKey));
  }

  @Test
  public void datastoreUtilPut_withNewDatastoreObject_successfullyPutsObject() throws Exception {
    Form existingForm = new Form(advertiserKey, 1, "1");

    DatastoreUtil.put(existingForm);

    assertTrue(DatastoreUtil.exists(existingForm.asEntity().getKey()));
  }
}
