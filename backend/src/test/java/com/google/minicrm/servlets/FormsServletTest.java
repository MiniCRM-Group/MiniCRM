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

package com.google.minicrm.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.com.google.gson.reflect.TypeToken;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.gson.Gson;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Form;
import com.google.minicrm.utils.DatastoreUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;

/**
 * Provides Unit Tests for the FormsServlet servlet at endpoint api/forms.
 */
@RunWith(JUnit4.class)
public final class FormsServletTest {

  private static final String TEST_FORM_KIND_NAME = "TestForm";
  private static final String TEST_USER_ID = "testUserId";
  private static final FormsServlet formsServlet = new FormsServlet();
  private static Map<String, Object> envAttributes;
  static {
    envAttributes = new HashMap<String, Object>();
    envAttributes.put("com.google.appengine.api.users.UserService.user_id_key", TEST_USER_ID);
  }
  private static final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
          new LocalDatastoreServiceTestConfig(),
          new LocalUserServiceTestConfig())
      .setEnvIsLoggedIn(true)
      .setEnvEmail("test@test.com")
      .setEnvAuthDomain("testAuthDomain")
      .setEnvAttributes(envAttributes);
  private final Gson gson = new Gson();

  private Key parentKey;
  private Form form1;
  private Form form2;
  private Form form3;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    //seed initial data
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    parentKey = Advertiser.generateKey(testUser);
    form1 = new Form(parentKey, 1, "form1" );
    TimeUnit.SECONDS.sleep(1); //timestamp only has 1 second precision
    form2 = new Form(parentKey, 2, "form2");
    TimeUnit.SECONDS.sleep(1);
    form3 = new Form(parentKey, 3, "form3");
    TimeUnit.SECONDS.sleep(1);
    DatastoreUtil.put(form1);
    DatastoreUtil.put(form2);
    DatastoreUtil.put(form3);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void forms_getRequest_returnsAllFormsInTimeOrder() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    formsServlet.doGet(request, response);

    writer.flush();

    Type mapStrToFormArrType = new TypeToken<Map<String, Form[]>>(){}.getType();
    System.out.println(stringWriter.toString());
    Map<String, Form[]> getResponse = gson.fromJson(stringWriter.toString(), mapStrToFormArrType);
    Form[] returnedForms = getResponse.get("forms");
    Form[] expectedForms = {form3, form2, form1};
    assertArrayEquals(expectedForms, returnedForms);
  }
}
