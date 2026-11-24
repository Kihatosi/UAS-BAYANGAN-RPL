# Board Game Buddy  (Spring AI in Action)

Repositori praktikum **Spring AI in Action (by Craig Walls)** - Chapter 1 & Chapter 2.
Mata Kuliah: **Rekayasa Perangkat Lunak Lanjut**  
Penyusun: **Rusmin Nuryadin (NIM: 12409011050120)**  
Dosen Pengampu: **Rizal Broer (rizalbroer@uinjkt.ac.id)**  
Program Studi: **Teknik Informatika - FST UIN Syarif Hidayatullah Jakarta (2026)**

---

##  Daftar Lab & Fitur yang Diimplementasikan

### BAB II: CHAPTER 1 - Getting Started with Spring AI
- **Lab 1.1**: Inisialisasi Project Spring Boot dengan Spring Initializr (Gradle Groovy, Java 21, Spring Boot, Spring AI BOM 1.0.0-M3/1.0.1).
- **Lab 1.2**: Implementasi Service & REST Controller (`BoardGameService`, `Question`, `Answer`, `SpringAiBoardGameService`, `AskController` POST `/ask`).
- **Lab 1.3**: Konfigurasi API Key via Environment Variable `SPRING_AI_OPENAI_API_KEY` untuk mencegah kebocoran secret.
- **Lab 1.4**: Pengujian Deterministik dengan **WireMock** (`SpringAiBoardGameServiceWireMockTests`) menggunakan canned JSON `test-openai-response.json` ("Paris").
- **Lab 1.5**: Menjalankan aplikasi via `./gradlew bootRun` dan testing endpoint dengan `curl` & `httpie`.
- **Lab 1.6**: Eksplorasi pemilihan model LLM (`gpt-4o-mini`, `gpt-4.1-nano`, Azure OpenAI).
- **Lab 1.7**: Dukungan Model LLM Lokal dengan **Ollama** (`gemma:2b`, `mistral:7b`) tanpa API key.

### BAB III: CHAPTER 2 - Evaluating Generated Responses
- **Lab 2.1**: Pengujian Relevansi Respons LLM dengan `RelevancyEvaluator` (`SpringAiBoardGameServiceTests.evaluateRelevancy`).
- **Lab 2.2**: Pengujian Akurasi Faktual dengan `FactCheckingEvaluator` (`SpringAiBoardGameServiceTests.evaluateFactualAccuracy`).
- **Lab 2.3**: Runtime Self-Evaluation & Fault Tolerance menggunakan **Spring Retry** (`SelfEvaluatingBoardGameService`, `AnswerNotRelevantException`, `@Retryable`, `@Recover`).

---

##  Cara Menjalankan Aplikasi

### 1. Menentukan Environment Variable API Key
Sebelum menjalankan aplikasi dengan OpenAI, atur environment variable:
- **Windows (Command Prompt)**:
  ```cmd
  set SPRING_AI_OPENAI_API_KEY=sk-your-openai-api-key
  ```
- **Windows (PowerShell)**:
  ```powershell
  $env:SPRING_AI_OPENAI_API_KEY="sk-your-openai-api-key"
  ```
- **Linux / macOS**:
  ```bash
  export SPRING_AI_OPENAI_API_KEY="sk-your-openai-api-key"
  ```

### 2. Menjalankan Unit & Integration Test (WireMock)
Untuk menjalankan WireMock test deterministik tanpa memerlukan koneksi/kuota OpenAI:
```bash
./gradlew test --tests *WireMockTests
```

### 3. Menjalankan Evaluator Tests
Pastikan API Key sudah diset, lalu jalankan:
```bash
./gradlew test --tests *SpringAiBoardGameServiceTests
```

### 4. Menjalankan REST Server
```bash
./gradlew bootRun
```

Lalu uji endpoint POST `/ask`:
```bash
curl -X POST http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d "{\"question\": \"Why is the sky blue?\"}"
```

---

## 🏛️ Arsitektur & Class Diagram

```
+------------------------------------+
|         <<interface>>              |
|        BoardGameService            |
+------------------------------------+
| + askQuestion(Question): Answer    |
+------------------------------------+
                  ^
                  |
     +------------+------------+
     |                         |
+-------------------------+  +----------------------------------+
| SpringAiBoardGameService|  |  SelfEvaluatingBoardGameService  |
+-------------------------+  +----------------------------------+
| - chatClient: ChatClient|  | - chatClient: ChatClient         |
|                         |  | - evaluator: RelevancyEvaluator  |
+-------------------------+  +----------------------------------+
| + askQuestion()         |  | + askQuestion() [@Retryable]     |
|                         |  | + recover() [@Recover]           |
|                         |  | - evaluateRelevancy()            |
+-------------------------+  +----------------------------------+
```

---

## 📌 Riwayat Commit (Versioning)
1. `init: project Spring Initializr` (Lab 1.1)
2. `feat: BoardGameService, controller, API key config` (Lab 1.2 & 1.3)
3. `test: WireMock` (Lab 1.4)
4. `feat: model Ollama, README` (Lab 1.5 - 1.7)
5. `test: RelevancyEvaluator` (Lab 2.1)
6. `test: FactCheckingEvaluator` (Lab 2.2)
7. `feat: self-evaluating service + retry` (Lab 2.3)
8. `docs: update README and comprehensive documentation`
