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
 * This utility class is responsible for using the App Engine Mail API to handle any emailing that
 * the server needs to make.
 */
public final class EmailUtil {

  public static void sendTestEmail() throws MessagingException {
    Session session = Session.getDefaultInstance(new Properties(), null);
    Message msg = new MimeMessage(session);
    try {
      msg.setFrom(new InternetAddress("test@form-ads-leads.appspot.com", "Test Address"));
    } catch (UnsupportedEncodingException e) {
      msg.setFrom(new InternetAddress("test@form-ads-leads.appspot.com"));
    }
    try {
      msg.addRecipient(Message.RecipientType.TO,
          new InternetAddress("alexkimt@gmail.com", "Test User"));
    } catch (UnsupportedEncodingException e) {
      msg.addRecipient(Message.RecipientType.TO,
          new InternetAddress("alexkimt@gmail.com"));
    }
    msg.setSubject("Test email!");
    msg.setText("This is the test email's text!");
    Transport.send(msg);
  }
}
