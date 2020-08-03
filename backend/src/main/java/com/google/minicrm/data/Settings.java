package com.google.minicrm.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.routines.EmailValidator;
import java.io.Reader;
import java.util.Map;

/**
 * An advertiser's settings for configuring email/phone
 * notifications, language, and currency.
 */
public final class Settings implements DatastoreObject {
    public static String KIND_NAME = "Settings";
    private transient Key advertiserKey;
    private String email;
    private NotificationsFrequency emailNotificationsFrequency;
    private String phone;
    private NotificationsFrequency phoneNotificationsFrequency;
    private Currency currency;

    public Settings(Key advertiserKey, String email, String phone, NotificationsFrequency emailNotificationsFrequency,
                    NotificationsFrequency phoneNotificationsFrequency, Currency currency)
            throws ValidatorException, NumberParseException {
        this.advertiserKey = advertiserKey;
        this.email = email;
        this.phone = phone;
        this.emailNotificationsFrequency = emailNotificationsFrequency;
        this.phoneNotificationsFrequency = phoneNotificationsFrequency;
        this.currency = currency;
        if (this.emailNotificationsFrequency != NotificationsFrequency.NEVER) {
            if (email == null || email.trim().length() < 0) {
                throw new IllegalArgumentException("Must provide email if notifications are enabled");
            }
            EmailValidator emailValidator = EmailValidator.getInstance();
            if (!emailValidator.isValid(email)) {
                throw new ValidatorException("Invalid email: " + email);
            }
        } else {
            this.email = ""; // ignore email input if user disables notifications
        }

        if (this.phoneNotificationsFrequency != NotificationsFrequency.NEVER) {
            if (phone == null || phone.trim().length() < 0) {
                throw new IllegalArgumentException("Must provide phone if notifications are enabled");
            }
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            if (!phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phone, "US"))) {
                throw new ValidatorException("Invalid phone number: " + email);
            }
        } else {
            this.phone = ""; // ignore phone input if user disables notifications
        }
    }

    /**
     * For creation of settings.
     * @param advertiserKey Used for linking a settings object to an advertiser.
     * @param email Used for sending notifications. We ask for this from the user
     *              because we can always reliably get it from the user service.
     */
    public Settings(Key advertiserKey, String email) {
        this.advertiserKey = advertiserKey;
        this.email = email;
        this.currency = Currency.US_DOLLAR;
        // defaults
        this.emailNotificationsFrequency = NotificationsFrequency.NEVER;
        this.phone = "";
        this.phoneNotificationsFrequency = NotificationsFrequency.NEVER;
    }

    /**
     * For retrieval of settings.
     * @param entity Used for mapping to a settings object.
     */
    public Settings(Entity entity) {
        this.advertiserKey = entity.getParent();
        this.email = (String) entity.getProperty("email");
        this.emailNotificationsFrequency =
                NotificationsFrequency.fromDisplayed((String) entity.getProperty("emailNf"));
        this.phone = (String) entity.getProperty("phone");
        this.phoneNotificationsFrequency =
                NotificationsFrequency.fromDisplayed((String) entity.getProperty("phoneNf"));
        this.currency = Currency.fromIsoCode((String) entity.getProperty("currency"));
    }

    public static Settings fromReader(Reader reader, Key advertiserKey) throws NumberParseException, ValidatorException, MalformedJsonException {
        Gson gson = new Gson();
        Map<String, String> jsonMap = gson.fromJson(reader, Map.class);
        if(jsonMap == null || !jsonMap.containsKey("email") || !jsonMap.containsKey("phone") ||
                !jsonMap.containsKey("emailNotificationsFrequency") ||
                !jsonMap.containsKey("phoneNotificationsFrequency") ||
                !jsonMap.containsKey("currency")) {
            throw new MalformedJsonException("One or more missing keys in JSON.");
        }
        String email = jsonMap.get("email");
        String phone = jsonMap.get("phone");
        NotificationsFrequency emailNotifFreq = NotificationsFrequency.valueOf(jsonMap.get("emailNotificationsFrequency"));
        NotificationsFrequency phoneNotifFreq = NotificationsFrequency.valueOf(jsonMap.get("phoneNotificationsFrequency"));
        Language lang = Language.fromIsoCode(jsonMap.get("language"));
        Currency curr = Currency.fromIsoCode(jsonMap.get("currency"));
        return new Settings(advertiserKey, email, phone, emailNotifFreq, phoneNotifFreq, curr);
    }

    public String getEmail() {
        return email;
    }

    public NotificationsFrequency getEmailNotificationsFrequency() {
        return emailNotificationsFrequency;
    }

    public String getPhone() {
        return phone;
    }

    public NotificationsFrequency getPhoneNotificationFrequency() {
        return phoneNotificationsFrequency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailNotificationsFrequency(NotificationsFrequency emailNotificationsFrequency) {
        this.emailNotificationsFrequency = emailNotificationsFrequency;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoneNotificationsFrequency(NotificationsFrequency phoneNotificationsFrequency) {
        this.phoneNotificationsFrequency = phoneNotificationsFrequency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public Entity asEntity() {
        Key key = KeyFactory.createKey(this.advertiserKey, KIND_NAME, this.advertiserKey.toString());
        Entity entity = new Entity(key);
        entity.setProperty("email", email);
        entity.setProperty("emailNf", emailNotificationsFrequency.getDisplayed());
        entity.setProperty("phone", phone);
        entity.setProperty("phoneNf", phoneNotificationsFrequency.getDisplayed());
        entity.setProperty("currency", currency.getIsoCode());
        return entity;
    }
}
