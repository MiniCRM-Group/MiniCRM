export interface SettingsResponse {
    settings: Settings;
    supportedCurrencies: Currency[];
    supportedNotifsFreqs: NotificationsFrequency[];
}

export interface Settings {
    email: string;
    emailNotificationsFrequency: string;
    phone: string;
    phoneNotificationsFrequency: string;
    currency: string;
}

export interface NotificationsFrequency {
    id: string;
    displayed: string;
}

export interface Currency {
    isoCode: string;
    displayed: string;
}

export interface Language {
    isoCode: string;
    displayed: string;
}
