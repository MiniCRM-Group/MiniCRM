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
 * Represents a Form and all of its data. Supports conversion to datastore Entity objects
 * and back.
 * Forms are direct children entities of the Advertiser entity that they belong to. They are not
 * directly linked to their leads. Instead, leads are linked to their respective form with the form
 * id instance variable.
 */
public final class Form implements DatastoreObject{

  public static final String KIND_NAME = "Form";

  /**
   * The entity Key of the advertiser entity that this form belongs to.
   */
  private transient Key advertiserKey;
  private Date date;
  private long formId;
  private String formName;

  /**
   * Full parameter constructor for a Form object Auto generated the date object
   *
   * @param formId        the id of the form
   * @param formName      the name of the form
   */
  public Form(Key advertiserKey,
      long formId,
      String formName) {
    this.date = new Date(System.currentTimeMillis());
    this.advertiserKey = advertiserKey;
    this.formId = formId;
    this.formName = formName;
  }

  /**
   * Generates a Form object based off an entity of kind Form.
   *
   * @param entity an entity to generate the Form object from
   * @throw IllegalArgumentException if the entity passed is not of kind Form
   */
  public Form(Entity entity) {
    if (!entity.getKind().equals(KIND_NAME)) {
      throw new IllegalArgumentException("Entity is not of kind Form.");
    }
    this.advertiserKey = entity.getParent();
    this.date = (Date) entity.getProperty("date");
    this.formId = (Long) entity.getProperty("formId");
    this.formName = (String) entity.getProperty("formName");
  }

  /**
   * Generates an Entity of kind Form as an ancestor of the Advertiser this form belongs to.
   * All Entity properties have the same name as their respective instance variables.
   * @return an entity representation of this Form with Kind Form
   */
  public Entity asEntity() {
    Key formKey = generateKey(advertiserKey, formId);
    Entity formEntity = new Entity(formKey);
    formEntity.setProperty("date", date);
    formEntity.setProperty("formId", formId);
    formEntity.setProperty("formName", formName);
    return formEntity;
  }

  /**
   * Checks whether another object o is a Form object that is either the same exact object or has
   * all the same values for all instance variables.
   * @param o the object to compare to this form
   * @return true if the given object is a Form instance with all instance variables equivalent.
   *         false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Form)) {
      return false;
    }

    Form other = (Form) o;
    return this.date.equals(other.date) &&
        this.advertiserKey.equals(other.advertiserKey) &&
        this.formId == other.formId &&
        this.formName.equals(other.formName);
  }

  //GETTERS AND SETTERS
  /**
   * @return the key of the advertiser entity that owns this form
   */
  public Key getAdvertiserKey() {
    return advertiserKey;
  }

  /**
   * @return the time when this form was created
   */
  public Date getDate() {
    return date;
  }

  /**
   * @return the id of this form
   */
  public long getFormId() {
    return formId;
  }

  /**
   * @return the name of this form
   */
  public String getFormName() {
    return formName;
  }

  /**
   * @param formName the new name of this form
   */
  public void setFormName(String formName) {
    this.formName = formName;
  }

  //STATIC METHODS
  /**
   * Generates a datastore key for the form specified by the parent advertiser key and form id given
   * @param parentKey the key for the advertiser entity that owns this form
   * @param formId    the id of the form
   * @return          a key for the form specified by the parentKey and formId given
   */
  public static Key generateKey(Key parentKey, long formId) {
    return KeyFactory.createKey(parentKey, KIND_NAME, formId);
  }

  /**
   * Same as {@link com.google.minicrm.data.Form#generateKey(Key, long)} but for multiple formIds.
   * @param parentKey the key for the advertiser entity that owns this form
   * @param formIds the ids of the forms
   * @return        the keys for the form specified with the given parentKey and formIds.
   */
  public static List<Key> generateKeys(Key parentKey, long[] formIds) {
    List<Key> keys = new ArrayList<>();
    for(long formId : formIds) {
      keys.add(generateKey(parentKey, formId));
    }
    return keys;
  }
}
