package com.google.minicrm.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
    private Language language;
    private Currency currency;

    /**
     * For creation of settings.
     * @param advertiserKey Used for linking a settings object to an advertiser.
     * @param email Used for sending notifications. We ask for this from the user
     *              because we can always reliably get it from the user service.
     */
    public Settings(Key advertiserKey, String email) {
        this.advertiserKey = advertiserKey;
        this.email = email;
        this.language = Language.ENGLISH;
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
        this.language = Language.fromIsoCode((String) entity.getProperty("language"));
        this.currency = Currency.fromIsoCode((String) entity.getProperty("currency"));
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

    public Language getLanguage() {
        return language;
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

    public void setLanguage(Language language) {
        this.language = language;
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
        entity.setProperty("language", language.getIsoCode());
        entity.setProperty("currency", currency.getIsoCode());
        return entity;
    }
}
