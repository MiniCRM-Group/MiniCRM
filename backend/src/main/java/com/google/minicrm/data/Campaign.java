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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a Campaign and all of its data. Supports conversion to datastore Entity objects and
 * back. Campaigns are direct children entities of the Advertiser entity that they belong to. They
 * are not directly linked to their leads. Instead, leads are linked to their respective campaign
 * with the campaign id instance variable.
 */
public final class Campaign implements DatastoreObject {

  public static final String KIND_NAME = "Campaign";

  /**
   * The entity Key of the advertiser entity that this campaign belongs to.
   */
  private transient Key advertiserKey;
  private Date date;
  private long campaignId;
  private String campaignName;

  /**
   * Full parameter constructor for a Campaign object Auto generated the date object
   *
   * @param campaignId   the id of the campaign
   * @param campaignName the name of the campaign
   */
  public Campaign(Key advertiserKey,
      long campaignId,
      String campaignName) {
    this.date = new Date(System.currentTimeMillis());
    this.advertiserKey = advertiserKey;
    this.campaignId = campaignId;
    this.campaignName = campaignName;
  }

  /**
   * Generates a Campaign object based off an entity of kind Campaign.
   *
   * @param entity an entity to generate the Campaign object from
   * @throw IllegalArgumentException if the entity passed is not of kind Campaign
   */
  public Campaign(Entity entity) {
    if (!entity.getKind().equals(KIND_NAME)) {
      throw new IllegalArgumentException("Entity is not of kind Campaign.");
    }
    this.advertiserKey = entity.getParent();
    this.date = (Date) entity.getProperty("date");
    this.campaignId = (Long) entity.getProperty("campaignId");
    this.campaignName = (String) entity.getProperty("campaignName");
  }

  /**
   * Generates an Entity of kind Campaign as an ancestor of the Advertiser this campaign belongs to.
   * All Entity properties have the same name as their respective instance variables.
   *
   * @return an entity representation of this Campaign with Kind Campaign
   */
  public Entity asEntity() {
    Key campaignKey = generateKey(advertiserKey, campaignId);
    Entity campaignEntity = new Entity(campaignKey);
    campaignEntity.setProperty("date", date);
    campaignEntity.setProperty("campaignId", campaignId);
    campaignEntity.setProperty("campaignName", campaignName);
    return campaignEntity;
  }

  /**
   * Checks whether another object o is a Campaign object that is either the same exact object or
   * the same campaignId and campaignName.
   * @param o the object to compare to this campaign
   * @return true if the given object is a Campaign instance with the same campaignId and
   *          campaignName as this campaign. Otherwise, false.
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Campaign)) {
      return false;
    }

    Campaign other = (Campaign) o;
    return this.campaignId == other.campaignId &&
        this.campaignName.equals(other.campaignName);
  }

  /**
   * Returns a String representation of this campaign with its campaignId and campaignName
   * @return a String representation of this campaign
   */
  @Override
  public String toString() {
    String str = "Campaign:{";
    str += "campaignId=" + campaignId + ", ";
    str += "campaignName=\"" + campaignName + "\"}";
    return str;
  }

  //GETTERS AND SETTERS

  /**
   * @return the key of the advertiser entity that owns this campaign
   */
  public Key getAdvertiserKey() {
    return advertiserKey;
  }

  /**
   * @return the time when this campaign was created
   */
  public Date getDate() {
    return date;
  }

  /**
   * @return the id of this campaign
   */
  public long getCampaignId() {
    return campaignId;
  }

  /**
   * @return the name of this campaign
   */
  public String getCampaignName() {
    return campaignName;
  }

  /**
   * @param campaignName the new name of this campaign
   */
  public void setCampaignName(String campaignName) {
    this.campaignName = campaignName;
  }

  //STATIC METHODS

  /**
   * Generates a datastore key for the campaign specified by the parent advertiser key and campaign
   * id given
   *
   * @param parentKey  the key for the advertiser entity that owns this campaign
   * @param campaignId the id of the campaign
   * @return a key for the campaign specified by the parentKey and campaignId given
   */
  public static Key generateKey(Key parentKey, long campaignId) {
    return KeyFactory.createKey(parentKey, KIND_NAME, campaignId);
  }

  /**
   * Same as {@link com.google.minicrm.data.Campaign#generateKey(Key, long)} but for multiple
   * campaignIds.
   *
   * @param parentKey   the key for the advertiser entity that owns this campaign
   * @param campaignIds the ids of the campaigns
   * @return the keys for the campaigns specified with the given parentKey and campaignIds.
   */
  public static List<Key> generateKeys(Key parentKey, long[] campaignIds) {
    List<Key> keys = new ArrayList<>();
    for (long campaignId : campaignIds) {
      keys.add(generateKey(parentKey, campaignId));
    }
    return keys;
  }
}
