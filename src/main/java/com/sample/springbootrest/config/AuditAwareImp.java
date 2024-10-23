package com.sample.springbootrest.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

class AuditAwareImp implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("system");
  }
}
