package com.google.minicrm.interfaces;

/**
 * Represents responses sent back to the client.
 */
public interface ClientResponse {

  /**
   * Returns this response as a String in JSON format.
   */
  public String toJson();
}
