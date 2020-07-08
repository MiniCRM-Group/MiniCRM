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

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.minicrm.data.Advertiser;
import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;


/**
 * Handles storing and reading Advertiser entities from the datastore.
 * Advertiser refers to the entity in the datastore, representing a User provided by
 * Google Authentication using our application.
 */
public final class AdvertiserUtil {


  /**
   * Checks whether the user object passed in exists in datastore as an advertiser
   *
   * @param user the google user to be checked
   * @return true if the user is contained in the datastore as an advertiser, false otherwise
   * @throws DatastoreFailureException if a datastore error occurs
   */
  public static boolean advertiserExistsInDatastore(User user) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key advertiserKey = Advertiser.generateKey(user);
    try {
      datastore.get(advertiserKey);
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    }
  }


}
