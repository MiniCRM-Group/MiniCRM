package com.google.minicrm.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Currency {
    @SerializedName("INR ₹") INDIAN_RUPEE("INR", "INR ₹"),
    @SerializedName("BRL R$") BRAZILIAN_REAL("BRL", "BRL R$"),
    @SerializedName("MXN $") MEXICAN_PESO("MXN", "MXN $"),
    @SerializedName("USD $") US_DOLLAR("USD", "USD $");

    private String isoCode;
    private String displayed;
    private static final Map<String, Currency> map = generateMap();
    /**
     * All the supported currencies in a format that can be parsed into JSON for web client.
     */
    public static final List<Map<String, String>> supportedCurrencies = generateSupportedCurrencies();

    Currency(String isoCode, String displayed) {
        this.isoCode = isoCode;
        this.displayed = displayed;
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
        return map.getOrDefault(isoCode, US_DOLLAR);
    }

    private static Map<String, Currency> generateMap() {
        Map<String, Currency> map = new HashMap<>();
        for(Currency currency: Currency.values()) {
            map.put(currency.isoCode, currency);
        }
        return map;
    }

    private static List<Map<String, String>> generateSupportedCurrencies() {
        List<Map<String, String>> list = new ArrayList<>();
        for(Currency currency: Currency.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("displayed", currency.displayed);
            map.put("isoCode", currency.isoCode);
            list.add(map);
        }
        return list;
    }
}
