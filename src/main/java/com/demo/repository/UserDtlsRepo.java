package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.UserDtls;

public interface UserDtlsRepo extends JpaRepository<UserDtls, Integer>{

}
