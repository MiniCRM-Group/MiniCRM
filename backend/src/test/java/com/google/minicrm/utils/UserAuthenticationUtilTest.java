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

package com.google.minicrm.utils;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.minicrm.data.Advertiser;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public final class UserAuthenticationUtilTest {

  private static final String TEST_USER_ID = "testUserId";
  private static Map<String, Object> envAttributes;

  static {
    envAttributes = new HashMap<>();
    envAttributes.put("com.google.appengine.api.users.UserService.user_id_key", TEST_USER_ID);
  }

  private static final LocalServiceTestHelper loggedInHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
          .setEnvIsLoggedIn(true)
          .setEnvEmail("email")
          .setEnvAuthDomain("authDomain")
          .setEnvAttributes(envAttributes);
  private static final LocalServiceTestHelper loggedOutHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig()).setEnvIsLoggedIn(false);

  @Test
  public void userAuthUtilIsAuthenticated_whenNotLoggedIn_returnsFalse() throws Exception {
    loggedOutHelper.setUp();

    assertFalse(UserAuthenticationUtil.isAuthenticated());

    loggedOutHelper.tearDown();
  }

  @Test
  public void userAuthUtilIsAuthenticated_whenLoggedIn_returnsTrue() throws Exception {
    loggedInHelper.setUp();

    assertTrue(UserAuthenticationUtil.isAuthenticated());

    loggedInHelper.tearDown();
  }

  @Test
  public void userAuthUntilIsAuthenticated_whenLoggedInAsNewUser_createsNewAdvertiserAndReturnsTrue()
      throws Exception {
    loggedInHelper.setUp();
    User testUser = new User("email", "authDomain", TEST_USER_ID);
    Key expectedKey = Advertiser.generateKey(testUser);

    assertTrue(UserAuthenticationUtil.isAuthenticated());
    assertTrue(DatastoreUtil.exists(expectedKey));
  }
}
