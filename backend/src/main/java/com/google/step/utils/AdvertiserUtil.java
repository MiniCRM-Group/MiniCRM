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

package com.google.step.utils;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;


/**
 * This class is designed to be the single point of responsibility for storing and reading
 * Advertiser entities from the datastore. Advertiser refers to the entity in the datastore,
 * representing a User provided by Google Authentication using our application.
 */
public final class AdvertiserUtil {

  public static final String ADVERTISER_ENTITY_NAME = "Advertiser";

  /**
   * Checks whether the user object passed in exists in datastore as an advertiser
   *
   * @param user the google user to be checked
   * @return true if the user is contained in the datastore as an advertiser, false otherwise
   * @throws DatastoreFailureException if a datastore error occurs
   */
  public static boolean advertiserExistsInDatastore(User user) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key advertiserKey = createAdvertiserKey(user);
    try {
      datastore.get(advertiserKey);
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    } finally {
      return false;
    }
  }

  /**
   * Puts the user object passed in to the datastore as an advertiser entity
   *
   * @param user the google user to be added
   * @throws java.util.ConcurrentModificationException if the entity group that the user entity
   *                                                   belongs to was being modified concurrently
   * @throws DatastoreFailureException                 if any other datastore error occurs
   */
  public static void putAdvertiserInDatastore(User user) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(userToAdvertiserEntity(user));
  }

  /**
   * Converts a Google User to an Advertiser datastore Entity
   *
   * @param user the google user to be converted
   * @return the Entity of kind Advertiser representation of the google User
   */
  private static Entity userToAdvertiserEntity(User user) {
    Entity entity = new Entity(createAdvertiserKey(user));
    entity.setProperty("authDomain", user.getAuthDomain());
    entity.setProperty("email", user.getEmail());
    entity.setProperty("federatedIdentity", user.getFederatedIdentity());
    entity.setProperty("nickname", user.getNickname());
    entity.setProperty("userId", user.getUserId());
    return entity;
  }

  /**
   * Gets a User object from the datastore from the advertiser key
   *
   * @param advertiserKey key for this user generated from the user id
   * @return a User object for the key passed in
   * @throws EntityNotFoundException if the advertiserKey given does not exist in the datastore
   */
  public static User getUserFromAdvertiserKey(Key advertiserKey) throws EntityNotFoundException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return advertiserEntityToUser(datastore.get(advertiserKey));
  }

  /**
   * Converts an Advertiser Entity into a Google User Object with the same data
   *
   * @param entity the advertiser entity represeting the Google User
   * @return a Google User object with the same data as the given advertiser Entity
   */
  private static User advertiserEntityToUser(Entity entity) {
    return new User(
        (String) (entity.getProperty("email") == null ? "" : entity.getProperty("email")),
        (String) (entity.getProperty("authDomain") == null ? "" : entity.getProperty("authDomain")),
        (String) (entity.getProperty("userId") == null ? "" : entity.getProperty("userId")),
        (String) (entity.getProperty("federatedIdentity") == null ? ""
            : entity.getProperty("federatedIdentity")));
  }

  /**
   * /** Creates a Key based on the User Id
   *
   * @param user the user object to create a key for
   * @return the key unique to the user's id
   */
  public static Key createAdvertiserKey(User user) {
    return KeyFactory.createKey(ADVERTISER_ENTITY_NAME, user.getUserId());
  }
}
