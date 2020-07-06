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
import com.google.minicrm.utils.AdvertiserUtil;
import java.util.Date;

/**
 * Represents a Form and all of its data. Supports conversion to datastore Entity objects
 * and back.
 */
public final class Form {

  public static final String KIND_NAME = "Form";
  private Date date;
  private long formId;
  private String formName;

  /**
   * Key for the advertiser entity that this form belongs to. Transient to prevent GSON from
   * serializing this when sending form data to client.
   */
  private transient Key advertiserKey;
  private String googleKey;
  private boolean verified;

  /**
   * Full parameter constructor for a Form object Auto generated the date object
   *
   * @param formId        the id of the form
   * @param formName      the name of the form
   * @param advertiserKey the key of the advertiser entity in datastore
   * @param googleKey     the google key for this form
   * @param verified      whether or not this form has been verified
   */
  public Form(long formId,
      String formName,
      Key advertiserKey,
      String googleKey,
      boolean verified) {
    this.date = new Date(System.currentTimeMillis());
    this.formId = formId;
    this.formName = formName;
    this.advertiserKey = advertiserKey;
    this.googleKey = googleKey;
    this.verified = verified;
  }

  /**
   * Generates a Form object based off an entity of kind Form.
   *
   * @param entity
   */
  public Form(Entity entity) {
    if (!entity.getKind().equals(KIND_NAME)) {
      throw new IllegalArgumentException("Entity is not of kind Form.");
    }
    this.date = (Date) entity.getProperty("date");
    this.formId = (Long) entity.getProperty("formId");
    this.formName = (String) entity.getProperty("formName");
    this.advertiserKey = (Key) entity.getProperty("advertiserKey");
    this.googleKey = (String) entity.getProperty("googleKey");
    this.verified = (Boolean) entity.getProperty("verified");
  }

  /**
   * @return an entity representation of this Form with Kind Form
   */
  public Entity asEntity() {
    Key formKey = KeyFactory.createKey(advertiserKey, KIND_NAME, formId);
    Entity formEntity = new Entity(formKey);
    formEntity.setProperty("date", date);
    formEntity.setProperty("formId", formId);
    formEntity.setProperty("formName", formName);
    formEntity.setProperty("advertiserKey", advertiserKey);
    formEntity.setProperty("googleKey", googleKey);
    formEntity.setProperty("verified", verified);
    return formEntity;
  }

  public static Key getFormKeyFromUserAndFormId(User user, long formId) {
    return KeyFactory.createKey(AdvertiserUtil.createAdvertiserKey(user), KIND_NAME, formId);
  }

}
