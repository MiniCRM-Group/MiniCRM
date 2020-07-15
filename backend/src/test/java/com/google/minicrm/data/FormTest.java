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
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides Unit tests for the Form data object.
 */
@RunWith(JUnit4.class)
public final class FormTest {

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
  public void newForm_fromValidFormAsEntity_producesEquivalentValidForm() throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Form form = new Form(advertiserKey, 1, "form1");

    Form convertedForm = new Form(form.asEntity());

    assertEquals(form, convertedForm);
  }

  @Test
  public void formGenerateKey_withDifferentParent_generatesDifferentKey()
      throws Exception {
    Key advertiserKey1 = KeyFactory.createKey(Advertiser.KIND_NAME, "key1");
    Key advertiserKey2 = KeyFactory.createKey(Advertiser.KIND_NAME, "key2");
    long formId = 12345;

    Key formKey1 = Form.generateKey(advertiserKey1, formId);
    Key formKey2 = Form.generateKey(advertiserKey2, formId);

    assertNotEquals(formKey1, formKey2);
  }

  @Test
  public void form_generateKey_withDifferentId_generatesDifferentKey()
    throws Exception {
    Key advertiserKey = KeyFactory.createKey(Advertiser.KIND_NAME, "key1");
    long formId1 = 1;
    long formId2 = 2;

    Key formKey1 = Form.generateKey(advertiserKey, formId1);
    Key formKey2 = Form.generateKey(advertiserKey, formId2);

    assertNotEquals(formKey1, formKey2);
  }

}
