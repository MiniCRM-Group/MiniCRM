package com.google.minicrm.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Settings {
    private String phone;
    private boolean phoneNotificationsEnabled;
    private NotificationFrequencyOptions phoneNotificationFrequencyOptions;
    private String email;
    private boolean emailNotificationsEnabled;
    private NotificationFrequencyOptions emailNotificationFrequencyOptions;
    private String language;
    private String currency;

    public Settings(String email, String language, String currency) {
        this.phone = "";
        this.phoneNotificationsEnabled = false;
        this.phoneNotificationFrequencyOptions = new NotificationFrequencyOptions(false, false, false);
        this.email = email;
        this.emailNotificationsEnabled = false;
        this.emailNotificationFrequencyOptions = new NotificationFrequencyOptions(false, false, false);
        this.language = language;
        this.currency = currency;
    }
}

public class Notifications {
    private String sendEmail;
    private boolean areEnabled;
    private Set<NotificationFrequency> notificationFrequencies;

    public Notifications(NotificationType notificationType) {
        this.notificationType  = notificationType;
        this.areEnabled = false;
        this.notificationFrequencies = new HashSet<>();
    }
}

public class NotificationFrequencyOptions {
    private boolean onEveryLead;
    private boolean daily;
    private boolean weekly;

    public NotificationFrequencyOptions(boolean onEveryLead, boolean daily, boolean weekly) {
        this.onEveryLead = onEveryLead;
        this.daily = daily;
        this.weekly = weekly;
    }
}
