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

package com.google.step.utils;

import com.google.appengine.api.users.User;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Handles any emailing that the server needs to make.
 */
public final class EmailUtil {

  /**
   * Sends an email to the given user notifying them of their new lead
   * @param user                 the google user to send the email to. Uses the user's email address
   * @throws MessagingException  if any errors with the Mail API service occurs. The email will not
   *                             be sent
   */
  public static void sendNewLeadEmail(User user) throws MessagingException {
    Session session = Session.getDefaultInstance(new Properties(), null);
    Message msg = new MimeMessage(session);
    try {
      msg.setFrom(new InternetAddress("noreply-test@form-ads-leads.appspot.com",
          "New Lead"));
    } catch (UnsupportedEncodingException e) {
      msg.setFrom(new InternetAddress("noreply-test@form-ads-leads.appspot.com"));
    }
    try {
      msg.addRecipient(Message.RecipientType.TO,
          new InternetAddress(user.getEmail(), user.getNickname()));
    } catch (UnsupportedEncodingException e) {
      msg.addRecipient(Message.RecipientType.TO,
          new InternetAddress(user.getEmail()));
    }
    msg.setSubject("New Lead Received!");
    msg.setText("You just got a new lead! "
        + "Check your new lead out at form-ads-leads.appspot.com!");
    Transport.send(msg);
  }
}
