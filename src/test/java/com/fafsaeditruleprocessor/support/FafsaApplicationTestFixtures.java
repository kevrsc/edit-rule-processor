package com.fafsaeditruleprocessor.support;

import com.fafsaeditruleprocessor.domain.model.DependencyStatus;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import com.fafsaeditruleprocessor.domain.model.Household;
import com.fafsaeditruleprocessor.domain.model.Income;
import com.fafsaeditruleprocessor.domain.model.MaritalStatus;
import com.fafsaeditruleprocessor.domain.model.SpouseInfo;
import com.fafsaeditruleprocessor.domain.model.StudentInfo;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class FafsaApplicationTestFixtures {

    public static final String VALID_SAMPLE_JSON =
            """
            {
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
            }
            """;

    public static final String INVALID_SAMPLE_JSON =
            """
            {
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
            }
            """;

    public static final String INDEPENDENT_SINGLE_SAMPLE_JSON =
            """
            {
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
            }
            """;

    public static final String MARRIED_WITHOUT_SPOUSE_SAMPLE_JSON =
            """
            {
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
            }
            """;

    private FafsaApplicationTestFixtures() {
    }

    public static FafsaApplication validDependentApplication() {
        return new FafsaApplication(
                new StudentInfo("Jane", "Smith", "123456789", LocalDate.of(2003, 5, 15)),
                DependencyStatus.DEPENDENT,
                MaritalStatus.SINGLE,
                null,
                new Household(4, 1),
                new Income(BigDecimal.valueOf(5000), BigDecimal.valueOf(65000)),
                "CA");
    }

    public static FafsaApplication withDateOfBirth(LocalDate dateOfBirth) {
        FafsaApplication base = validDependentApplication();
        return new FafsaApplication(
                new StudentInfo(
                        base.studentInfo().firstName(),
                        base.studentInfo().lastName(),
                        base.studentInfo().ssn(),
                        dateOfBirth),
                base.dependencyStatus(),
                base.maritalStatus(),
                base.spouseInfo(),
                base.household(),
                base.income(),
                base.stateOfResidence());
    }

    public static FafsaApplication withSsn(String ssn) {
        FafsaApplication base = validDependentApplication();
        return new FafsaApplication(
                new StudentInfo(
                        base.studentInfo().firstName(),
                        base.studentInfo().lastName(),
                        ssn,
                        base.studentInfo().dateOfBirth()),
                base.dependencyStatus(),
                base.maritalStatus(),
                base.spouseInfo(),
                base.household(),
                base.income(),
                base.stateOfResidence());
    }

    public static FafsaApplication withIncome(BigDecimal studentIncome, BigDecimal parentIncome) {
        FafsaApplication base = validDependentApplication();
        return new FafsaApplication(
                base.studentInfo(),
                base.dependencyStatus(),
                base.maritalStatus(),
                base.spouseInfo(),
                base.household(),
                new Income(studentIncome, parentIncome),
                base.stateOfResidence());
    }

    public static FafsaApplication withHousehold(int numberInHousehold, int numberInCollege) {
        FafsaApplication base = validDependentApplication();
        return new FafsaApplication(
                base.studentInfo(),
                base.dependencyStatus(),
                base.maritalStatus(),
                base.spouseInfo(),
                new Household(numberInHousehold, numberInCollege),
                base.income(),
                base.stateOfResidence());
    }

    public static FafsaApplication withStateOfResidence(String state) {
        FafsaApplication base = validDependentApplication();
        return new FafsaApplication(
                base.studentInfo(),
                base.dependencyStatus(),
                base.maritalStatus(),
                base.spouseInfo(),
                base.household(),
                base.income(),
                state);
    }

    public static FafsaApplication independentSingleApplication() {
        return new FafsaApplication(
                new StudentInfo("Alex", "Lee", "111223333", LocalDate.of(2000, 1, 1)),
                DependencyStatus.INDEPENDENT,
                MaritalStatus.SINGLE,
                null,
                new Household(1, 1),
                new Income(BigDecimal.valueOf(12000), null),
                "NY");
    }

    public static FafsaApplication marriedWithoutSpouseInfo() {
        return new FafsaApplication(
                new StudentInfo("Sam", "Taylor", "222334444", LocalDate.of(1999, 6, 1)),
                DependencyStatus.INDEPENDENT,
                MaritalStatus.MARRIED,
                null,
                new Household(2, 1),
                new Income(BigDecimal.valueOf(30000), null),
                "TX");
    }

    public static FafsaApplication marriedWithSpouseInfo() {
        return new FafsaApplication(
                new StudentInfo("Sam", "Taylor", "222334444", LocalDate.of(1999, 6, 1)),
                DependencyStatus.INDEPENDENT,
                MaritalStatus.MARRIED,
                new SpouseInfo("Pat", "Taylor", "333445555"),
                new Household(2, 1),
                new Income(BigDecimal.valueOf(30000), null),
                "TX");
    }

    public static FafsaApplication invalidMultiViolationApplication() {
        return new FafsaApplication(
                new StudentInfo("John", "Doe", "invalid", LocalDate.of(2015, 1, 1)),
                DependencyStatus.DEPENDENT,
                MaritalStatus.MARRIED,
                null,
                new Household(2, 5),
                new Income(BigDecimal.valueOf(-1000), null),
                "XX");
    }
}
