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
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides Unit tests for the Lead data object.
 */
@RunWith(JUnit4.class)
public final class LeadTest {

  private static final File leadFile = new File("src/test/resources/lead1.txt");
  private static final String TEST_USER_ID = "testUserId";
  private static final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown(){
    helper.tearDown();
  }

  @Test
  public void leadFromReader_fromValidLeadJson_producesEquivalentValidLead() throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Reader reader = new FileReader(leadFile);

    Lead lead = Lead.fromReader(reader, advertiserKey);

    //assert all properties are correct
    assertEquals("1", lead.getLeadId());
    assertEquals("1.0", lead.getApiVersion());
    assertEquals(100, lead.getFormId());
    assertEquals(200, lead.getCampaignId());
    assertEquals("aSecretKey", lead.getGoogleKey());
    assertEquals(true, lead.isTest());
    assertEquals("123abc456def",lead.getGclId());
    assertEquals(300, lead.getAdgroupId());
    assertEquals(400, lead.getCreativeId());
    assertEquals("Alex Kim", lead.getColumnData("FULL_NAME"));
    assertEquals("+16505550123", lead.getColumnData("PHONE_NUMBER"));
    assertEquals("alex@example.com", lead.getColumnData("EMAIL"));
    assertEquals("UnitedStates", lead.getColumnData("COUNTRY"));
    assertEquals("123.456.789-00", lead.getColumnData("NATIONAL_ID_CPF_BR"));
    assertEquals("Nissan", lead.getColumnData("VEHICLE_TYPE"));
  }

  @Test
  public void leadFromReader_fromValidLeadJson_correctlyInitializesDefaultAdvertiserDefinedFields0()
      throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Reader reader = new FileReader(leadFile);

    Lead lead = Lead.fromReader(reader, advertiserKey);

    //assert all advertiser defined properties are assigned to defaults
    assertEquals("", lead.getNotes());
    assertEquals(LeadStatus.NEW, lead.getStatus());
  }

  @Test
  public void newLead_fromValidLeadAsEntity_producesEquivalentValidLead() throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Reader reader = new FileReader(leadFile);
    Lead lead = Lead.fromReader(reader, advertiserKey);
    Entity leadEntity = lead.asEntity();
    //mimic datastore (integers converted to long)
    leadEntity.setProperty("status", Long.valueOf(lead.getStatus().ordinal()));

    Lead convertedLead = new Lead(leadEntity);

    assertEquals(lead, convertedLead);
  }

  @Test(expected = IllegalArgumentException.class)
  public void newLead_fromInvalidEntityKind_throwsIllegalArgumentException() throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Entity invalidEntity = new Entity("NotLead", advertiserKey);

    new Lead(invalidEntity);
  }

  @Test
  public void leadGenerateKey_withValidLeadParams_producesCorrespondingKey() throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Reader reader = new FileReader(leadFile);
    Lead lead = Lead.fromReader(reader, advertiserKey);

    Key generatedKey = Lead.generateKey(lead.getAdvertiserKey(), lead.getLeadId());

    assertEquals(lead.asEntity().getKey(), generatedKey);
  }

  @Test
  public void leadGenerateKey_withDifferentParent_generatesDifferentKey()
      throws Exception {
    Key advertiserKey1 = KeyFactory.createKey(Advertiser.KIND_NAME, "key1");
    Key advertiserKey2 = KeyFactory.createKey(Advertiser.KIND_NAME, "key2");
    String leadId = "12345";

    Key leadKey1 = Lead.generateKey(advertiserKey1, leadId);
    Key leadKey2 = Lead.generateKey(advertiserKey2, leadId);

    assertNotEquals(leadKey1, leadKey2);
  }

  @Test
  public void leadGenerateKey_withDifferentId_generatesDifferentKey()
      throws Exception {
    Key advertiserKey = KeyFactory.createKey(Advertiser.KIND_NAME, "key1");

    Key leadKey1 = Lead.generateKey(advertiserKey, "id1");
    Key leadKey2 = Lead.generateKey(advertiserKey, "id2");

    assertNotEquals(leadKey1, leadKey2);
  }

}
