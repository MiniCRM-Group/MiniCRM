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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Provides testing when the user is not logged in. Verifies that all accesses to user data is
 * correctly blocked and returns a 401 Unauthorized error message.
 */
@RunWith(JUnit4.class)
public final class UnauthenticatedRequestsTest {

  private static final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
          new LocalDatastoreServiceTestConfig(),
          new LocalUserServiceTestConfig())
          .setEnvIsLoggedIn(false);
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FormsServlet formsServlet = new FormsServlet();
  private LeadsServlet leadsServlet = new LeadsServlet();
  private WebhookServlet webhookServlet = new WebhookServlet();

  @Before
  public void setUp() {
    helper.setUp();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void formsServlet_unauthorized_getRequest_throws401() throws Exception {
    formsServlet.doGet(request, response);

    //verify error code
    verify(response).sendError(eq(401), anyString());
  }

  @Test
  public void formsServlet_unauthorized_putRequest_throws401() throws Exception {
    formsServlet.doPut(request, response);

    //verify error code
    verify(response).sendError(eq(401), anyString());
  }

  @Test
  public void leadsServlet_unauthorized_getRequest_throws401() throws Exception {
    leadsServlet.doGet(request, response);

    //verify error code
    verify(response).sendError(eq(401), anyString());
  }

  @Test
  public void leadsServlet_unauthorized_putRequest_throws401() throws Exception {
    leadsServlet.doGet(request, response);

    //verify error code
    verify(response).sendError(eq(401), anyString());
  }

  @Test
  public void webhookServlet_unauthorized_getRequest_throws401() throws Exception {
    webhookServlet.doGet(request, response);

    //verify error code
    verify(response).sendError(eq(401), anyString());
  }

}
