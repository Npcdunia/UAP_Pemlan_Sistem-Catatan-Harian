package org.example;

import java.io.*;
import java.util.*;

public class CatatanManager {
    private ArrayList<Catatan> daftarCatatan;
    private final String FILE_NAME = "database_catatan.txt";

    public CatatanManager() {
        this.daftarCatatan = new ArrayList<>();
        File file = new File(FILE_NAME);
        try {
            if (file.createNewFile()) {
                System.out.println("File baru berhasil dibuat: " + file.getName());
            }
        } catch (IOException e) {
            System.err.println("Gagal membuat file inisial: " + e.getMessage());
        }
        muatDataDariFile();
    }

    public void tambahCatatan(Catatan c) {
        daftarCatatan.add(c);
        simpanDataKeFile();
    }

    public void updateCatatan(int index, String judulBaru, String isiBaru, String moodBaru) {
        if (index >= 0 && index < daftarCatatan.size()) {
            Catatan c = daftarCatatan.get(index);
            c.setJudul(judulBaru);
            c.setIsi(isiBaru);
            c.setMood(moodBaru);
            simpanDataKeFile();
        }
    }

    public void hapusCatatan(int index) {
        if (index >= 0 && index < daftarCatatan.size()) {
            daftarCatatan.remove(index);
            simpanDataKeFile();
        }
    }

    public List<Catatan> cariCatatan(String keyword) {
        List<Catatan> hasil = new ArrayList<>();
        for (Catatan c : daftarCatatan) {
            if (c.getJudul().toLowerCase().contains(keyword.toLowerCase())) {
                hasil.add(c);
            }
        }
        return hasil;
    }

    public void urutkanBerdasarkanJudul() {
        daftarCatatan.sort(Comparator.comparing(Catatan::getJudul));
        simpanDataKeFile();
    }

    private void simpanDataKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Catatan c : daftarCatatan) {
                String isiAman = c.getIsi().replace("\n", "\\n");
                writer.write(c.getJudul() + "|" + isiAman + "|" + c.getMood() + "|" + c.getTanggal());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Gagal simpan file: " + e.getMessage());
        }
    }

    private void muatDataDariFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String baris;
            while ((baris = reader.readLine()) != null) {
                String[] data = baris.split("\\|");
                if (data.length == 4) {
                    String isiAsli = data[1].replace("\\n", "\n");
                    daftarCatatan.add(new Catatan(data[0], isiAsli, data[2], data[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal muat file: " + e.getMessage());
        }
    }

    public ArrayList<Catatan> getDaftarCatatan() {
        return daftarCatatan;
    }
}