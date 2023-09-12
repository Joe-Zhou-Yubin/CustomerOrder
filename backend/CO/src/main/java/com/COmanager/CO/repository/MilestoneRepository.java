package com.COmanager.CO.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.COmanager.CO.models.Milestone;

public interface MilestoneRepository extends JpaRepository<Milestone, Long>{
    Optional<Milestone> findByMilestoneId(String milestoneId);
    List<Milestone> findByOrderNumberAndPaid(String orderNumber, boolean paid);
	List<Milestone> findByOrderNumber(String orderNumber);
}
