# FAFSA Edit Rule Processor

## Solution: Build, Run & Test

**Current status:** Phases 1–2 are complete. The project has a runnable Spring Boot
service with domain models, an extensible edit-rule framework, API DTOs, and
application wiring. The seven validation rules and HTTP endpoint are not
implemented yet (Phase 3).

### What's in place

**Infrastructure (Phase 1)**

- Java 25, Spring Boot 4.1, Gradle 9 (`./gradlew`)
- Virtual threads enabled (`spring.threads.virtual.enabled=true`)
- Entry point: `com.fafsaeditruleprocessor.FafsaEditProcessorApplication`
- Server port 8080
- `.gitignore`

**Domain model** (`domain/model/`)

- `FafsaApplication` aggregate with `StudentInfo`, `SpouseInfo`, `Household`, `Income`
- Status enums: `OverallStatus`, `DependencyStatus`, `MaritalStatus`
- `ValidationResult` with overall valid/invalid derivation

**Edit framework** (`domain/edit/`)

- `EditRule` interface, `EditEngine` (collect-all-errors), `EditOutcome`, `EditSeverity`
- `UsStateCodes` utility (50 states + DC)

**API layer** (`api/dto/`)

- Request/response DTOs matching [contracts/openapi.yaml](contracts/openapi.yaml)

**Application wiring** (`application/`, `infrastructure/config/`)

- `ApplicationMapper` — DTO ↔ domain mapping
- `ValidationService` — delegates to `EditEngine`
- `EditRuleConfig` — Spring bean wiring for `EditEngine` (no rule beans registered yet)

### Not yet implemented

- Seven FAFSA edit rule classes (`domain/edit/rules/`)
- `POST /api/v1/applications/validate` controller
- Automated tests

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