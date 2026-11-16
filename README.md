# 🎬 SINEMAKU — Katalog Film CLI

Aplikasi katalog film berbasis CLI yang dibangun menggunakan **Kotlin** dengan prinsip **Pemrograman Berorientasi Objek (PBO)**.

> Proyek UAS mata kuliah Pemrograman Berorientasi Objek

---

## 👥 Tim Pengembang

| Nama   | NIM        | Peran                          |
|--------|------------|-------------------------------|
| Agisna | 2XXXXXXX   | Project Lead / Arsitek         |
| Fawwaz | 2XXXXXXX   | Domain Developer (`model/`)    |
| Miftah | 2XXXXXXX   | Business Logic (`service/`)    |
| Anida  | 2XXXXXXX   | UI/CLI Developer (`Main.kt`)   |
| Maula  | 2XXXXXXX   | QA + Data & Persistence        |
| Apip   | 2XXXXXXX   | DevOps                         |

---

## 🏗️ Arsitektur Proyek

```
sinemaku/
├── src/main/kotlin/
│   ├── model/          # Domain model (Film, Genre, dsb)
│   ├── service/        # Business logic
│   ├── exception/      # Custom exceptions
│   └── Main.kt         # Entry point & UI CLI
├── src/test/kotlin/    # Unit tests
├── data/               # Penyimpanan data (JSON/CSV)
├── Dockerfile
├── docker-compose.yml
└── build.gradle.kts
```

---

## 🚀 Cara Menjalankan

### Dengan Gradle
```bash
./gradlew run --console=plain
```

### Dengan Docker
```bash
docker compose up --build
```

### Build distribusi
```bash
./gradlew installDist
./build/install/sinemaku/bin/sinemaku
```

---

## 🔧 Prasyarat

- JDK 21+
- Gradle 8.10+ (atau gunakan wrapper `./gradlew`)
- Docker & Docker Compose (opsional)

---

## 🌿 Alur Branching

```
main        ← stable, production-ready
└── develop ← integrasi
    ├── feature/model-film
    ├── feature/service-film
    ├── feature/cli-ui
    └── feature/persistence
```

**Aturan:**
- Setiap fitur dikerjakan di branch `feature/nama-fitur`
- PR ke `develop`, bukan langsung ke `main`
- `main` hanya diupdate via PR dari `develop`

---

## 📋 Fitur

- [ ] Tambah film baru
- [ ] Lihat daftar film
- [ ] Cari film berdasarkan judul / genre
- [ ] Update data film
- [ ] Hapus film
- [ ] Simpan & muat data dari file

---

## 📄 Lisensi

Proyek ini dibuat untuk keperluan akademik.
