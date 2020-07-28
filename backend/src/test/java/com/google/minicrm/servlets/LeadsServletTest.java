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

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.google.minicrm.data.Lead;
import com.google.minicrm.data.LeadStatus;
import com.google.minicrm.utils.DatastoreUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides Unit Tests for the LeadsServlet servlet at endpoint api/leads.
 */
@RunWith(JUnit4.class)
public final class LeadsServletTest {

  private static final File leadFile1 = new File("src/test/resources/lead1.txt");
  private static final File leadFile2 = new File("src/test/resources/lead2.txt");
  private static final File leadFile3 = new File("src/test/resources/lead3.txt");

  private static final String TEST_USER_ID = "testUserId";
  private static final LeadsServlet leadsServlet = new LeadsServlet();
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

  private Lead lead1;
  private Lead lead2;
  private Lead lead3;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    seedLeads();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  /**
   * Initializes instance variables lead1, lead2, and lead3 and stores them in datastore.
   */
  private void seedLeads() throws IOException {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key advertiserKey = Advertiser.generateKey(testUser);
    Reader reader1 = new FileReader(leadFile1);
    Reader reader2 = new FileReader(leadFile2);
    Reader reader3 = new FileReader(leadFile3);

    lead1 = Lead.fromReader(reader1, advertiserKey);
    lead1.setStatus(LeadStatus.WORKING);
    lead2 = Lead.fromReader(reader2, advertiserKey);
    lead2.setNotes("old notes.");
    lead3 = Lead.fromReader(reader3, advertiserKey);
    DatastoreUtil.put(lead1);
    DatastoreUtil.put(lead2);
    DatastoreUtil.put(lead3);
  }

  @Test
  public void leadsServletGetRequest_returnsAllLeads() throws Exception {
    List<Lead> returnedLeads = getLeads();

    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);
  }

  @Test
  public void leadsServletPutRequest_validJsonRequest_successfullyUpdatesAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new LeadsPutRequest("2", "OPEN", "new note.").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    List<Lead> returnedLeads = getLeads();
    lead2.setStatus(LeadStatus.OPEN);
    lead2.setNotes("new note.");
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validUrlEncodedRequest_successfullyUpdatesAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("2");
    when(request.getParameter("status")).thenReturn("OPEN");
    when(request.getParameter("notes")).thenReturn("new note.");

    leadsServlet.doPut(request, response);

    //get the new leads
    List<Lead> returnedLeads = getLeads();
    lead2.setStatus(LeadStatus.OPEN);
    lead2.setNotes("new note.");
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    //assert all leads are the same regardless of order in list
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validJsonRequestWithoutStatus_successfullyUpdatesAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new LeadsPutRequest("2", null, "new note.").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    List<Lead> returnedLeads = getLeads();
    lead2.setNotes("new note.");
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validUrlEncodedRequestWithoutStatus_successfullyUpdatesAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("2");
    when(request.getParameter("notes")).thenReturn("new note.");

    leadsServlet.doPut(request, response);

    //get the new leads
    List<Lead> returnedLeads = getLeads();
    lead2.setNotes("new note.");
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    //assert all leads are the same regardless of order in list
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validJsonRequestWithoutNotes_successfullyUpdatesAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new LeadsPutRequest("2", "OPEN", null).toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    List<Lead> returnedLeads = getLeads();
    lead2.setStatus(LeadStatus.OPEN);
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validUrlEncodedRequestWithoutNotes_successfullyUpdatesAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("2");
    when(request.getParameter("status")).thenReturn("OPEN");

    leadsServlet.doPut(request, response);

    //get the new leads
    List<Lead> returnedLeads = getLeads();
    lead2.setStatus(LeadStatus.OPEN);
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    //assert all leads are the same regardless of order in list
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validJsonRequestWithBlankNotes_successfullyUpdatesAndReturns204()
      throws Exception {

    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new LeadsPutRequest("2", "OPEN", "").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    List<Lead> returnedLeads = getLeads();
    lead2.setStatus(LeadStatus.OPEN);
    lead2.setNotes("");
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validUrlEncodedRequestWithBlankNotes_successfullyUpdatesAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("2");
    when(request.getParameter("status")).thenReturn("OPEN");
    when(request.getParameter("notes")).thenReturn("");

    leadsServlet.doPut(request, response);

    //get the new leads
    List<Lead> returnedLeads = getLeads();
    lead2.setStatus(LeadStatus.OPEN);
    lead2.setNotes("");
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    //assert all leads are the same regardless of order in list
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validJsonRequestWithOnlyId_doesNotChangeDataAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new LeadsPutRequest("2", null, null).toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    List<Lead> returnedLeads = getLeads();
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_validUrlEncodedRequestWithOnlyId_doesNotChangeDataAndReturns204()
      throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("2");

    leadsServlet.doPut(request, response);

    //get the new leads
    List<Lead> returnedLeads = getLeads();
    List<Lead> expectedLeads = Arrays.asList(lead1, lead2, lead3);
    //assert all leads are the same regardless of order in list
    assertListEqualsIgnoreOrder(expectedLeads, returnedLeads);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void leadsServletPutRequest_urlEncodedWithNoFields_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_jsonWithNoBody_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    BufferedReader br = new BufferedReader(new StringReader(""));
    when(request.getReader()).thenReturn(br);

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_urlEncodedWithBlankId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("");
    when(request.getParameter("status")).thenReturn("OPEN");
    when(request.getParameter("notes")).thenReturn("new notes.");

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_jsonWithBlankId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new LeadsPutRequest("", "OPEN", "new notes.").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_urlEncodedWithMissingId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("status")).thenReturn("OPEN");
    when(request.getParameter("notes")).thenReturn("new notes.");

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_jsonWithMissingId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new LeadsPutRequest(null, "OPEN", "new notes.").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_urlEncodedWithInvalidStatus_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("1");
    when(request.getParameter("status")).thenReturn("I am not valid.");
    when(request.getParameter("notes")).thenReturn("new notes.");

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_jsonWithInvalidStatus_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(
        new LeadsPutRequest("1", "I am not valid.", "new notes").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_urlEncodedWithNonexistentLeadId_throws404() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("leadId")).thenReturn("nonexistentLead");
    when(request.getParameter("status")).thenReturn("OPEN");
    when(request.getParameter("notes")).thenReturn("new notes.");

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(404), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_jsonWithNonexistentLeadId_throws404() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(
        new LeadsPutRequest("nonexistentLead", "OPEN", "new notes.").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    leadsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(404), anyString()); //we want an error msg
  }

  @Test
  public void leadsServletPutRequest_withInvalidContentType_throws415() throws Exception {
    when(request.getContentType()).thenReturn("multipart/form-data;");

    leadsServlet.doPut(request, response);

    //verify the error with a message
    verify(response).sendError(eq(415), anyString());
  }

  /**
   * Asserts that two lists are equal while ignoring order. Assumes no duplicates exist.
   *
   * @param expectedList the expect list values
   * @param actualList   the actual list to compare to the expectedList
   * @param <T>          the type of the lists
   */
  private <T> void assertListEqualsIgnoreOrder(List<T> expectedList, List<T> actualList) {
    assertTrue(expectedList.size() == actualList.size());
    assertTrue(expectedList.containsAll(actualList) &&
        actualList.containsAll(expectedList));
  }

  /**
   * Gets the leads using the LeadsServlet GET method
   *
   * @return the leads currently in the datastore
   * @throws Exception if any error occurs
   */
  private List<Lead> getLeads() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    leadsServlet.doGet(request, response);

    writer.flush();

    Type mapStrToLeadArrType = new TypeToken<Map<String, Lead[]>>() {
    }.getType();
    Map<String, Lead[]> getResponse = gson.fromJson(stringWriter.toString(), mapStrToLeadArrType);
    return Arrays.asList(getResponse.get("leads"));
  }

  private class LeadsPutRequest {

    private String leadId;
    private String status;
    private String notes;

    LeadsPutRequest(String leadId, String status, String notes) {
      this.leadId = leadId;
      this.status = status;
      this.notes = notes;
    }

    public String toJson() {
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}
