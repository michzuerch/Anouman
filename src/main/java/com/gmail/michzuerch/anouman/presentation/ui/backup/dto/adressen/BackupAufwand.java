package com.gmail.michzuerch.anouman.presentation.ui.backup.dto.adressen;

import java.time.LocalDateTime;

public class BackupAufwand {
    private String titel;
    private String bezeichnung;
    private LocalDateTime start;
    private LocalDateTime end;

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "BackupAufwand{" +
                "titel='" + titel + '\'' +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}