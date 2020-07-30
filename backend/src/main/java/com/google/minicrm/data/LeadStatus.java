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

/**
 * Represents all the possible statuses a lead can have throughout its lifecycle.
 */
public enum LeadStatus {
  NEW(0),
  OPEN(1),
  WORKING(2),
  CLOSED_CONVERTED(3),
  CLOSED_NOT_CONVERTED(4);

  private static final LeadStatus[] STATUSES = LeadStatus.values();
  private final int index;

  LeadStatus(int index) {
    this.index = index;
  }

  /**
   * @return the index of this lead status
   */
  public int getIndex() {
    return index;
  }

  /**
   * @param i the index of the leadstatus to get
   * @return the lead status at the corresponding index
   */
  public static LeadStatus getFromIndex(int i) {
    return STATUSES[i];
  }
}
