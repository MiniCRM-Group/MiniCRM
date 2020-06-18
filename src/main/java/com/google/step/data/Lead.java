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

import java.util.List;
import java.util.Collections;

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
  private boolean isTest;


  public Lead() {
  }

  public Lead(String leadId) {
    this.leadId = leadId;
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
