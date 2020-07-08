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

package com.google.minicrm.utils;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.minicrm.data.DatastoreObject;

/**
 * Handles any frequent methods and access patterns to the datastore.
 */
public class DatastoreUtil {

  /**
   * Checks whether the key passed in exists in datastore
   *
   * @param entityKey  the key whose existence in the datastore will be checked
   * @return true if the key exists the datastore as an advertiser, false otherwise
   */
  public static boolean exists(Key entityKey) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      datastore.get(entityKey);
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    }
  }

  /**
   * Puts the given datastoreObject in the datastore
   * @param datastoreObject the object to be put in the datastore
   * @return the Key of the entity representation of the datastoreObject
   */
  public static Key put(DatastoreObject datastoreObject) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return datastore.put(datastoreObject.asEntity());
  }
}
