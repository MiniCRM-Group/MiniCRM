package com.google.minicrm.data;

import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public final class Settings implements DatastoreObject {
    public static String KIND_NAME = "Settings";

    // used for maintaining uniqueness
    private static long CURRENT_ID = 1;
    private transient Key advertiserKey;
    private long settingsId;

    private String email;
    private boolean emailNotificationsEnabled;
    // no setter because you should get and then set instance vars
    private NotificationsFrequency emailNotificationsFrequency;
    private String phone;
    private boolean phoneNotificationsEnabled;
    // no setter because you should get and then set instance vars
    private NotificationsFrequency phoneNotificationsFrequency;
    private Language language;
    private Currency currency;

    // default settings created when user first created
    public Settings(Key advertiserKey, String email) {
        this.advertiserKey = advertiserKey;
        this.settingsId = CURRENT_ID++;
        this.email = email;
        this.language = Language.ENGLISH;
        this.currency = Currency.US_DOLLAR;
        // defaults
        this.emailNotificationsEnabled = false;
        this.emailNotificationsFrequency = new NotificationsFrequency(true, false, false);
        this.phone = "";
        this.phoneNotificationsEnabled = false;
        this.phoneNotificationsFrequency = new NotificationsFrequency(true, false, false);
    }

    public Settings(Entity entity) {
        this.advertiserKey = entity.getParent();
        this.settingsId = (long) entity.getProperty("settingsId");
        this.email = (String) entity.getProperty("email");
        this.emailNotificationsEnabled = (boolean) entity.getProperty("emailNotificationsEnabled");
        this.emailNotificationsFrequency =
                new NotificationsFrequency((EmbeddedEntity) entity.getProperty("emailNotificationsFrequency"));
        this.phone = (String) entity.getProperty("phone");
        this.phoneNotificationsEnabled = (boolean) entity.getProperty("phoneNotificationsEnabled");
        this.phoneNotificationsFrequency =
                new NotificationsFrequency((EmbeddedEntity) entity.getProperty("phoneNotificationsFrequency"));
        this.language = Language.fromIsoCode((String) entity.getProperty("language"));
        this.currency = Currency.fromIsoCode((String) entity.getProperty("currency"));
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public NotificationsFrequency getEmailNotificationsFrequency() {
        return emailNotificationsFrequency;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isPhoneNotificationsEnabled() {
        return phoneNotificationsEnabled;
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

    public void setEmailNotificationsEnabled(boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoneNotificationsEnabled(boolean phoneNotificationsEnabled) {
        this.phoneNotificationsEnabled = phoneNotificationsEnabled;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Entity asEntity() {
        Key key = KeyFactory.createKey(this.advertiserKey, KIND_NAME, this.settingsId);
        Entity entity = new Entity(key);
        entity.setProperty("settingsId", settingsId);
        entity.setProperty("email", email);
        entity.setProperty("emailNotificationsEnabled", emailNotificationsEnabled);
        entity.setProperty("emailNotificationsFrequency", emailNotificationsFrequency.asEmbeddedEntity());
        entity.setProperty("phone", phone);
        entity.setProperty("phoneNotificationsEnabled", phoneNotificationsEnabled);
        entity.setProperty("phoneNotificationsFrequency", phoneNotificationsFrequency.asEmbeddedEntity());
        entity.setProperty("language", language.getIsoCode());
        entity.setProperty("currency", currency.getIsoCode());
        return entity;
    }
}
