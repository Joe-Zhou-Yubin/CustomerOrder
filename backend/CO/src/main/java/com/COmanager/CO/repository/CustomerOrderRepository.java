package com.COmanager.CO.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.COmanager.CO.models.CustomerOrder;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>{
    Optional<CustomerOrder> findByOrderNumber(String orderNumber);

    Page<CustomerOrder> findByType(String type, Pageable pageable);

}
