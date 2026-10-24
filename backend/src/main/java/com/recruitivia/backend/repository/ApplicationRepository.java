package com.recruitivia.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recruitivia.backend.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByApplicantId(Long applicantId);

    List<Application> findByJobId(Long jobId);

    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
}
