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

package com.google.minicrm.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides Unit tests for the Advertiser data object.
 */
@RunWith(JUnit4.class)
public final class AdvertiserTest {

  private static final String TEST_USER_ID = "testUserId";
  private static final String TEST_USER_ID_2 = "testUserId2";
  private static final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void newAdvertiser_fromValidAdvertiserAsEntity_producesEquivalentValidAdvertiser()
      throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Advertiser advertiser = new Advertiser(testUser);

    Advertiser convertedAdvertiser = new Advertiser(advertiser.asEntity());

    assertEquals(advertiser, convertedAdvertiser);
  }

  @Test(expected = IllegalArgumentException.class)
  public void newAdvertiser_fromInvalidEntityKind_throwsIllegalArgumentException() {
    Entity invalidEntity = new Entity("Lead");

    new Advertiser(invalidEntity);
  }

  @Test
  public void advertiserGenerateKey_withCorrectUser_producesCorrespondingKey() {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Advertiser advertiser = new Advertiser(testUser);

    Key generatedKey = Advertiser.generateKey(advertiser.getUser());

    assertEquals(advertiser.asEntity().getKey(), generatedKey);
  }

  @Test
  public void advertiserGenerateKey_withDifferentUser_generatesDifferentKey()
      throws Exception {
    User user1 = new User("email", "authDomain", TEST_USER_ID);
    User user2 = new User("email2", "authDomain", TEST_USER_ID_2);

    Key advertiserKey1 = Advertiser.generateKey(user1);
    Key advertiserKey2 = Advertiser.generateKey(user2);

    assertNotEquals(advertiserKey1, advertiserKey2);
  }

  @Test
  public void advertiserGenerateWebhook_withValidAdvertiser_generatesWebhookWithCorrespondingId()
      throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getScheme()).thenReturn("https");
    when(request.getServerName()).thenReturn("miniCrm.com");
    when(request.getServerPort()).thenReturn(4200);
    User user1 = new User("email", "authDomain", TEST_USER_ID);
    Advertiser advertiser = new Advertiser(user1);

    String webhook = advertiser.generateWebhook(request);

    //get the query parameters
    String query = webhook.substring(webhook.indexOf("?") + 1);
    String[] keyValuePairs = query.split("&");
    Map<String, String> params = new HashMap<String, String>();
    for (String keyValuePair : keyValuePairs) {
      String[] pair = keyValuePair.split("=");
      params.put(pair[0], pair[1]);
    }
    //assert that the id parameter exists and correctly refers to the right advertiser
    Key webhookAdvertiserKey = KeyFactory.stringToKey(params.get("id"));
    assertEquals(advertiser.asEntity().getKey(), webhookAdvertiserKey);
  }
}
