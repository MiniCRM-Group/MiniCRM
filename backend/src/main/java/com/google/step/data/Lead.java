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

package com.google.step.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a lead and all its data.
 */
public final class Lead {

  private static final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  private Date date;
  private String leadId;
  private long campaignId;
  private String gclId;
  private String apiVersion;
  private long formId;
  private String googleKey;
  private List<ColumnData> userColumnData;
  private Map<String, String> columnData;
  private boolean isTest;
  private long adgroupId;
  private long creativeId;

  /**
   * Blank constructor that sets the time when this lead was created
   */
  public Lead() {
    this.date = new Date(System.currentTimeMillis());
  }

  /**
   * Constructs a lead object based off an Entity of kind Lead
   *
   * @param entity entity of kind lead that represents a lead
   */
  public Lead(Entity entity) {
    this();
    if (!entity.getKind().equals("Lead")) {
      throw new IllegalArgumentException("Entity is not of kind Lead.");
    }
    this.date = (Date) entity.getProperty("date");
    this.leadId = (String) entity.getProperty("leadId");
    this.campaignId = (Long) entity.getProperty("campaignId");
    this.gclId = (String) entity.getProperty("gclId");
    this.apiVersion = (String) entity.getProperty("apiVersion");
    this.formId = (Long) entity.getProperty("formId");
    this.googleKey = (String) entity.getProperty("googleKey");
    this.isTest = (Boolean) entity.getProperty("isTest");
    this.adgroupId = (Long) entity.getProperty("adgroupId");
    this.creativeId = (Long) entity.getProperty("creativeId");

    entity.removeProperty("date");
    entity.removeProperty("leadId");
    entity.removeProperty("campaignId");
    entity.removeProperty("gclId");
    entity.removeProperty("apiVersion");
    entity.removeProperty("formId");
    entity.removeProperty("googleKey");
    entity.removeProperty("isTest");
    entity.removeProperty("adgroupId");
    entity.removeProperty("creativeId");
    this.columnData = new HashMap<>();
    for (String key : entity.getProperties().keySet()) {
      columnData.put(key, (String) entity.getProperty(key));
    }
  }

  /**
   * Creates a lead based off of JSON
   *
   * @param reader a reader object containing a JSON describing a lead object
   * @return a lead object created by the JSON
   */
  public static Lead fromReader(Reader reader) {
    Lead thisLead = gson.fromJson(reader, Lead.class);
    thisLead.generateDataMap();
    return thisLead;
  }

  /**
   * @return this object represented as JSON
   */
  public String asJson() {
    return gson.toJson(this);
  }

  /**
   * @param parentKey the key of the parent entity of this entity. Should be an Advertiser key.
   * @return this lead object represented as an Entity
   */
  public Entity asEntity(Key parentKey) {
    Entity leadEntity = new Entity("Lead", parentKey);
    leadEntity.setProperty("date", date);
    leadEntity.setProperty("leadId", leadId);
    leadEntity.setProperty("campaignId", campaignId);
    leadEntity.setProperty("gclId", gclId);
    leadEntity.setProperty("apiVersion", apiVersion);
    leadEntity.setProperty("formId", formId);
    leadEntity.setProperty("googleKey", googleKey);
    leadEntity.setProperty("isTest", isTest);
    leadEntity.setProperty("adgroupId", adgroupId);
    leadEntity.setProperty("creativeId", creativeId);
    for (String key : columnData.keySet()) {
      leadEntity.setProperty(key, columnData.get(key));
    }
    return leadEntity;
  }

  /**
   * Creates and populates the columnData Map from userColumnData
   */
  private void generateDataMap() {
    columnData = new HashMap<>(userColumnData.size());
    for (ColumnData cData : userColumnData) {
      columnData.put(cData.getColumnId(), cData.getStringValue());
    }
  }

  //Getters and Setters

  /**
   * @return the Date this lead was received
   */
  public Date getDate() {
    return date;
  }

  /**
   * @return current leadId
   */
  public String getLeadId() {
    return leadId;
  }

  /**
   * @return current campaignId
   */
  public long getCampaignId() {
    return campaignId;
  }

  /**
   * @return current gclId
   */
  public String getGclId() {
    return gclId;
  }

  /**
   * @return current apiVersion
   */
  public String getApiVersion() {
    return apiVersion;
  }

  /**
   * @return current formId
   */
  public long getFormId() {
    return formId;
  }

  /**
   * @return current googleKey
   */
  public String getGoogleKey() {
    return googleKey;
  }

  /**
   * @return unmodifiable map representing this lead's column data
   */
  public Map<String, String> getColumnData() {
    return Collections.unmodifiableMap(columnData);
  }

  /**
   * Puts the key value pair associated into the Lead's column data
   *
   * @param key   the key
   * @param value the value
   */
  public void putColumnData(String key, String value) {
    columnData.put(key, value);
  }

  /**
   * @param key the property key
   * @return the value associated with the key
   */
  public String getColumnData(String key) {
    return columnData.get(key);
  }

  /**
   * @return whether this Lead is a test
   */
  public boolean isTest() {
    return isTest;
  }

  /**
   * @return this Lead's adgroupId
   */
  public long getAdgroupId() {
    return adgroupId;
  }

  /**
   * @return this Lead's creativeId
   */
  public long getCreativeId() {
    return creativeId;
  }
}
