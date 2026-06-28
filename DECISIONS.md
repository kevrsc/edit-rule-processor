# Design Decisions: FAFSA Edit Rule Processor

This document records design choices, trade-offs, and assumptions. 

**Status**: Phases 1–4 complete (User Stories 1–2). Core architecture, all seven
edit rules, validation endpoint, collect-all-errors behavior, actionable failure
messages, and HTTP 400 for malformed requests are implemented. Phases 5–6
(conditional branch tests, polish) remain open.

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

**Why**: Requiring the sample invalid application to
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

## Technology Stack

**Decision**: Java 25, Spring Boot 4.1, Gradle 9.x, JUnit 5, virtual threads
enabled (`spring.threads.virtual.enabled=true`).

**Why**: Aligns with employer stack (README).

**Implemented**: `./gradlew test` passes with 8 test classes (7 rule tests +
`ValidationControllerTest`).

---

## Resolved Implementation Details

| Topic | Decision | Where |
|-------|----------|-------|
| Age calculation | `Period.between(dob, LocalDate.now()).getYears()`; must be ≥ 14 | `StudentAgeEdit` |
| US state codes | 50 states + DC; case-insensitive via `trim().toUpperCase()` | `UsStateCodes`, `StateCodeEdit` |
| SSN format | Exactly nine digits (`\d{9}`); null or non-matching fails | `SsnFormatEdit` |
| Parent income | Required when `dependencyStatus` is `DEPENDENT`; `null` fails, `0` passes | `DependentParentIncomeEdit` |
| Negative income | Any present `studentIncome` or `parentIncome` below zero fails | `IncomeValidationEdit` |
| Household logic | `numberInCollege` must be ≤ `numberInHousehold` (equality allowed) | `HouseholdLogicEdit` |
| Marital status | When `MARRIED`, spouse `firstName`, `lastName`, and `ssn` must all be non-blank | `MaritalStatusEdit` |
| Conditional gating | `appliesTo()` limits evaluation; engine records pass for skipped rules | `EditEngine`, conditional rules |
| API path | `POST /api/v1/applications/validate` | `ValidationController` |
| JSON enums | Request uses lowercase (`dependent`, `single`); mapped to domain enums via `toUpperCase()` | `ApplicationMapper` |
| Response enums | `overallStatus` and `severity` serialized as uppercase strings | `ApplicationMapper` |

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
