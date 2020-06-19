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

import com.google.step.data.ColumnData;
import com.google.step.data.DataIds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;

import com.google.appengine.api.datastore.Entity;

import java.util.*;
import java.io.Reader;

/**
 * This class represents a lead and all its data.
 */
public class Lead {
  private String leadId;
  private long campaignId;
  private String gclId;
  private String apiVersion;
  private long formId;
  private String googleKey;
  private List<ColumnData> userColumnData;
  private Map<String,String> columnData;
  private boolean isTest;
  private long adgroupId;
  private long creativeId;

  private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  public Lead() {
  }

  public Lead(String leadId) {
    this.leadId = leadId;
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
  public String getLeadId() {
    return leadId;
  }
  public void setLeadId(String leadId) {
    this.leadId = leadId;
  }

  public long getCampaignId() {
    return campaignId;
  }
  public void setCampaignId(long campaignId) {
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

  public long getFormId() {
    return formId;
  }
  public void setFormId(long formId) {
    this.formId = formId;
  }

  public String getGoogleKey() {
    return googleKey;
  }
  public void setGoogleKey(String googleKey) {
    this.googleKey = googleKey;
  }

  public List<ColumnData> getUserColumnData() {
    return Collections.unmodifiableList(userColumnData);
  }

  public boolean isTest() {
    return isTest;
  }
}
