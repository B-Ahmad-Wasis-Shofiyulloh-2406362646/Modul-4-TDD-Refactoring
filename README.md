## Reflection
<details>
<summary>Modul 1 - Reflection 1</summary>

Dalam implementasi dua fitur baru yang berupa edit dan delete, saya sudah mencoba mengimplementasikan prinsip-prinsip clean and secure code. Penggunaan metode POST untuk fitur delete mengurangi vulnerabilitas situs dibandingkan menggunakan metode GET. Saya juga menggunakan nama yang deskriptif terhadap fungsi dan variabel yang bersangkutan. Saya juga memastikan bahwa sebuah fungsi hanya bertujuan untuk melaksanakan satu buah tugas. Sejauh ini, saya masih belum menggunakan comment sama sekali karena saya merasa kode saya masih bersifat self-explanatory. Improvement yang dapat saya pikirkan untuk kedepannya adalah penggunaan null dan exception handling. Selain itu, improvement lain yang dapat saya pikirkan adalah penambahan validasi input.

</details>

<details>
<summary>Modul 1 - Reflection 2</summary>

Mengenai jumlah unit test yang dibutuhkan sebuah class, tidak ada jumlah pasti yang tepat untuk class yang berbeda. Sebagai indikasi, code coverage dapat menunjukkan seberapa besar persentase kodemu yang sudah dites. Namun, 100% code coverage tidak menjamin bahwa kode kita aman dari error ataupun bug, persentase tersebut hanya menjamin bahwa semua baris kode telah dieksekusi dalam proses testing. Tetap dibutuhkan fungsi testing yang bagus untuk memastikan semua perilaku, edge cases, dan error ter cover dengan baik.

Melakukan duplikasi terhadap setup functional test yang sudah digunakan melanggar salah satu prinsip clean code, yaitu DRY (Don't Repeat Yourself). Improvement yang dapat saya pikirkan adalah penggunaan helper method untuk assertion yang digunakan berulang kali.

</details>

<details>
<summary>Modul 2 - Reflection</summary>

Daftar code quality issues yang saya perbaiki:

1. Penggunaan Field Injection menggunakan @AutoWired di ProductController dan ProductServiceImpl telah saya refactor menjadi menggunakan konstruktor.
2. Tidak adanya Assertion pada beberapa test yang ada telah saya ubah dengan menambahkan assertDoesNotThrow().
3. Deklarasi throw exception yang tidak diperlukan pada functional test telah saya hapus.
4. Public modifier yang tidak diperlukan pada test yang ada telah saya hapus. 
5. Kumpulan dependency pada build gradle telah saya rapikan dengan mengelompokkannya berdasarkan konfigurasi.
6. Penggunaan anchor tag pada tombol yang mengubah data (delete) telah saya ubah menjadi button di dalam form dengan metode POST.


Ya, saya cukup yakin bahwa implementasi yang saya terapkan sudah memenuhi definisi CI dan CD. Untuk CI, setiap kali saya melakukan push kode ke repositori GitHub, beberapa workflow telah berjalan secara otomatis untuk membangun projek, menjalankan unit test dan functional test, serta menganalasis masalah kualitas dari kode yang ada melalui SonarCloud. Implementasi ini memastikan semua perubahan telah menjalani testing yang cukup dan tidak merusak fungsionalitas yang sudah ada.

Untuk CD, setiap kali proses CI sukses dan kode di merge ke branch main, Railway secara otomatis melakukan pull kepada perubahan yang terbaru dan melakukan deploy ke cloud tanpa usaha manual. Otomasi yang berbasis pull ini menciptakan pipeline yang mulus di mana kode berpindah dari tahap development ke tahap production dengan cepat dan bisa diandalkan. 

</details>

<details>
<summary>Modul 3 - Reflection</summary>

Prinsip SOLID yang saya terapkan:

1. SRP: Saya memisahkan CarController dengan ProductController ke kelas yang berbeda. 
2. OCP: Saya mengubah metode update di folder repository dengan menggunakan Java Streams and mendelegasikan field yang di update ke modelnya masing-masing.
3. LSP: Saya membuang inheritance di antara CarController dan ProductController.
4. ISP: Saya memisahkan interface antara product dan car.
5. DIP: Saya mengimplementasikan interface yang ada sehingga semua kelas sekarang bergantung ke interface dan bukan kelas konkrit. Saya juga mengubah field injection menjadi constructor injection.

Keuntungan penggunaan prinsip SOLID:

1. Karena penggunaan OCP, saya dapat menambah atribut ke model tanpa perlu menyentuh repository.
2. Karena penggunaan DIP, saya dapat melakukan unit testing yang terisolasi.
3. Karena penggunaan ISP, saya memastikan bahwa product dan car independen satu sama lain.

Kerugian tidak menggunakan prinsip SOLID:

1. Jika tidak menggunakan OCP, saya akan perlu melakukan manual setter untuk setiap atribut ketika ada perubahan atribut pada model.
2. Jika tidak menggunakan DIP, sebuah service akan menjadi hard-coded ke sebuah repository yang spesifik.

</details>