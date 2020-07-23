package com.google.minicrm.data;

import com.google.gson.annotations.SerializedName;

public enum Language {
    @SerializedName("English") ENGLISH("en"),
    @SerializedName("Spanish") SPANISH("es"),
    @SerializedName("Hindi") HINDI("hi"),
    @SerializedName("Portuguese") PORTUGUESE("pt");

    private String isoCode;

    Language(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getIsoCode() {
        return  this.isoCode;
    }

    /**
     * Returns a Language enum given the ISO 639-1 code.
     * @param isoCode Must be in ISO 639-1 and must be supported.
     * @return Supported Language enum or default (ENGLISH).
     */
    public static Language fromIsoCode(String isoCode) {
        for(Language language: Language.values()) {
            if(language.isoCode.equals(isoCode)) {
                return language;
            }
        }
        return ENGLISH;
    }
}
