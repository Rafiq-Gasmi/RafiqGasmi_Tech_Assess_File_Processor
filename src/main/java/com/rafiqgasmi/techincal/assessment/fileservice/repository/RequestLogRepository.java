package com.rafiqgasmi.techincal.assessment.fileservice.repository;

import com.rafiqgasmi.techincal.assessment.fileservice.domain.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}