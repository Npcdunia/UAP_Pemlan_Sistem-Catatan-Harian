package org.example;

import java.time.LocalDate;

public class Catatan {
    private String judul;
    private String isi;
    private String mood;
    private LocalDate tanggal;

    public Catatan(String judul, String isi, String mood) {
        this.judul = judul;
        this.isi = isi;
        this.mood = mood;
        this.tanggal = LocalDate.now();
    }

    public Catatan(String judul, String isi, String mood, String tanggal) {
        this.judul = judul;
        this.isi = isi;
        this.mood = mood;
        this.tanggal = LocalDate.parse(tanggal);
    }


    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    @Override
    public String toString() {
        return judul + "|" + isi + "|" + mood + "|" + tanggal;
    }
}