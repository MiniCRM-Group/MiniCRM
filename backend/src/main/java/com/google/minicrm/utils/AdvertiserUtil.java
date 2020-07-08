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
import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;


/**
 * Handles storing and reading Advertiser entities from the datastore.
 * Advertiser refers to the entity in the datastore, representing a User provided by
 * Google Authentication using our application.
 */
public final class AdvertiserUtil {

  public static final String ADVERTISER_KIND_NAME = "Advertiser";
  private static final char[] alphanumerics = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
      .toCharArray();
  private static final String ID_URL_PARAM = "id";
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
    }
  }

  /**
   * Puts the user object passed in to the datastore as an advertiser entity.
   * Remakes and changes the advertiser's Google Key if they already exist in the datastore.
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
   * Converts a Google User to an Advertiser datastore Entity with a randomly generated Google Key.
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
    entity.setProperty("googleKey", generateRandomGoogleKey(20));
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
   * Converts an Advertiser Entity into a Google User Object with the same data and a randomly
   * generated google key
   *
   * @param entity the advertiser entity representing the Google User
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
   * Creates an Advertiser Key based on the User Id of the User object passed in
   *
   * @param user the user object to create a key for
   * @return the key unique to the user's id
   */
  public static Key createAdvertiserKey(User user) {
    return KeyFactory.createKey(ADVERTISER_KIND_NAME, user.getUserId());
  }

  /**
   * Gets the Google Key for the passed in User. Assumes the user has an associated Advertiser
   * entity in the datastore with a google key.
   * @param user the user to get the Google Key for
   * @return     the user's google key
   * @throws EntityNotFoundException if the specified User does not have an Advertiser Entity
   */
  public static String getGoogleKey(User user) throws EntityNotFoundException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return (String) datastore.get(createAdvertiserKey(user)).getProperty("googleKey");
  }

  /**
   * Generates a random Google Key of the specified length with alphanumeric characters.
   *
   * @param length the length of the Google Key
   * @return the randomly generated Google Key
   */
  private static String generateRandomGoogleKey(int length) {
    Random rand = new SecureRandom();
    StringBuilder googleKeyBuilder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      googleKeyBuilder.append(alphanumerics[rand.nextInt(alphanumerics.length)]);
    }
    return googleKeyBuilder.toString();
  }

  /**
   * @return the webhook for this user with a URL-Safe Key String uniquely identifying the user
   */
  public static String getUserWebhook(HttpServletRequest request, User user) {
    //generate URL-Safe Key string
    String advertiserKeyString = KeyFactory.keyToString(AdvertiserUtil.createAdvertiserKey(user));
    return request.getScheme() + "://" +
        request.getServerName() + ":" +
        request.getServerPort() + "/api/webhook?" + ID_URL_PARAM + "=" +
        advertiserKeyString;
  }
}
