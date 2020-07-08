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
import java.util.Date;

/**
 * Represents a Form and all of its data. Supports conversion to datastore Entity objects
 * and back.
 */
public final class Form {

  public static final String KIND_NAME = "Form";

  private Key advertiserKey;
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
   * Generates a datastore key for the form specified by the parent advertiser key and form id given
   * @param parentKey the key for the advertiser entity that owns this form
   * @param formId    the id of the form
   * @return          a key for the form specified by the parentKey and formId given
   */
  public static Key generateKey(Key parentKey, long formId) {
    return KeyFactory.createKey(parentKey, KIND_NAME, formId);
  }
}
