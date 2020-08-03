package com.google.minicrm.data;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Language {
    @SerializedName("English") ENGLISH("en", "English"),
    @SerializedName("Spanish") SPANISH("es", "Spanish"),
    @SerializedName("Hindi") HINDI("hi", "Hindi"),
    @SerializedName("Portuguese") PORTUGUESE("pt", "Portuguese");

    private static Map<String, Language> map = generateMap();
    /**
     * All the supported languages in a format that can be parsed into JSON for web client.
     */
    public static List<Map<String, String>> supportedLanguages = generateSupportedLanguages();
    private String displayed;
    private String isoCode;

    Language(String isoCode, String displayed) {
        this.isoCode = isoCode;
        this.displayed = displayed;
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
        return map.getOrDefault(isoCode, ENGLISH);
    }

    private static Map<String, Language> generateMap() {
        Map<String, Language> map = new HashMap<>();
        for(Language language: Language.values()) {
            map.put(language.isoCode, language);
        }
        return  map;
    }

    private static List<Map<String, String>> generateSupportedLanguages(){
        List<Map<String, String>> list = new ArrayList<>();
        for(Language language: Language.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("displayed", language.displayed);
            map.put("isoCode", language.isoCode);
            list.add(map);
        }
        return list;
    }
}

