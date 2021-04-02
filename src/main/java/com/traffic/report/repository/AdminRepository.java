package com.traffic.report.repository;

import com.traffic.report.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUserName(String userName);
}
