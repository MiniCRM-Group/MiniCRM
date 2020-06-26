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
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;

public class Form {
    private Date date;
    private long formId;
    private String formName;
    private Key advertiserKey;
    private String googleKey;
    private boolean verified;

    /**
     * Full parameter constructor for a Form object
     * Auto generated the date object
     * @param formId         the id of the form
     * @param formName       the name of the form
     * @param advertiserKey  the key of the advertiser entity in datastore
     * @param googleKey      the google key for this form
     * @param verified       whether or not this form has been verified
     */
    public Form (long formId,
                 String formName,
                 Key advertiserKey,
                 String googleKey,
                 boolean verified) {
        this();
        this.formId = formId;
        this.formName = formName;
        this.advertiserKey = advertiserKey;
        this.googleKey = googleKey;
        this.verified = verified;
    }

    /**
     * Constructs a form with default value of not verified
     * @param formId        form id
     * @param formName      form name
     * @param advertiserKey advertiser key
     * @param googleKey     google key
     */
    public Form (long formId,
                 String formName,
                 Key advertiserKey,
                 String googleKey) {
        this(formId, formName, advertiserKey, googleKey, false);
    }

    /**
     * Private blank constructor that initializes the date created.
     */
    private Form () {
        this.date = new Date(System.currentTimeMillis());
    }

    /**
     * Generates a Form object based off an entity of kind Form.
     * @param entity
     */
    public Form (Entity entity) {
        this();
        if (!entity.getKind().equals("Form")) {
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
        Key formKey = KeyFactory.createKey(advertiserKey, "Form", formId);
        Entity formEntity = new Entity(formKey);
        formEntity.setProperty("date", date);
        formEntity.setProperty("formId", formId);
        formEntity.setProperty("formName", formName);
        formEntity.setProperty("advertiserKey", advertiserKey);
        formEntity.setProperty("googleKey", googleKey);
        formEntity.setProperty("verified", verified);
        return formEntity;
    }

}
