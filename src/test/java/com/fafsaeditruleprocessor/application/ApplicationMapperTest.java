package com.fafsaeditruleprocessor.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fafsaeditruleprocessor.api.dto.FafsaApplicationRequest;
import com.fafsaeditruleprocessor.api.dto.HouseholdDto;
import com.fafsaeditruleprocessor.api.dto.IncomeDto;
import com.fafsaeditruleprocessor.api.dto.SpouseInfoDto;
import com.fafsaeditruleprocessor.api.dto.StudentInfoDto;
import com.fafsaeditruleprocessor.domain.model.MaritalStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationMapperTest {

    private ApplicationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ApplicationMapper();
    }

    @Test
    void toDomainMapsSpouseInfoWhenPresent() {
        var request = marriedRequest(new SpouseInfoDto("Pat", "Taylor", "333445555"));

        var application = mapper.toDomain(request);

        assertEquals(MaritalStatus.MARRIED, application.maritalStatus());
        assertEquals("Pat", application.spouseInfo().firstName());
        assertEquals("Taylor", application.spouseInfo().lastName());
        assertEquals("333445555", application.spouseInfo().ssn());
    }

    @Test
    void toDomainLeavesSpouseInfoNullWhenAbsent() {
        var request = marriedRequest(null);

        var application = mapper.toDomain(request);

        assertEquals(MaritalStatus.MARRIED, application.maritalStatus());
        assertNull(application.spouseInfo());
    }

    private static FafsaApplicationRequest marriedRequest(SpouseInfoDto spouseInfo) {
        return new FafsaApplicationRequest(
                new StudentInfoDto("Sam", "Taylor", "222334444", LocalDate.of(1999, 6, 1)),
                "independent",
                "married",
                spouseInfo,
                new HouseholdDto(2, 1),
                new IncomeDto(BigDecimal.valueOf(30000), null),
                "TX");
    }
}
