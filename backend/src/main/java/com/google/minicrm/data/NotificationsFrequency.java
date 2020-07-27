package com.google.minicrm.data;

import com.google.gson.annotations.SerializedName;

import java.util.*;

public enum NotificationsFrequency {
	@SerializedName("Never") NEVER("Never"),
	@SerializedName("On Every Lead") ON_EVERY_LEAD("On Every Lead"),
	@SerializedName("Daily") DAILY("Daily"),
	@SerializedName("Weekly") WEEKLY("Weekly");

	private String displayed;
	private static final Map<String, NotificationsFrequency> map = generateMap();
	/**
	 * All the supported notifications frequencies in a format that can be parsed into JSON for web client.
	 */
	public static final List<Map<String, String>> supportedNotificationsFrequencies =
			supportedNotificationsFrequencies();

	NotificationsFrequency(String displayed) {
		this.displayed = displayed;
	}

	public String getDisplayed() {
		return this.displayed;
	}

	/**
	 * Retrieve NotificationsFrequency enum based on displayed name.
	 * @param displayed The displayed value on the web app.
	 * @return The corresponding enum or default (NEVER).
	 */
	public static NotificationsFrequency fromDisplayed(String displayed) {
		return  map.getOrDefault(displayed, NEVER);
	}

	private static Map<String, NotificationsFrequency> generateMap() {
		Map<String, NotificationsFrequency> map = new HashMap<>();
		for(NotificationsFrequency nf: NotificationsFrequency.values()) {
			map.put(nf.displayed, nf);
		}
		return map;
	}

	private static List<Map<String, String>> supportedNotificationsFrequencies() {
		List<Map<String, String>> list = new ArrayList<>();
		for(NotificationsFrequency nf: NotificationsFrequency.values()) {
			Map<String, String> map = new HashMap<>();
			map.put("id", nf.toString());
			map.put("displayed", nf.displayed);
			list.add(map);
		}
		return list;
	}
}
