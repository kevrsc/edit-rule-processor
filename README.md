# FAFSA Edit Rule Processor

## Solution: Build, Run & Test

**Current status:** Phases 1–3 are complete (User Story 1 / MVP). The service
accepts FAFSA application JSON over HTTP, runs seven edit rules, and returns a
full validation audit trail. Valid sample payloads return `overallStatus: VALID`.
All automated tests pass.

### What's in place

**Infrastructure (Phase 1)**

- Java 25, Spring Boot 4.1, Gradle 9 (`./gradlew`)
- Virtual threads enabled (`spring.threads.virtual.enabled=true`)
- Entry point: `com.fafsaeditruleprocessor.FafsaEditProcessorApplication`
- Server port 8080
- `.gitignore`, `DECISIONS.md`

**Domain model** (`domain/model/`)

- `FafsaApplication` aggregate with `StudentInfo`, `SpouseInfo`, `Household`, `Income`
- Status enums: `OverallStatus`, `DependencyStatus`, `MaritalStatus`
- `ValidationResult` with overall valid/invalid derivation

**Edit framework** (`domain/edit/`)

- `EditRule` interface, `EditEngine` (collect-all-errors), `EditOutcome`, `EditSeverity`
- `UsStateCodes` utility (50 states + DC)

**Edit rules** (`domain/edit/rules/`)

| Class | Code | Summary |
|-------|------|---------|
| `StudentAgeEdit` | `STUDENT_AGE` | Student must be at least 14 years old |
| `SsnFormatEdit` | `SSN_FORMAT` | SSN must be exactly 9 digits |
| `DependentParentIncomeEdit` | `DEPENDENT_PARENT_INCOME` | Parent income required when dependent |
| `IncomeValidationEdit` | `INCOME_VALIDATION` | No negative income values |
| `HouseholdLogicEdit` | `HOUSEHOLD_LOGIC` | College count cannot exceed household size |
| `StateCodeEdit` | `STATE_CODE` | Valid US state or DC |
| `MaritalStatusEdit` | `MARITAL_STATUS` | Spouse name and SSN required when married |

Rules are Spring `@Component` beans auto-wired into `EditEngine` via `EditRuleConfig`.

**API** (`api/`)

- `POST /api/v1/applications/validate` — `ValidationController`
- Request/response DTOs matching [contracts/openapi.yaml](contracts/openapi.yaml)

**Application wiring** (`application/`, `infrastructure/config/`)

- `ApplicationMapper` — DTO ↔ domain mapping
- `ValidationService` — delegates to `EditEngine`
- `EditRuleConfig` — injects all `EditRule` beans into `EditEngine`

**Tests** (`src/test/java/`)

- Unit test per edit rule (7 classes)
- `ValidationControllerTest` — valid-sample HTTP scenario

### Not yet implemented

- **Phase 4 (US2):** Invalid-sample controller tests, HTTP 400 for malformed input,
  `EditEngineTest`, enhanced failure messages
- **Phase 5 (US3):** Conditional-rule branch tests (independent parent income skip,
  married spouse requirements)
- **Phase 6:** Polish — `ValidationServiceTest`, README curl catalog, manual
  quickstart validation


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

The application starts on `http://localhost:8080`.

### Validate an application

```bash
curl -s -X POST http://localhost:8080/api/v1/applications/validate \
  -H "Content-Type: application/json" \
  -d '{
    "studentInfo": {
      "firstName": "Jane",
      "lastName": "Smith",
      "ssn": "123456789",
      "dateOfBirth": "2003-05-15"
    },
    "dependencyStatus": "dependent",
    "maritalStatus": "single",
    "household": { "numberInHousehold": 4, "numberInCollege": 1 },
    "income": { "studentIncome": 5000, "parentIncome": 65000 },
    "stateOfResidence": "CA"
  }'
```

**Expected:** `"overallStatus": "VALID"` with all seven edits `"passed": true`.

### Test

```bash
./gradlew test
```

Windows:

```powershell
.\gradlew.bat test
```

Pre-push verification:

```bash
./gradlew clean test
```

---
