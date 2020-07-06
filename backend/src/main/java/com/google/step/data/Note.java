package com.google.step.data;

public final class Note {
    private final String leadId;
    private final String note;

    public Note (String leadId, String note){
        this.leadId = leadId;
        this.note = note;
    }

    public String getLeadId() {
        return leadId;
    }

    public String getNote() {
        return note;
    }
}