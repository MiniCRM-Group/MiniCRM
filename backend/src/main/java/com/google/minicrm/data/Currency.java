package com.google.minicrm.data;

public enum Currency {
    INDIAN_RUPEE("INR"),
    BRAZILIAN_REAL("BRL"),
    MEXICAN_PESO("MXN"),
    US_DOLLAR("USD");

    private String isoCode;

    Currency(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getIsoCode() {
        return  this.isoCode;
    }

    /**
     * Returns a Currency enum given the ISO 4217 code.
     * @param isoCode Must be in ISO 4217 and must be supported.
     * @return Supported Currency enum or default (US_DOLLAR).
     */
    public static Currency fromIsoCode(String isoCode) {
        for(Currency currency: Currency.values()) {
            if(currency.isoCode.equals(isoCode)) {
                return currency;
            }
        }
        return US_DOLLAR;
    }
}
