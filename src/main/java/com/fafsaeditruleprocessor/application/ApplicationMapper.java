package com.fafsaeditruleprocessor.application;

import com.fafsaeditruleprocessor.api.dto.EditOutcomeDto;
import com.fafsaeditruleprocessor.api.dto.FafsaApplicationRequest;
import com.fafsaeditruleprocessor.api.dto.HouseholdDto;
import com.fafsaeditruleprocessor.api.dto.IncomeDto;
import com.fafsaeditruleprocessor.api.dto.SpouseInfoDto;
import com.fafsaeditruleprocessor.api.dto.StudentInfoDto;
import com.fafsaeditruleprocessor.api.dto.ValidationResponse;
import com.fafsaeditruleprocessor.domain.edit.EditOutcome;
import com.fafsaeditruleprocessor.domain.model.DependencyStatus;
import com.fafsaeditruleprocessor.domain.model.FafsaApplication;
import com.fafsaeditruleprocessor.domain.model.Household;
import com.fafsaeditruleprocessor.domain.model.Income;
import com.fafsaeditruleprocessor.domain.model.MaritalStatus;
import com.fafsaeditruleprocessor.domain.model.SpouseInfo;
import com.fafsaeditruleprocessor.domain.model.StudentInfo;
import com.fafsaeditruleprocessor.domain.model.ValidationResult;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public FafsaApplication toDomain(FafsaApplicationRequest request) {
        return new FafsaApplication(
                toDomainStudent(request.studentInfo()),
                DependencyStatus.valueOf(request.dependencyStatus().toUpperCase(Locale.US)),
                MaritalStatus.valueOf(request.maritalStatus().toUpperCase(Locale.US)),
                toDomainSpouse(request.spouseInfo()),
                toDomainHousehold(request.household()),
                toDomainIncome(request.income()),
                request.stateOfResidence());
    }

    public ValidationResponse toResponse(ValidationResult result) {
        List<EditOutcomeDto> edits =
                result.edits().stream().map(this::toEditOutcomeDto).toList();
        return new ValidationResponse(result.overallStatus().name(), edits);
    }

    private StudentInfo toDomainStudent(StudentInfoDto dto) {
        return new StudentInfo(dto.firstName(), dto.lastName(), dto.ssn(), dto.dateOfBirth());
    }

    private SpouseInfo toDomainSpouse(SpouseInfoDto dto) {
        if (dto == null) {
            return null;
        }
        return new SpouseInfo(dto.firstName(), dto.lastName(), dto.ssn());
    }

    private Household toDomainHousehold(HouseholdDto dto) {
        return new Household(dto.numberInHousehold(), dto.numberInCollege());
    }

    private Income toDomainIncome(IncomeDto dto) {
        return new Income(dto.studentIncome(), dto.parentIncome());
    }

    private EditOutcomeDto toEditOutcomeDto(EditOutcome outcome) {
        return new EditOutcomeDto(
                outcome.id(),
                outcome.name(),
                outcome.passed(),
                outcome.severity().name(),
                outcome.message());
    }
}
