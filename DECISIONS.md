# Design Decisions: FAFSA Edit Rule Processor

This document records design choices, trade-offs, and assumptions.

**Status**: All phases complete (Phases 1–6). Feature is submission-ready:
seven edit rules, validation API, collect-all-errors, actionable messages, HTTP
400 for malformed requests, conditional edit gating, automated tests, and manual
quickstart scenarios verified.

---

## Rule Representation

**Decision**: Code-based strategy pattern. Each edit is a Spring `@Component`
implementing `EditRule` with stable `code()`, `name()`, `severity()`, `appliesTo()`,
and `evaluate()` methods.

**Why**: Discrete, independently testable units (constitution Principle I). Adding
EDIT-008 means a new class + test with no changes to existing rules.

**Implemented**: Seven rule classes in `domain/edit/rules/`; `EditRuleConfig` injects
`List<EditRule>` into `EditEngine`.

**Alternatives rejected**: JSON/YAML config, database-driven rules, monolithic
validator, Drools/rules engine (over-engineered for seven static rules).

---

## Error Handling

**Decision**: Collect-all-errors — evaluate every applicable edit and return all
outcomes in a single response.

**Why**: User Story 2 requires the sample invalid application to
report all seven violations at once.

**Implemented**: `EditEngine.validate()` iterates all registered rules without
short-circuiting. Non-applicable rules emit `EditOutcome.passed()` so the response
always contains seven edit entries (audit trail intact).

**Alternatives rejected**: Stop at first failure; optional query parameter for mode.

---

## Rule Priority and Order

**Decision**: Rules run in Spring bean registration order. Order does not affect
pass/fail outcomes or overall status.

**Why**: No rule depends on another rule's result. Overall status is `INVALID` if
any blocking edit fails.

**Implemented**: Bean list order from component scanning; `EditEngine` preserves
that order in the `edits` array.

**Alternatives rejected**: Priority-sorted execution; short-circuit on first error.

---

## Severity Levels

**Decision**: `ERROR` (blocking; fails overall status) and `WARNING` (reserved for
future non-blocking edits). All seven required edits use `ERROR`.

**Why**: Simple binary scheme that satisfies assignment severity requirement and
supports interview extensions (advisory edits).

**Implemented**: All seven rules return `EditSeverity.ERROR`.

**Alternatives rejected**: Single severity only; four-level INFO/WARNING/ERROR/FATAL.

---

## Age Calculation

**Decision**: `Period.between(dateOfBirth, LocalDate.now()).getYears()`; student
must be ≥ 14 on the validation date.

**Why**: Matches spec assumption (age relative to submission date). Exactly 14 years
today passes; under 14 fails.

**Alternatives rejected**: Age at end of aid year; UTC midnight birthday rules.

---

## Valid US State Codes

**Decision**: 50 states + DC as two-letter postal abbreviations; comparison is
case-insensitive via `trim().toUpperCase()`.

**Why**: Standard FAFSA convention per spec assumptions.

**Alternatives rejected**: US territories; full state name lookup.

---

## SSN Validation

**Decision**: Exactly nine consecutive digits (`\d{9}`); no dashes or spaces.

**Why**: Assignment specifies 9 digits; README sample uses unformatted SSN.

---

## Conditional Field Handling

**Decision**:

- **Parent income**: Required when `dependencyStatus` is `DEPENDENT`; `parentIncome`
  must be non-null (zero is valid).
- **Spouse info**: Required when `maritalStatus` is `MARRIED`; `firstName`, `lastName`,
  and `ssn` must all be non-blank.
- **Independent / single**: Conditional edits pass without evaluating absent fields.

**Why**: Spec User Story 3.

**Implemented**: `appliesTo()` on `DependentParentIncomeEdit` and `MaritalStatusEdit`;
`EditEngine` records pass outcomes for skipped rules.

---

## API Design

**Decision**: `POST /api/v1/applications/validate` with JSON request/response per
`contracts/openapi.yaml`. Malformed bodies return HTTP 400 with `ErrorResponse`.

**Why**: Collect-all validation results in one round trip.

**Implemented**: `ValidationController`, `ApiExceptionHandler`, Jakarta validation on
request DTOs.

**Alternatives rejected**: `PUT /applications/{id}` (no persistence); GraphQL.

---

## Technology Stack

**Decision**: Java 25, Spring Boot 4.1, Gradle 9.x, JUnit 5, virtual threads
enabled (`spring.threads.virtual.enabled=true`).

**Why**: Aligns with employer stack (README), AGENTS.md, and constitution.

**Implemented**: `./gradlew clean test` passes (10 test classes).

---

## Testing Strategy

**Decision**:

- One unit test class per `EditRule` (boundary, failure messages, conditional cases).
- `EditEngineTest` for collect-all and skip behavior.
- `ValidationControllerTest` for valid, invalid, conditional, malformed, and OpenAPI
  contract shape.
- `ValidationServiceTest` for service-layer smoke coverage.

**Why**: TDD, meaningful coverage.

---

## Resolved Implementation Details

| Topic | Decision | Where |
|-------|----------|-------|
| Age calculation | `Period.between(dob, LocalDate.now()).getYears()`; must be ≥ 14 | `StudentAgeEdit` |
| US state codes | 50 states + DC; case-insensitive | `UsStateCodes`, `StateCodeEdit` |
| SSN format | Exactly nine digits (`\d{9}`) | `SsnFormatEdit` |
| Parent income | Required when `DEPENDENT`; `null` fails, `0` passes | `DependentParentIncomeEdit` |
| Negative income | Any present income below zero fails | `IncomeValidationEdit` |
| Household logic | `numberInCollege` ≤ `numberInHousehold` | `HouseholdLogicEdit` |
| Marital status | Spouse name + SSN when `MARRIED` | `MaritalStatusEdit` |
| Conditional gating | `appliesTo()` + engine pass for skipped rules | `EditEngine` |
| API path | `POST /api/v1/applications/validate` | `ValidationController` |
| JSON enums | Lowercase in request; uppercase in response | `ApplicationMapper` |
| Malformed JSON | HTTP 400 `MALFORMED_REQUEST` | `ApiExceptionHandler` |
| Validation errors | HTTP 400 `VALIDATION_ERROR` | `ApiExceptionHandler` |

---

## Edit Rule Catalog

| Code | Class | Applies when |
|------|-------|--------------|
| `STUDENT_AGE` | `StudentAgeEdit` | Always |
| `SSN_FORMAT` | `SsnFormatEdit` | Always |
| `DEPENDENT_PARENT_INCOME` | `DependentParentIncomeEdit` | `DEPENDENT` only |
| `INCOME_VALIDATION` | `IncomeValidationEdit` | Always |
| `HOUSEHOLD_LOGIC` | `HouseholdLogicEdit` | Always |
| `STATE_CODE` | `StateCodeEdit` | Always |
| `MARITAL_STATUS` | `MaritalStatusEdit` | `MARRIED` only |

---

## Time Spent

_To be updated honestly before submission._
