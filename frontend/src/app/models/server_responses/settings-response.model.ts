export interface SettingsResponse {
    settings: Settings;
    supportedCurrencies: Currency[];
    supportedLanguages: Language[];
}

export interface Settings {
    settingsId: number;
    email: string;
    emailNotificationsEnabled: boolean;
    emailNotificationsFrequency: NotificationsFrequency;
    phone: string;
    phoneNotificationsEnabled: boolean;
    phoneNotificationsFrequency: NotificationsFrequency;
    language: string;
    currency: string;
}

export interface NotificationsFrequency {
    onEveryLead: boolean;
    daily: boolean;
    weekly: boolean;
}

export interface Currency {
    currency: string;
    isoCode: string;
}

export interface Language {
    language: string;
    isoCode: string;
}

export function displayNotificationFrequencies(notifFreq: NotificationsFrequency): string {
    let freqs = [];
    if(notifFreq.onEveryLead) {
        freqs.push("On Every Lead");
    }
    if(notifFreq.daily) {
        freqs.push("Daily");
    }
    if(notifFreq.weekly) {
        freqs.push("Weekly");
    }
    return freqs.join(", ");
}