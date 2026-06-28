package com.fafsaeditruleprocessor.infrastructure.config;

import com.fafsaeditruleprocessor.domain.edit.EditEngine;
import com.fafsaeditruleprocessor.domain.edit.EditRule;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EditRuleConfig {

    @Bean
    public EditEngine editEngine(List<EditRule> rules) {
        return new EditEngine(rules);
    }
}
