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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides Unit tests for the Campaign data object.
 */
@RunWith(JUnit4.class)
public final class CampaignTest {

  private static final String TEST_USER_ID = "testUserId";
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
  public void newCampaign_fromValidCampaignAsEntity_producesEquivalentValidCampaign()
      throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Campaign campaign = new Campaign(advertiserKey, 1, "campaign1");

    Campaign convertedCampaign = new Campaign(campaign.asEntity());

    assertEquals(campaign, convertedCampaign);
  }

  @Test(expected = IllegalArgumentException.class)
  public void newCampaign_fromInvalidEntityKind_throwsIllegalArgumentException() {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Entity invalidEntity = new Entity("Lead", advertiserKey);

    new Campaign(invalidEntity);
  }

  @Test
  public void campaignGenerateKey_withValidCampaignParams_producesCorrespondingKey() {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Campaign campaign = new Campaign(advertiserKey, 1, "campaign1");

    Key generatedKey = Campaign.generateKey(campaign.getAdvertiserKey(), campaign.getCampaignId());

    assertEquals(campaign.asEntity().getKey(), generatedKey);
  }

  @Test
  public void campaignGenerateKey_withDifferentParent_generatesDifferentKey()
      throws Exception {
    Key advertiserKey1 = KeyFactory.createKey(Advertiser.KIND_NAME, "key1");
    Key advertiserKey2 = KeyFactory.createKey(Advertiser.KIND_NAME, "key2");
    long campaignId = 12345;

    Key campaignKey1 = Campaign.generateKey(advertiserKey1, campaignId);
    Key campaignKey2 = Campaign.generateKey(advertiserKey2, campaignId);

    assertNotEquals(campaignKey1, campaignKey2);
  }

  @Test
  public void campaignGenerateKey_withDifferentId_generatesDifferentKey()
      throws Exception {
    Key advertiserKey = KeyFactory.createKey(Advertiser.KIND_NAME, "key1");
    long campaignId1 = 1;
    long campaignId2 = 2;

    Key campaignKey1 = Campaign.generateKey(advertiserKey, campaignId1);
    Key campaignKey2 = Campaign.generateKey(advertiserKey, campaignId2);

    assertNotEquals(campaignKey1, campaignKey2);
  }
}
