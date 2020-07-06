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

/**
 * This class represents column data for a lead.
 */
public class ColumnData {

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
   * @param stringValue new value
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * @return current columnId
   */
  public String getColumnId() {
    return columnId;
  }

  /**
   * @param columnId new columnId
   */
  public void setColumnId(String columnId) {
    this.columnId = columnId;
  }
}
