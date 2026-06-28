package com.fafsaeditruleprocessor.api;

import com.fafsaeditruleprocessor.api.dto.FafsaApplicationRequest;
import com.fafsaeditruleprocessor.api.dto.ValidationResponse;
import com.fafsaeditruleprocessor.application.ApplicationMapper;
import com.fafsaeditruleprocessor.application.ValidationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applications")
public class ValidationController {

    private final ValidationService validationService;
    private final ApplicationMapper applicationMapper;

    public ValidationController(ValidationService validationService, ApplicationMapper applicationMapper) {
        this.validationService = validationService;
        this.applicationMapper = applicationMapper;
    }

    @PostMapping("/validate")
    public ValidationResponse validate(@RequestBody FafsaApplicationRequest request) {
        var result = validationService.validate(applicationMapper.toDomain(request));
        return applicationMapper.toResponse(result);
    }
}
