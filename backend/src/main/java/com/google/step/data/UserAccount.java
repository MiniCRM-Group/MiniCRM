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

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;


/**
 * This class represents a UserAccount in datastore and provides utility methods
 * for working with Appengine User objects and Entities of kind "UserAccount".
 * This class is designed to be the single point of responsibility for storing and reading
 * UserAccount entities from the datstore.
 */
public final class UserAccount {

    /**
     * Checks whether the user object passed in exists in datastore
     * @param user the google user to be checked
     * @return     true if the user is contained in the datastore, false otherwise
     * @throws DatastoreFailureException if a datastore error occurs
     */
    public static boolean existsInDatastore(User user) {
        try {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Key userKey = createKey(user);
            datastore.get(userKey);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    /**
     * Puts the user object passed in to the datastore
     * @param user the google user to be added
     * @throws java.util.ConcurrentModificationException if the entity group that the user entity
     *  belongs to was being modified concurrently
     * @throws DatastoreFailureException if any other datastore error occurs
     */
    public static void putInDatastore(User user) {
        Key userKey = createKey(user);
        Entity userEntity = new Entity(userKey);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(userEntity);
    }

    /**
     * Creates a Key based on the User Id
     * @param user the user object to create a key for
     * @return     the key unique to the user's id
     */
    private static Key createKey(User user) {
        return KeyFactory.createKey("UserAccount", user.getUserId());
    }
}
