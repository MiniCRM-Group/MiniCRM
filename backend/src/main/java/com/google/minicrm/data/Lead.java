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
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a lead and all its data. Supports conversion to and from JSON and datastore Entity
 * objects.
 * Leads are direct children of the Advertiser entity that they belong to. The form and campaign
 * that each lead belongs to can be obtained from the formId and campaignId respectively by
 * generating a datastore key from them.
 */
public final class Lead implements DatastoreObject {

  public static final String KIND_NAME = "Lead";
  private static final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
  private static String geoApiKey;
  private static List<AreaCode> areaCodes;

  static {
    //load in GEO API KEY
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream is = classloader.getResourceAsStream("api-keys/GeoApiKey.txt");
    geoApiKey = new BufferedReader(
        new InputStreamReader(is, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));

    // load in area code JSON
    ClassLoader classloader2 = Thread.currentThread().getContextClassLoader();
    InputStream is2 = classloader2.getResourceAsStream("data/AreaCodes.json");
    InputStreamReader reader = 
      new InputStreamReader(is2, StandardCharsets.UTF_8);
    areaCodes = new Gson().fromJson(reader, new TypeToken<List<AreaCode>>() {}.getType());
    try {
      reader.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private transient Key advertiserKey;
  private final Date date;
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
   * Advertiser defined and edited fields.
   */
  private LeadStatus status;
  private String notes;
  /**
   * Generated based on any column data that may contain location data (zip code, phone #, etc.)
   * If no location data exists, both will be null.
   */
  private Double estimatedLatitude;
  private Double estimatedLongitude;

  /**
   * Blank constructor that sets the time when this lead was created and sets defaults for values
   * that are not provided by Google Ads server.
   */
  public Lead() {
    this.date = new Date(System.currentTimeMillis());
    this.notes = "";
    this.status = LeadStatus.NEW;
  }

  /**
   * Constructs a lead object based off an Entity of kind Lead
   *
   * @param entity entity of kind lead that represents a lead
   */
  public Lead(Entity entity) {
    if (!entity.getKind().equals(KIND_NAME)) {
      throw new IllegalArgumentException("Entity is not of kind Lead.");
    }
    this.advertiserKey = entity.getParent();
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
    this.status = LeadStatus.getFromIndex(((Long) entity.getProperty("status")).intValue());
    this.notes = (String) entity.getProperty("notes");
    this.estimatedLatitude = (Double)entity.getProperty("estimatedLatitude");
    this.estimatedLongitude = (Double)entity.getProperty("estimatedLongitude");

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
    entity.removeProperty("status");
    entity.removeProperty("notes");
    entity.removeProperty("estimatedLatitude");
    entity.removeProperty("estimatedLongitude");
    this.columnData = new HashMap<>();
    for (String key : entity.getProperties().keySet()) {
      columnData.put(key, (String) entity.getProperty(key));
    }
  }

  /**
   * Creates a lead based off of JSON representing the Lead with the parent advertiserKey
   * specified.
   *
   * @param reader        a reader object containing a JSON describing a lead object
   * @param advertiserKey the Key of the advertiser that this lead belongs to
   * @return a lead object created by the JSON
   */
  public static Lead fromReader(Reader reader, Key advertiserKey) {
    Lead thisLead = gson.fromJson(reader, Lead.class);
    thisLead.generateDataMap();
    thisLead.generateLocationInfo();
    thisLead.advertiserKey = advertiserKey;
    return thisLead;
  }

  /**
   * @return this object represented as JSON
   */
  public String asJson() {
    return gson.toJson(this);
  }

  /**
   * Generates an Entity of kind Lead with the parent entity specified by the parentKey passed in.
   * All Entity properties have the same name as their respective instance variables.
   * The key value pairs in the columnData map are stored separately.
   *
   * @return this lead object represented as an Entity
   */
  public Entity asEntity() {
    Entity leadEntity = new Entity(generateKey(advertiserKey, leadId));
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
    leadEntity.setProperty("notes", notes);
    leadEntity.setProperty("status", status.getIndex());
    leadEntity.setProperty("estimatedLatitude", estimatedLatitude);
    leadEntity.setProperty("estimatedLongitude", estimatedLongitude);
    for (String key : columnData.keySet()) {
      leadEntity.setProperty(key, columnData.get(key));
    }
    return leadEntity;
  }

  /**
   * Checks whether another object o is a Lead that is either the same object as this lead or has
   * all the same instance variables expect the date created variable and advertiserKey.
   *
   * @param o the object to compare to this lead
   * @return true if the given object is a Lead instance with the same instance variables other than
   *         date created and advertiserKey. False, otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Lead)) {
      return false;
    }

    Lead other = (Lead) o;
    return this.leadId.equals(other.leadId) &&
        this.campaignId == other.campaignId &&
        this.gclId.equals(other.gclId) &&
        this.apiVersion.equals(other.apiVersion) &&
        this.formId == other.formId &&
        this.googleKey.equals(other.googleKey) &&
        this.columnData.equals(other.columnData) &&
        this.isTest == other.isTest &&
        this.adgroupId == other.adgroupId &&
        this.creativeId == other.creativeId &&
        this.status == other.status &&
        this.notes.equals(other.notes);
  }

  /**
   * Returns a JSON representation of this object as a String
   * @return a String representation of this lead
   */
  @Override
  public String toString() {
    return this.asJson();
  }

  /**
   * Generates a datastore key for the lead specified by the parent advertiser key and lead id given
   * @param parentKey the key for the advertiser entity that owns this lead
   * @param leadId    the id of the lead
   * @return          a key for thelead specified by the parentKey and leadId given
   */
  public static Key generateKey(Key parentKey, String leadId) {
    return KeyFactory.createKey(parentKey, KIND_NAME, leadId);
  }

  /**
   * Searches for any location data present (Postal Code, Street Address, City, Region, Country, and Phone Number ) 
   * and populates the estimatedLongitude and
   * estimateLatitude instance variables.
   */
  private void generateLocationInfo() {
    StringBuilder locationInfo = new StringBuilder();
    String phoneNumber = "";

    for (String key : columnData.keySet()) {
      // If the column data is related with address add it into locationInfo arraylist
      if (key.equals("POSTAL_CODE") || key.equals("STREET_ADDRESS") ||
          key.equals("CITY") || key.equals("REGION") ||
          key.equals("COUNTRY")) {
        locationInfo.append(columnData.get(key) + " ");
      } else if (key.equals("PHONE_NUMBER") ) {
        phoneNumber = columnData.get("PHONE_NUMBER");
      }
    }
    
    // If there is no info from the generated lead relating to address then GeoCoding is impossible
    if (locationInfo.length() == 0 && phoneNumber.equals("")) {
      //no location data
      return;
    }

    //use locationInfo first if any exists; phoneNumber area code is a backup
    if (locationInfo.length() > 0) {
      GeoApiContext context = new GeoApiContext.Builder()
        .apiKey(geoApiKey)
        .build();

      GeocodingResult[] results;
      try {
        results = GeocodingApi.geocode(context, locationInfo.toString()).await();
      } catch (ApiException | InterruptedException | IOException e) {
        e.printStackTrace();
        return;
      }

      // Get and assign latitude and longitude
      estimatedLatitude = results[0].geometry.location.lat;
      estimatedLongitude = results[0].geometry.location.lng;
    } else { //use phone number info since no other locationInfo exists
      final int areaCodeFromLead;
      try {
        areaCodeFromLead = Integer.parseInt(phoneNumber.substring(0, 3));
      } catch (NumberFormatException e) {
        //error in parsing phoneNumber area code, don't do anything then
        return;
      }

      // Filter AreaCodes list to get the AreaCode
      List<AreaCode> areaCodesFiltered = areaCodes
        .stream()
        .filter(c -> c.areaCodes == areaCodeFromLead)
        .collect(Collectors.toList());
      if (!areaCodesFiltered.isEmpty()) {
        estimatedLatitude = areaCodesFiltered.get(0).latitude;
        estimatedLongitude = areaCodesFiltered.get(0).longitude;
      }
    }
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
   * @return the Key for the advertiser entity that owns this lead
   */
  public Key getAdvertiserKey() {
    return advertiserKey;
  }

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

  /**
   * @return the String representation of this lead's notes
   */
  public String getNotes() {
    return notes;
  }

  /**
   * @param notes the new notes for this lead
   */
  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * @return the status of this lead
   */
  public LeadStatus getStatus() {
    return status;
  }

  /**
   * @param status the new status of this lead
   */
  public void setStatus(LeadStatus status) {
    this.status = status;
  }


  /**
   * This class represents column data for a lead.
   */
  private class ColumnData {

    private String stringValue;
    private String columnId;

    //Getters and Setters

    /**
     * @return columnData value
     */
    public String getStringValue() {
      return stringValue;
    }

    /**
     * @return current columnId
     */
    public String getColumnId() {
      return columnId;
    }
  }
}
