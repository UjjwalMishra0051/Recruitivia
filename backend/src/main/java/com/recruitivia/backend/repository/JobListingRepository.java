package com.recruitivia.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recruitivia.backend.entity.JobListing;

public interface JobListingRepository extends JpaRepository<JobListing, Long> {

    List<JobListing> findByStatus(String status);

    List<JobListing> findByEmployerId(Long employerId);
}

