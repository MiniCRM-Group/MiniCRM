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

package com.google.minicrm.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

/**
 * Represents an Advertiser using our web application. Consists of a Google User object and other
 * data relevant to the Advertiser (i.e. their googleKey). Supports conversion to datastore entities
 * and back.
 * Advertiser entities are the only root level entity in the datastore. Every data that belongs to
 * an advertiser (their leads, forms, etc.) are children of their advertiser entity.
 */
public class Advertiser implements DatastoreObject {
  public static final String KIND_NAME = "Advertiser";

  private static final String ID_URL_PARAM = "id";
  private static final char[] alphanumerics = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
      .toCharArray();

  private User user;
  private String googleKey;

  /**
   * Creates an Advertiser based off of a User. Generates a random Google Key length 20 for this
   * Advertiser.
   * @param user the user that this Advertiser will represent
   */
  public Advertiser(User user) {
    this.user = user;
    this.googleKey = generateRandomGoogleKey(20);
  }

  /**
   * Constructs an Advertiser based off of an entity of Kind Advertiser
   *
   * @param entity an entity to generate the Advertiser from
   * @throw IllegalArgumentException if the entity passed is not of kind Advertiser
   */
  public Advertiser(Entity entity) {
    if (!entity.getKind().equals(KIND_NAME)) {
      throw new IllegalArgumentException("Entity is not of kind Advertiser.");
    }
    this.user = advertiserEntityToUser(entity);
    this.googleKey = (String) entity.getProperty("googleKey");
  }

  /**
   * Creates an entity representation of this Advertiser
   * @return an entity of kind "Advertiser" representing this Advertiser
   */
  public Entity asEntity() {
    Entity entity = new Entity(generateKey(user));
    entity.setProperty("authDomain", user.getAuthDomain());
    entity.setProperty("email", user.getEmail());
    entity.setProperty("federatedIdentity", user.getFederatedIdentity());
    entity.setProperty("nickname", user.getNickname());
    entity.setProperty("userId", user.getUserId());
    entity.setProperty("googleKey", googleKey);
    return entity;
  }

  /**
   * Creates an Advertiser Key based on the User Id of the User object passed in
   *
   * @param user the user object to create a key for
   * @return the key unique to the user's id
   */
  public static Key generateKey(User user) {
    return KeyFactory.createKey(KIND_NAME, user.getUserId());
  }

  /**
   * Generates the webhook for this user based on the HttpRequest and the advertiser key
   * @return the webhook for this user with a URL-Safe Key String uniquely identifying the user
   */
  public String generateWebhook(HttpServletRequest request) {
    //generate URL-Safe Key string
    String advertiserKeyString = KeyFactory.keyToString(generateKey(this.user));
    return request.getScheme() + "://" +
        request.getServerName() + ":" +
        request.getServerPort() + "/api/webhook?" + ID_URL_PARAM + "=" +
        advertiserKeyString;
  }

  //GETTERS AND SETTERS

  /**
   * @return the user object of this Advertiser
   */
  public User getUser() {
    return user;
  }

  /**
   * @return this advertiser's googleKey
   */
  public String getGoogleKey() {
    return googleKey;
  }

  /**
   * @param googleKey the new googleKey for this Advertiser
   */
  public void setGoogleKey(String googleKey) {
    this.googleKey = googleKey;
  }


  /**
   * Converts an Advertiser Entity into a Google User Object with the same data.
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
}
