# T4-Mobile
---
## Nama : M. Wahyu Hilal Abroor
## NIM  : F1D02310123
---
### Deskripsi Singkat "Student Contact App"
Aplikasi manajemen data mahasiswa berbasis Android yang dibangun menggunakan bahasa Kotlin. Aplikasi ini dikembangkan sebagai pemenuhan Tugas Akhir untuk mata kuliah Mobile Programming. Fitur utama aplikasi ini meliputi sistem otentikasi sederhana, operasi CRUD (Create, Read, Update, Delete) data mahasiswa, pencarian data secara *real-time*, dan kustomisasi profil.

---

### Dokumentasi Aplikasi

1. **Halaman Login (`LoginActivity`)**
   Halaman awal aplikasi. Memiliki fitur "Remember Me" agar user tidak perlu login berulang kali.
    <img width="383" height="845" alt="image" src="https://github.com/user-attachments/assets/d6ba57bb-0e1b-4168-b6f7-9264f7606630" 
     <img width="375" height="839" alt="image" src="https://github.com/user-attachments/assets/1bc033d3-ceb9-45d6-a455-04bfae3d073b" />
     <img width="381" height="837" alt="image" src="https://github.com/user-attachments/assets/38a23418-2533-41cb-8884-24fc9ef84bf5" />




2. **Halaman Utama / Home (`HomeFragment`)**
   Menampilkan daftar mahasiswa dalam bentuk *RecyclerView*. Dilengkapi tombol tambah data, tombol Edit/Delete pada setiap kartu, dan fitur *Swipe to Delete* (geser untuk menghapus).
   <img width="385" height="850" alt="image" src="https://github.com/user-attachments/assets/29cb0257-8179-4432-9f91-8517b02b0a4a" />


3. **Halaman Pencarian (`SearchFragment`)**
   Fitur pencarian *real-time* menggunakan *TextWatcher*. Data otomatis tersaring setiap kali user mengetikkan nama atau NIM.
  <img width="379" height="834" alt="image" src="https://github.com/user-attachments/assets/ef50ce25-1f74-4d86-88e7-50752b0245df" />


4. **Form Tambah & Edit (`FormStudentActivity`)**
   Halaman dinamis yang berfungsi ganda. Jika menambah data, form akan kosong. Jika mengedit data, form akan terisi otomatis dengan data mahasiswa yang dipilih.
   <img width="378" height="839" alt="image" src="https://github.com/user-attachments/assets/aa659af0-99d9-4ea2-bb8f-4293f0a036b4" />
   <img width="376" height="837" alt="image" src="https://github.com/user-attachments/assets/83a6fe6c-5d58-4f7e-a3a6-7d7699d7ef26" />


5. **Halaman Detail & Catatan (`DetailActivity`)**
   Menampilkan detail mahasiswa dengan Avatar besar. Terdapat area teks untuk menulis dan memuat catatan khusus untuk mahasiswa tersebut.
   <img width="365" height="805" alt="image" src="https://github.com/user-attachments/assets/b14de6d9-6342-4fee-a85d-bd7425688f28" />
   <img width="380" height="842" alt="image" src="https://github.com/user-attachments/assets/8239b60d-3c7b-42c0-8c51-c6bd36683ba7" />



6. **Halaman Profil (`ProfileFragment`)**
   Berisi pengaturan aplikasi (Dark Mode, Font Size, Notifikasi) dan tombol Logout untuk menghapus sesi login.
   <img width="380" height="837" alt="image" src="https://github.com/user-attachments/assets/f3749337-5c6c-4d0a-b2f7-ed6e48a79679" />

   ---

### Metode Penyimpanan Data

Aplikasi ini mengimplementasikan 3 (tiga) metode penyimpanan lokal Android yang berbeda sesuai dengan fungsinya:

1. **Room Database (SQLite)**
   * **Penggunaan:** Untuk menyimpan daftar data mahasiswa (Nama, NIM, Prodi, Email, Semester).
   * **Alasan:** Sangat efisien untuk data terstruktur yang butuh operasi CRUD berulang. Penggunaan Room dikombinasikan dengan `Coroutines` dan `Flow` memungkinkan layar daftar mahasiswa ter-*update* secara *real-time* otomatis di latar belakang saat ada perubahan data.

2. **SharedPreferences**
   * **Penggunaan:** Untuk menyimpan status Login ("Remember Me"), *username*, dan status pengaturan di tab Profile (Switch Dark Mode, dll).
   * **Alasan:** Sangat ringan dan cepat untuk menyimpan data sederhana berbentuk *Key-Value Pair* tanpa perlu membuat tabel *database* yang rumit.

3. **Internal Storage**
   * **Penggunaan:** Untuk fitur simpan dan muat "Catatan" pada halaman Detail Mahasiswa.
   * **Alasan:** Cocok untuk menyimpan teks panjang (seperti file `.txt`) ke dalam memori internal HP yang bersifat *private* dan aman, sehingga tidak membebani ukuran *database* utama.

---
### Kendala dan Solusi

Selama proses pengembangan, terdapat beberapa kendala teknis yang berhasil diatasi:

1. **Aplikasi Gagal Dijalankan**
   * **Masalah:** Proses menjalankan aplikasi sering tiba-tiba terhenti karena error dari sistem Gradle.
   * **Solusi:** Saya melakukan pengecekan mendalam melalui panel log Build Output untuk menemukan lokasi baris kode yang bermasalah. Solusi spesifik yang diterapkan meliputi:
     - Sinkronisasi ID XML dan Kotlin: Memperbaiki kesalahan Unresolved Reference dengan mengganti nama ID lama menjadi ID baru (contohnya mengganti etNoteContent menjadi etNote) agar sesuai dengan desain XML terbaru pada file DetailActivity.kt dan LoginActivity.kt.
     - Melengkapi Parameter Adapter: Memperbarui inisialisasi StudentAdapter di dalam SearchFragment.kt dengan menambahkan parameter onItemClick. Hal ini dilakukan agar selaras dengan perubahan struktur adapter yang telah diperbarui di halaman utama.
     - Pembersihan Struktur XML: Menghapus baris kosong atau spasi pada bagian paling atas file bottom_nav_menu.xml. Hal ini dilakukan karena sistem Gradle mewajibkan deklarasi XML berada tepat di baris pertama agar proses kompilasi sumber daya (resource) tidak gagal.
     - Validasi Manifest: Memastikan semua aktivitas (Activity) yang baru dibuat telah terdaftar di dalam file AndroidManifest.xml untuk menghindari kegagalan sistem saat aplikasi mencoba berpindah halaman.
