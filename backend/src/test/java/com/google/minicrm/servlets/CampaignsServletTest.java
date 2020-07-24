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
import com.google.minicrm.data.Campaign;
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
 * Provides Unit Tests for the CampaignsServlet servlet at endpoint api/campaigns.
 */
@RunWith(JUnit4.class)
public final class CampaignsServletTest {

  private static final String TEST_USER_ID = "testUserId";
  private static final CampaignsServlet campaignsServlet = new CampaignsServlet();
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

  private Campaign campaign1;
  private Campaign campaign2;
  private Campaign campaign3;
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
  public void campaignsServletGetRequest_returnsAllCampaigns() throws Exception {
    seedCampaigns();

    List<Campaign> returnedCampaigns = getCampaigns();

    List<Campaign> expectedCampaigns = Arrays.asList(campaign1, campaign2, campaign3);
    assertListEqualsIgnoreOrder(expectedCampaigns, returnedCampaigns);
  }

  @Test
  public void campaignsServletPutRequest_validJsonRequest_successfullyRenamesAndReturns204()
      throws Exception {

    seedCampaigns();
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new CampaignsPutRequest("2", "newName").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    campaignsServlet.doPut(request, response);

    List<Campaign> returnedCampaigns = getCampaigns();
    campaign2.setCampaignName("newName");
    List<Campaign> expectedCampaigns = Arrays.asList(campaign1, campaign2, campaign3);
    assertListEqualsIgnoreOrder(expectedCampaigns, returnedCampaigns);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void campaignsServletPutRequest_validUrlEncodedRequest_successfullyRenamesAndReturns204()
      throws Exception {

    seedCampaigns();
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("campaignId")).thenReturn("2");
    when(request.getParameter("campaignName")).thenReturn("newName");

    campaignsServlet.doPut(request, response);

    //get the new campaigns
    List<Campaign> returnedCampaigns = getCampaigns();
    campaign2.setCampaignName("newName");
    List<Campaign> expectedCampaigns = new ArrayList<>();
    expectedCampaigns.add(campaign1);
    expectedCampaigns.add(campaign2);
    expectedCampaigns.add(campaign3);
    //assert all campaigns are the same regardless of order in list
    assertListEqualsIgnoreOrder(expectedCampaigns, returnedCampaigns);

    //verify response codes
    verify(response).setStatus(204);
    verify(response, never()).sendError(anyInt(), anyString());
    verify(response, never()).sendError(anyInt());
  }

  @Test
  public void campaignsServletPutRequest_urlEncodedWithNoFields_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_jsonWithNoBody_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    BufferedReader br = new BufferedReader(new StringReader(""));
    when(request.getReader()).thenReturn(br);

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_urlEncodedWithBlankId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("campaignId")).thenReturn("");
    when(request.getParameter("campaignName")).thenReturn("name");

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_jsonWithBlankId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new CampaignsPutRequest("", "newName").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_urlEncodedWithMissingId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("campaignName")).thenReturn("name");

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_jsonWithMissingId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new CampaignsPutRequest(null, "newName").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_urlEncodedWithBlankName_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("campaignId")).thenReturn("1234");
    when(request.getParameter("campaignName")).thenReturn("");

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_jsonWithBlankName_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new CampaignsPutRequest("1234", "").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_urlEncodedWithMissingName_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("campaignId")).thenReturn("1234");

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_jsonWithMissingName_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new CampaignsPutRequest("1234", null).toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_urlEncodedWithNonIntegerCampaignId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("campaignId")).thenReturn("1234.5");
    when(request.getParameter("campaignName")).thenReturn("newNAme");

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_jsonWithNonIntegerCampaignId_throws400() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new CampaignsPutRequest("1234.5", "newName").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(400), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_urlEncodedWithNonexistentCampaignId_throws404() throws Exception {
    when(request.getContentType()).thenReturn("application/x-www-form-urlencoded;");
    when(request.getParameter("campaignId")).thenReturn("1234");
    when(request.getParameter("campaignName")).thenReturn("newNAme");

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(404), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_jsonWithNonexistentCampaignId_throws404() throws Exception {
    when(request.getContentType()).thenReturn("application/json;");
    Reader reader = new StringReader(new CampaignsPutRequest("1234", "newName").toJson());
    when(request.getReader()).thenReturn(new BufferedReader(reader));

    campaignsServlet.doPut(request, response);

    //verify the response has an error
    verify(response).sendError(eq(404), anyString()); //we want an error msg
  }

  @Test
  public void campaignsServletPutRequest_withInvalidContentType_throws415() throws Exception {
    when(request.getContentType()).thenReturn("multipart/form-data;");

    campaignsServlet.doPut(request, response);

    //verify the error with a message
    verify(response).sendError(eq(415), anyString());
  }

  /**
   * Asserts that two lists are equal while ignoring order. Assumes no duplicates exist.
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
   * Initializes instance variables campaign1, campaign2, and campaign3 and stores them in datastore.
   */
  private void seedCampaigns() {
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key parentKey = Advertiser.generateKey(testUser);
    campaign1 = new Campaign(parentKey, 1, "campaign1");
    campaign2 = new Campaign(parentKey, 2, "campaign2");
    campaign3 = new Campaign(parentKey, 3, "campaign3");
    DatastoreUtil.put(campaign1);
    DatastoreUtil.put(campaign2);
    DatastoreUtil.put(campaign3);
  }

  /**
   * Gets the campaigns using the CampaignsServlet GET method
   *
   * @return the campaigns currently in the datastore
   * @throws Exception if any error occurs
   */
  private List<Campaign> getCampaigns() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    campaignsServlet.doGet(request, response);

    writer.flush();

    Type mapStrToCampaignArrType = new TypeToken<Map<String, Campaign[]>>() {
    }.getType();
    Map<String, Campaign[]> getResponse = gson.fromJson(stringWriter.toString(), mapStrToCampaignArrType);
    return Arrays.asList(getResponse.get("campaigns"));
  }

  private class CampaignsPutRequest {

    private String campaignId;
    private String campaignName;

    CampaignsPutRequest(String campaignId, String campaignName) {
      this.campaignId = campaignId;
      this.campaignName = campaignName;
    }

    public String toJson() {
      Gson gson = new GsonBuilder().disableHtmlEscaping().create();
      return gson.toJson(this);
    }
  }
}
