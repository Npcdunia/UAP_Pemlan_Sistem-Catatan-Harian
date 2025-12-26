package org.example;

import java.util.Scanner;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        CatatanManager manager = new CatatanManager();
        Scanner input = new Scanner(System.in);
        boolean berjalan = true;

        while (berjalan) {
            System.out.println("\n=== DIARY SYSTEM (TERMINAL VERSION) ===");
            System.out.println("1. Tambah Catatan (Create)");
            System.out.println("2. Lihat Semua Catatan (Read)");
            System.out.println("3. Update Catatan (Update)");
            System.out.println("4. Hapus Catatan (Delete)");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");

            String pilihan = input.nextLine();

            switch (pilihan) {
                case "1":
                    System.out.print("Masukkan Judul: ");
                    String judul = input.nextLine();
                    System.out.print("Masukkan Isi: ");
                    String isi = input.nextLine();
                    System.out.print("Masukkan Mood: ");
                    String mood = input.nextLine();

                    manager.tambahCatatan(new Catatan(judul, isi, mood));
                    System.out.println("Berhasil disimpan ke .txt!");
                    break;

                case "2":
                    System.out.println("\n--- DAFTAR CATATAN ---");
                    ArrayList<Catatan> list = manager.getDaftarCatatan();
                    if (list.isEmpty()) {
                        System.out.println("Belum ada catatan.");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            Catatan c = list.get(i);
                            System.out.println(i + ". [" + c.getTanggal() + "] " + c.getJudul() + " (Mood: " + c.getMood() + ")");
                            System.out.println("   Isi: " + c.getIsi());
                        }
                    }
                    break;

                case "3":
                    ArrayList<Catatan> listUpdate = manager.getDaftarCatatan();
                    if (listUpdate.isEmpty()) {
                        System.out.println("Tidak ada catatan untuk diupdate.");
                    } else {
                        boolean updateBerhasil = false;
                        while (!updateBerhasil) {
                            try {
                                System.out.println("\n--- Pilih Catatan yang Akan Diupdate ---");
                                for (int i = 0; i < listUpdate.size(); i++) {
                                    System.out.println(i + ". " + listUpdate.get(i).getJudul());
                                }
                                System.out.print("Masukkan nomor indeks (atau -1 untuk batal): ");
                                int idx = Integer.parseInt(input.nextLine());

                                if (idx == -1) break;

                                if (idx >= 0 && idx < listUpdate.size()) {
                                    Catatan lama = listUpdate.get(idx);

                                    System.out.println("\n[Judul Lama: " + lama.getJudul() + "]");
                                    System.out.print("Input Judul baru (Enter jika tetap): ");
                                    String jdlBaru = input.nextLine();
                                    if (jdlBaru.isEmpty()) jdlBaru = lama.getJudul();

                                    System.out.println("\n[Mood Lama: " + lama.getMood() + "]");
                                    System.out.print("Input Mood baru (Enter jika tetap): ");
                                    String moodBaru = input.nextLine();
                                    if (moodBaru.isEmpty()) moodBaru = lama.getMood();

                                    System.out.println("\n[Isi Lama: " + lama.getIsi() + "]");
                                    System.out.println("Pilih mode edit isi:");
                                    System.out.println("1. Tambah teks di akhir");
                                    System.out.println("2. Ganti total isi");
                                    System.out.println("3. Tetap (Tidak berubah)");
                                    System.out.print("Pilihan: ");
                                    String modeIsi = input.nextLine();

                                    String isiBaru = lama.getIsi();
                                    if (modeIsi.equals("1")) {
                                        System.out.print("Masukkan teks tambahan: ");
                                        isiBaru += " " + input.nextLine();
                                    } else if (modeIsi.equals("2")) {
                                        System.out.print("Masukkan isi baru total: ");
                                        isiBaru = input.nextLine();
                                    }

                                    manager.updateCatatan(idx, jdlBaru, isiBaru, moodBaru);
                                    System.out.println("\nBerhasil! Catatan telah diperbarui di file .txt.");
                                    updateBerhasil = true;
                                } else {
                                    System.out.println("Indeks tidak ditemukan!");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Input harus angka!");
                            }
                        }
                    }
                    break;

                case "4":
                    ArrayList<Catatan> listHapus = manager.getDaftarCatatan();
                    if (listHapus.isEmpty()) {
                        System.out.println("Tidak ada catatan yang bisa dihapus.");
                    } else {
                        boolean indeksValid = false;
                        while (!indeksValid) {
                            try {
                                System.out.println("\n--- Daftar Catatan ---");
                                for (int i = 0; i < listHapus.size(); i++) {
                                    System.out.println(i + ". " + listHapus.get(i).getJudul());
                                }

                                System.out.print("Masukkan nomor indeks yang ingin dihapus (atau ketik -1 untuk batal): ");
                                int idxHapus = Integer.parseInt(input.nextLine());

                                if (idxHapus == -1) {
                                    System.out.println("Batal menghapus.");
                                    indeksValid = true;
                                } else if (idxHapus >= 0 && idxHapus < listHapus.size()) {

                                    manager.hapusCatatan(idxHapus);
                                    System.out.println("Data berhasil dihapus secara permanen dari file .txt!");
                                    indeksValid = true;
                                } else {
                                    System.out.println("Error: Indeks " + idxHapus + " tidak ditemukan! Silakan coba lagi.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Masukkan harus berupa angka indeks!");
                            }
                        }
                    }
                    break;

                case "5":
                    berjalan = false;
                    System.out.println("Keluar program...");
                    break;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}