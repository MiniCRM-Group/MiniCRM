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
 * This enum represents all possible IDs for lead data.
 */
public enum DataIds {
  FULL_NAME("Full Name"),
  FIRST_NAME("First Name"),
  LAST_NAME("Last Name"),
  EMAIL("User Email"),
  PHONE_NUMBER("User Phone"),
  POSTAL_CODE("Postal Code"),
  COMPANY_NAME("Company Name"),
  JOB_TITLE("JOB_TITLE"),
  WORK_PHONE("Work Phone"),
  STREET_ADDRESS("Street Address"),
  CITY("City"),
  REGION("Region"),
  COUNTRY("Country"),
  VEHICLE_MODEL,
  VEHICLE_TYPE,
  PREFERRED_DEALERSHIP,
  VEHICLE_PURCHASE_TIMELINE,
  VEHICLE_CONDITION,
  VEHICLE_ONWERSHIPVEHICLE_PAYMENT_TYPE,
  VEHICLE_PAYMENT_TYPE,
  COMPANY_SIZE,
  ANNUAL_SALES,
  YEARS_IN_BUSINESS,
  JOB_DEPARTMENT,
  JOB_ROLE,
  EDUCATION_COURSE,
  PRODUCT,
  SERVICE,
  OFFER,
  CATEGORY,
  PREFERRED_CONTACT_METHOD,
  PREFERRED_LOCATION,
  PREFERRED_CONTACT_TIME,
  PURCHASE_TIMELINE,
  YEARS_OF_EXPERIENCE,
  JOB_INDUSTRY,
  LEVEL_OF_EDUCATION,
  PROPERTY_TYPE,
  REALTOR_HELP_GOAL,
  PROPERTY_COMMUNITY,
  PRICE_RANGE,
  NUMBER_OF_BEDROOMS,
  FURNISHED_PROPERTY,
  PETS_ALLOWED_PROPERTY,
  NEXT_PLANNED_PURCHASE,
  EVENT_SIGNUP_INTEREST,
  PREFERRED_SHOPPING_PLACES,
  FAVORITE_BRAND,
  TRANSPORTATION_COMMERCIAL_LICENSE_TYPE,
  EVENT_BOOKING_INTEREST,
  DESTINATION_CITY,
  DEPARTURE_COUNTRY,
  DEPARTURE_CITY,
  DEPARTURE_DATE,
  RETURN_DATE,
  NUMBER_OF_TRAVELERES,
  TRAVEL_BUDGET,
  TRAVEL_ACCOMODATION;


  private String name;
  private DataIds() {
  }

  private DataIds(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
