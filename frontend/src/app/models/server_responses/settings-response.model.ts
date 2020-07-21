export interface Settings {
    email: string;
    phone: string;
    receiveEmails: boolean;
    receiveTexts: boolean;
    language: LanguageType;
    currency: CurrencyType;
}


// https://angular.io/guide/i18n#refer-to-locales-by-id
export enum LanguageType {
    ENGLISH = 'en'
    // most likely want to support
    // Hindi 
    // Portuguese
    // Spanish
    // feel free to add more
}

// https://www.iso.org/iso-4217-currency-codes.html
export enum CurrencyType {
    USD = 'USD'
    // also want to support local currencies?
}
