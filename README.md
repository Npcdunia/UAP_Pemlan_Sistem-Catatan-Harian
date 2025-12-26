# 📓 Digital Diary System 2025

Aplikasi **Digital Diary System** adalah platform pencatatan harian berbasis Java yang menggabungkan efisiensi sistem Terminal (CLI) dengan kenyamanan antarmuka Grafis (GUI). Aplikasi ini dirancang untuk membantu pengguna mendokumentasikan kenangan, pikiran, dan suasana hati (mood) mereka secara permanen ke dalam basis data teks.

## 🚀 Fitur Utama

Aplikasi ini mencakup siklus **CRUD** lengkap dengan fitur tambahan:

* **Dashboard Utama**: Tampilan tabel yang rapi untuk melihat semua koleksi catatan beserta tanggal pembuatannya.
* **Manajemen Catatan**:
    * **Tambah**: Input catatan baru dengan judul, isi, dan kategori mood.
    * **Edit**: Memperbarui kenangan lama dengan data yang baru.
    * **Hapus**: Menghapus catatan secara permanen dengan konfirmasi keamanan.
* **Sistem Pencarian Pintar**: Mencari catatan tertentu berdasarkan judul tanpa harus menggulir seluruh daftar.
* **Persistensi Data**: Semua data disimpan secara otomatis ke dalam file `database_catatan.txt`.
* **Multi-Interface**: Mendukung penggunaan melalui Terminal (CLI) dan Jendela Aplikasi (GUI).

## 📂 Struktur Class

Proyek ini menggunakan prinsip enkapsulasi dan pengorganisasian kode yang baik:

| Class | Peran | Deskripsi |
| :--- | :--- | :--- |
| **Catatan** | *Model* | Representasi data catatan (Judul, Isi, Mood, Tanggal). |
| **CatatanManager** | *Controller* | Mengelola logika bisnis dan interaksi File I/O. |
| **AppGUI** | *View* | Antarmuka pengguna berbasis Swing dengan navigasi `CardLayout`. |
| **MainApp** | *Entry Point* | Class utama untuk menjalankan aplikasi versi GUI. |
| **main** | *Legacy/CLI* | Versi terminal untuk operasi cepat. |

## 🛠️ Detail Teknis

* **Penyimpanan**: Menggunakan `BufferedWriter` dengan *delimiter* pipe (`|`) untuk struktur data TXT.
* **Navigasi**: Mengimplementasikan `CardLayout` untuk transisi antar halaman dalam satu Frame.
* **Pencarian**: Menggunakan algoritma *linear search* dengan pencocokan *case-insensitive*.
* **Keamanan**: Menggunakan mapping indeks asli untuk menjamin akurasi update data dari hasil filter.

## 📝 Cara Menjalankan

1.  Pastikan Anda memiliki **Java JDK 8 atau lebih baru**.
2.  Buka proyek di IDE (IntelliJ/Eclipse/NetBeans).
3.  Jalankan class `MainApp.java` untuk GUI atau `main.java` untuk Terminal.
4.  File database akan otomatis terbuat sebagai `database_catatan.txt`.

---
**Dibuat untuk memenuhi tugas UAP Pemrograman Lanjut.**
