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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.com.google.gson.reflect.TypeToken;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Form;
import com.google.minicrm.utils.DatastoreUtil;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
 * Provides Unit Tests for the FormsServlet servlet at endpoint api/forms.
 */
@RunWith(JUnit4.class)
public final class FormsServletTest {

  private static final String TEST_USER_ID = "testUserId";
  private static final FormsServlet formsServlet = new FormsServlet();
  private static Map<String, Object> envAttributes;
  static {
    envAttributes = new HashMap<>();
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

  private Form form1;
  private Form form2;
  private Form form3;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void formsServlet_getRequest_returnsAllFormsInTimeOrder() throws Exception {
    slowSeedForms();
    Form[] returnedForms = getForms();
    Form[] expectedForms = {form3, form2, form1};
    assertArrayEquals(expectedForms, returnedForms);
  }

  @Test
  public void formsServlet_putRequest_json_successfullyRenamesAndReturns204() throws Exception {
    seedForms();
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new FormsPutRequest("2", "newName").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));
    formsServlet.doPut(request, response);

    //get the new forms
    List<Form> returnedForms = Arrays.asList(getForms());
    form2.setFormName("newName");
    List<Form> expectedForms = new ArrayList<>(3);
    expectedForms.add(form1);
    expectedForms.add(form2);
    expectedForms.add(form3);
    //assert all forms are the same regardless of order in list
    assertTrue(expectedForms.size() == returnedForms.size());
    assertTrue(expectedForms.containsAll(returnedForms) && expectedForms.containsAll(returnedForms));

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(Mockito.anyInt(), Mockito.anyString());
    verify(response, never()).sendError(Mockito.anyInt());
  }

  @Test
  public void formsServlet_putRequest_urlEncoded_successfullyRenamesAndReturns204()
      throws Exception {
    seedForms();
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("formId")).thenReturn("2");
    when(request.getParameter("formName")).thenReturn("newName");
    formsServlet.doPut(request, response);

    //get the new forms
    List<Form> returnedForms = Arrays.asList(getForms());
    form2.setFormName("newName");
    List<Form> expectedForms = new ArrayList<>(3);
    expectedForms.add(form1);
    expectedForms.add(form2);
    expectedForms.add(form3);
    //assert all forms are the same regardless of order in list
    assertTrue(expectedForms.size() == returnedForms.size());
    assertTrue(expectedForms.containsAll(returnedForms) && expectedForms.containsAll(returnedForms));


    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(Mockito.anyInt(), Mockito.anyString());
    verify(response, never()).sendError(Mockito.anyInt());
  }

  @Test
  public void formsServlet_putRequest_urlEncoded_throws400WithNoFields() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    formsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), Mockito.anyString()); //we want an error msg
  }

  @Test
  public void formsServlet_putRequest_urlEncoded_throws400WithNoBody() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    BufferedReader br = new BufferedReader(new StringReader(""));
    when(request.getReader()).thenReturn(br);
    formsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), Mockito.anyString()); //we want an error msg
  }

  @Test
  public void formsServlet_putRequest_urlEncoded_throws400BlankParameter() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("formId")).thenReturn("");
    when(request.getParameter("formName")).thenReturn("name");
    formsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), Mockito.anyString()); //we want an error msg
  }

  @Test
  public void formsServlet_putRequest_json_throws400BlankParameter() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new FormsPutRequest("", "newName").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));
    formsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), Mockito.anyString()); //we want an error msg
  }

  @Test
  public void formsServlet_putRequest_throws415OnInvalidContentType() throws Exception {
    when(request.getContentType()).thenReturn("multipart/form-data;");
    formsServlet.doPut(request, response);

    //verify the error with a message
    verify(response).sendError(eq(415), Mockito.anyString());
  }

  /**
   * Seeds forms but with one second delays inbetween because datastore only has a 1 second
   * resolution for date objects
   * @throws Exception
   */
  private void slowSeedForms() throws Exception {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key parentKey = Advertiser.generateKey(testUser);
    form1 = new Form(parentKey, 1, "form1");
    TimeUnit.SECONDS.sleep(1); //datastore timestamp only has 1 second precision
    form2 = new Form(parentKey, 2, "form2");
    TimeUnit.SECONDS.sleep(1);
    form3 = new Form(parentKey, 3, "form3");
    TimeUnit.SECONDS.sleep(1);
    DatastoreUtil.put(form1);
    DatastoreUtil.put(form2);
    DatastoreUtil.put(form3);
  }

  /**
   * Initializes instance variables form1, form2, and form3 and stores them in datastore.
   */
  private void seedForms() {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key parentKey = Advertiser.generateKey(testUser);
    form1 = new Form(parentKey, 1, "form1");
    form2 = new Form(parentKey, 2, "form2");
    form3 = new Form(parentKey, 3, "form3");
    DatastoreUtil.put(form1);
    DatastoreUtil.put(form2);
    DatastoreUtil.put(form3);
  }
  /**
   * Gets the forms using the FormsServlet GET method
   *
   * @return the forms currently in the datastore
   * @throws Exception if any error occurs
   */
  private Form[] getForms() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    formsServlet.doGet(request, response);

    writer.flush();

    Type mapStrToFormArrType = new TypeToken<Map<String, Form[]>>() {
    }.getType();
    Map<String, Form[]> getResponse = gson.fromJson(stringWriter.toString(), mapStrToFormArrType);
    return getResponse.get("forms");
  }

  private class FormsPutRequest {

    private String formId;
    private String formName;

    FormsPutRequest(String formId, String formName) {
      this.formId = formId;
      this.formName = formName;
    }

    public String toJson() {
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}
