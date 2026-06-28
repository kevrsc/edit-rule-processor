# FAFSA Edit Rule Processor

## Solution: Build, Run & Test

**Current status:** Phase 1 (project setup) is complete. The service is a runnable
Spring Boot scaffold; validation API and edit rules are not implemented yet.

### What's in place

- Java 25 + Spring Boot 4.1 + Gradle 9 (`./gradlew`)
- Virtual threads enabled (`spring.threads.virtual.enabled=true`)
- Spring Boot entry point: `com.fafsaeditruleprocessor.FafsaEditProcessorApplication`
- Server port 8080 (no HTTP endpoints exposed yet)

### Prerequisites

- JDK 25
- No database or external services required

### Build

```bash
./gradlew bootJar
```

Windows:

```powershell
.\gradlew.bat bootJar
```

### Run

```bash
./gradlew bootRun
```

Windows:

```powershell
.\gradlew.bat bootRun
```

The application starts on `http://localhost:8080`. There is no validation
endpoint to call yet.

### Test

```bash
./gradlew test
```

No test sources are present yet; the task succeeds with zero tests. After
validation logic is added, use:

```bash
./gradlew clean test
```

for pre-push verification.

---