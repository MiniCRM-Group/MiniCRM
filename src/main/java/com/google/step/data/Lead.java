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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;

import com.google.appengine.api.datastore.Entity;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * This class represents a lead and all its data.
 */
public class Lead {
  private Date date;
  private String leadId;
  private Long campaignId;
  private String gclId;
  private String apiVersion;
  private Long formId;
  private String googleKey;
  private List<ColumnData> userColumnData;
  private Map<String,String> columnData;
  private boolean isTest;
  private Long adgroupId;
  private Long creativeId;

  private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  /**
   * Blank constructor
   */
  public Lead() {
    this.date = new Date(System.currentTimeMillis());
  }

  /**
   * Constructs a lead object based off an Entity of type Lead
   * @param entity entity of type lead that represents a lead
   */
  public Lead(Entity entity) {
    this();
    if (!entity.getKind().equals("Lead")) {
      throw new IllegalArgumentException("Entity is not of type Lead.");
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
   * @return this object represented as JSON
   */
  public String asJson() {
    return gson.toJson(this);
  }

  /**
   * @return this lead object represented as an Entity
   */
  public Entity asEntity() {
    Entity leadEntity = new Entity("Lead");
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
   * Creates a lead based off of JSON
   * @param reader a reader object containing a JSON describing a lead object
   * @return a lead object created by the JSON
   */
  public static Lead fromReader(Reader reader) {
    Lead thisLead = gson.fromJson(reader, Lead.class);
    thisLead.generateDataMap();
    return thisLead;
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
  public Date getDate() {
    return date;
  }

  public String getLeadId() {
    return leadId;
  }
  public void setLeadId(String leadId) {
    this.leadId = leadId;
  }

  public Long getCampaignId() {
    return campaignId;
  }
  public void setCampaignId(Long campaignId) {
    this.campaignId = campaignId;
  }

  public String getGclId() {
    return gclId;
  }
  public void setGclId(String gclId) {
    this.gclId = gclId;
  }

  public String getApiVersion() {
    return apiVersion;
  }
  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public Long getFormId() {
    return formId;
  }
  public void setFormId(Long formId) {
    this.formId = formId;
  }

  public String getGoogleKey() {
    return googleKey;
  }
  public void setGoogleKey(String googleKey) {
    this.googleKey = googleKey;
  }

  public Map<String, String> getColumnData() {
    return Collections.unmodifiableMap(columnData);
  }

  public void putColumnData(String key, String value) {
    columnData.put(key, value);
  }

  public String getColumnData(String key) {
    return columnData.get(key);
  }

  public boolean isTest() {
    return isTest;
  }

  public Long getAdgroupId() {
    return adgroupId;
  }
  public void setAdgroupId(Long adgroupId) {
    this.adgroupId = adgroupId;
  }

  public Long getCreativeId() {
    return creativeId;
  }
  public void setCreativeId(Long creativeId) {
    this.creativeId = creativeId;
  }
}
