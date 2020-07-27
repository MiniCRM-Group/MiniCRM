package com.google.minicrm.servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.gson.Gson;
import com.google.minicrm.data.Advertiser;
import com.google.minicrm.data.Campaign;
import com.google.minicrm.data.Form;
import com.google.minicrm.data.Lead;
import com.google.minicrm.utils.DatastoreUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides Unit Tests for the WebhookServlet servlet at endpoint api/webhook.
 */
@RunWith(JUnit4.class)
public final class WebhookServletTest {

  private static final File leadFile = new File("src/test/resources/lead1.txt");
  private static final String TEST_USER_ID = "testUserId";
  private static final WebhookServlet webhookServlet = new WebhookServlet();
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
  private final User testUser = new User("email", "authDomain", TEST_USER_ID);

  private HttpServletRequest request;
  private HttpServletResponse response;
  private Advertiser testAdvertiser;

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
  public void webhookServletGetRequest_withExistingAdvertiser_returnsCorrectWebhookAndGoogleKeyResponse()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("aSecretKey");
    //store the advertiser beforehand
    DatastoreUtil.put(testAdvertiser);
    //setup the writer to get the response back
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
    //setup request fields for generating the webhook
    when(request.getScheme()).thenReturn("https");
    when(request.getServerName()).thenReturn("miniCrm.com");
    when(request.getServerPort()).thenReturn(4200);

    webhookServlet.doGet(request, response);

    writer.flush();
    Map<String, String> getResponse = gson.fromJson(stringWriter.toString(), Map.class);
    assertEquals(testAdvertiser.getGoogleKey(), getResponse.get("googleKey"));
    assertEquals(testAdvertiser.generateWebhook(request), getResponse.get("webhookUrl"));
  }

  @Test
  public void webhookServletGetRequest_withNewAdvertiser_returnsCorrectWebhookAndGoogleKeyResponse()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    //setup the writer to get the response back
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
    //setup request fields for generating the webhook
    when(request.getScheme()).thenReturn("https");
    when(request.getServerName()).thenReturn("miniCrm.com");
    when(request.getServerPort()).thenReturn(4200);

    webhookServlet.doGet(request, response);

    writer.flush();
    Map<String, String> getResponse = gson.fromJson(stringWriter.toString(), Map.class);
    assertEquals(testAdvertiser.generateWebhook(request), getResponse.get("webhookUrl"));
  }

  @Test
  public void webhookServletPostRequest_withValidRequest_storesLeadAndFormAndCampaign()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("aSecretKey");
    DatastoreUtil.put(testAdvertiser);
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));
    Key advertiserKey = Advertiser.generateKey(testAdvertiser.getUser());
    String advertiserKeyString = KeyFactory.keyToString(advertiserKey);
    when(request.getParameter("id")).thenReturn(advertiserKeyString);

    webhookServlet.doPost(request, response);

    //assert that the lead exists and that it is equal
    Lead lead = Lead.fromReader(new FileReader(leadFile), advertiserKey);
    Key leadKey = lead.asEntity().getKey();
    assertEquals(lead, new Lead(DatastoreUtil.get(leadKey)));
    //assert that the form exists and that the id is equal
    long formId = lead.getFormId();
    Form form = new Form(advertiserKey, formId, Long.toString(formId));
    Key formKey = form.asEntity().getKey();
    assertEquals(form, new Form(DatastoreUtil.get(formKey)));
    //assert that the campaign exists and that the id is equal
    long campaignId = lead.getCampaignId();
    Campaign campaign = new Campaign(advertiserKey, campaignId, Long.toString(campaignId));
    Key campaignKey = campaign.asEntity().getKey();
    assertEquals(campaign, new Campaign(DatastoreUtil.get(campaignKey)));
  }

  @Test
  public void webhookServletPostRequest_withValidRequestAndExistingFormAndCampaign_storesLeadWithoutModifyingFormAndCampaign()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("aSecretKey");
    DatastoreUtil.put(testAdvertiser);
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));
    Key advertiserKey = Advertiser.generateKey(testAdvertiser.getUser());
    String advertiserKeyString = KeyFactory.keyToString(advertiserKey);
    when(request.getParameter("id")).thenReturn(advertiserKeyString);
    //store the form and campaign prior to the lead
    Lead lead = Lead.fromReader(new FileReader(leadFile), advertiserKey);
    long formId = lead.getFormId();
    Form form = new Form(advertiserKey, formId, "alreadyExistingForm");
    DatastoreUtil.put(form);
    long campaignId = lead.getCampaignId();
    Campaign campaign = new Campaign(advertiserKey, campaignId, "alreadyExistingCampaign");
    DatastoreUtil.put(campaign);

    webhookServlet.doPost(request, response);

    //assert that the lead exists and that it is equal
    Key leadKey = lead.asEntity().getKey();
    assertEquals(lead, new Lead(DatastoreUtil.get(leadKey)));
    //assert that the form was not changed
    Key formKey = form.asEntity().getKey();
    assertEquals(form, new Form(DatastoreUtil.get(formKey)));
    //assert that the campaign was not changed
    Key campaignKey = campaign.asEntity().getKey();
    assertEquals(campaign, new Campaign(DatastoreUtil.get(campaignKey)));
  }

  @Test
  public void webhookServletPostRequest_withIncorrectGoogleKey_throws401Unauthorized()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("notASecretKey");
    DatastoreUtil.put(testAdvertiser);
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));
    Key advertiserKey = Advertiser.generateKey(testAdvertiser.getUser());
    String advertiserKeyString = KeyFactory.keyToString(advertiserKey);
    when(request.getParameter("id")).thenReturn(advertiserKeyString);

    webhookServlet.doPost(request, response);

    verify(response).sendError(eq(401), anyString());
  }

  @Test
  public void webhookServletPostRequest_withNoIdParameter_throws404NotFound()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("aSecretKey");
    DatastoreUtil.put(testAdvertiser);
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));
    when(request.getParameter("id")).thenReturn("");

    webhookServlet.doPost(request, response);

    //assert that the response threw a 404 not found error
    verify(response).sendError(eq(404), anyString());
  }

  @Test
  public void webhookServletPostRequest_withBlankIdParameter_throws404NotFound()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("aSecretKey");
    DatastoreUtil.put(testAdvertiser);
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));

    webhookServlet.doPost(request, response);

    //assert that the response threw a 404 not found error
    verify(response).sendError(eq(404), anyString());
  }

  @Test
  public void webhookServletPostRequest_withIncorrectIdParameter_throws404NotFound()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("aSecretKey");
    DatastoreUtil.put(testAdvertiser);
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));
    User nonexistentUser = new User("notEmail", "notAuthDomain", "notUser");
    Key nonexistentAdvertiserKey = Advertiser.generateKey(nonexistentUser);
    String nonexistentAdvertiserKeyString = KeyFactory.keyToString(nonexistentAdvertiserKey);
    when(request.getParameter("id")).thenReturn(nonexistentAdvertiserKeyString);

    webhookServlet.doPost(request, response);

    verify(response).sendError(eq(404), anyString());
  }

  @Test
  public void webhookServletPostRequest_withRepeatedValidRequests_throws409Conflict()
      throws Exception {

    testAdvertiser = new Advertiser(testUser);
    testAdvertiser.setGoogleKey("aSecretKey");
    DatastoreUtil.put(testAdvertiser);
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));
    Key advertiserKey = Advertiser.generateKey(testAdvertiser.getUser());
    String advertiserKeyString = KeyFactory.keyToString(advertiserKey);
    when(request.getParameter("id")).thenReturn(advertiserKeyString);
    //do first request
    webhookServlet.doPost(request, response);
    //refresh reader
    when(request.getReader()).thenReturn(new BufferedReader(new FileReader(leadFile)));
    HttpServletResponse response2 = mock(HttpServletResponse.class);

    webhookServlet.doPost(request, response2);

    verify(response2).sendError(eq(409), anyString());
  }


}
