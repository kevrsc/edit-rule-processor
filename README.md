# FAFSA Edit Rule Processor

## Solution: Build, Run & Test

**Current status:** All phases complete (Phases 1–6). The service accepts FAFSA
application JSON over HTTP, runs seven edit rules with collect-all-errors semantics,
returns actionable violation messages, handles malformed requests with HTTP 400,
and correctly gates conditional edits by dependency and marital status. All
automated tests pass.

### What's in place

**Infrastructure**

- Java 25, Spring Boot 4.1, Gradle 9 (`./gradlew`)
- Virtual threads enabled (`spring.threads.virtual.enabled=true`)
- Entry point: `com.fafsaeditruleprocessor.FafsaEditProcessorApplication`
- Server port 8080

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

**API**

- `POST /api/v1/applications/validate` — see [contracts/openapi.yaml](contracts/openapi.yaml)
- HTTP 400 for malformed JSON or missing required fields

**Tests** (10 classes)

- 7 rule unit tests, `EditEngineTest`, `ValidationControllerTest`, `ValidationServiceTest`

Design rationale: [DECISIONS.md](DECISIONS.md) 

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

### Manual validation scenarios

#### 1. Valid application

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

**Expected:** `"overallStatus": "VALID"`; all seven edits `"passed": true`.

#### 2. Invalid application (collect all errors)

```bash
curl -s -X POST http://localhost:8080/api/v1/applications/validate \
  -H "Content-Type: application/json" \
  -d '{
    "studentInfo": {
      "firstName": "John",
      "lastName": "Doe",
      "ssn": "invalid",
      "dateOfBirth": "2015-01-01"
    },
    "dependencyStatus": "dependent",
    "maritalStatus": "married",
    "household": { "numberInHousehold": 2, "numberInCollege": 5 },
    "income": { "studentIncome": -1000 },
    "stateOfResidence": "XX"
  }'
```

**Expected:** `"overallStatus": "INVALID"`; seven failed edits with actionable messages.

#### 3. Independent student (conditional skip)

```bash
curl -s -X POST http://localhost:8080/api/v1/applications/validate \
  -H "Content-Type: application/json" \
  -d '{
    "studentInfo": {
      "firstName": "Alex",
      "lastName": "Lee",
      "ssn": "111223333",
      "dateOfBirth": "2000-01-01"
    },
    "dependencyStatus": "independent",
    "maritalStatus": "single",
    "household": { "numberInHousehold": 1, "numberInCollege": 1 },
    "income": { "studentIncome": 12000 },
    "stateOfResidence": "NY"
  }'
```

**Expected:** `DEPENDENT_PARENT_INCOME` and `MARITAL_STATUS` pass without spouse or parent income.

#### 4. Married applicant (spouse required)

```bash
curl -s -X POST http://localhost:8080/api/v1/applications/validate \
  -H "Content-Type: application/json" \
  -d '{
    "studentInfo": {
      "firstName": "Sam",
      "lastName": "Taylor",
      "ssn": "222334444",
      "dateOfBirth": "1999-06-01"
    },
    "dependencyStatus": "independent",
    "maritalStatus": "married",
    "household": { "numberInHousehold": 2, "numberInCollege": 1 },
    "income": { "studentIncome": 30000 },
    "stateOfResidence": "TX"
  }'
```

**Expected:** `MARITAL_STATUS` fails with spouse information required.

### Test

```bash
./gradlew test
```

Pre-push verification:

```bash
./gradlew clean test
```

---
