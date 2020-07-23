package com.google.minicrm.data;

import com.google.appengine.api.datastore.EmbeddedEntity;

public final class NotificationsFrequency {
    private boolean onEveryLead;
    private boolean daily;
    private boolean weekly;

    public NotificationsFrequency(boolean onEveryLead, boolean daily, boolean weekly) {
        this.onEveryLead = onEveryLead;
        this.daily = daily;
        this.weekly = weekly;
    }

    public NotificationsFrequency(EmbeddedEntity embeddedEntity) {
    	this.onEveryLead = (boolean) embeddedEntity.getProperty("onEveryLead");
		this.daily = (boolean) embeddedEntity.getProperty("daily");
		this.weekly = (boolean) embeddedEntity.getProperty("weekly");
	}

	public boolean isOnEveryLead() {
		return this.onEveryLead;
	}

	public void setOnEveryLead(boolean onEveryLead) {
		this.onEveryLead = onEveryLead;
	}

	public boolean isDaily() {
		return this.daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}

	public boolean isWeekly() {
		return this.weekly;
	}

	public void setWeekly(boolean weekly) {
		this.weekly = weekly;
    }
    
    public EmbeddedEntity asEmbeddedEntity() {
    	EmbeddedEntity embeddedEntity = new EmbeddedEntity();
    	embeddedEntity.setProperty("onEveryLead", onEveryLead);
    	embeddedEntity.setProperty("daily", daily);
    	embeddedEntity.setProperty("weekly", weekly);
    	return embeddedEntity;
    }
}
