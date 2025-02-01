package com.laborlink.renting.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.laborlink.renting.entities.Contract;

@Repository
public interface ContractRepo extends CrudRepository<Contract,Long> {

    Optional<Contract> findByUserId(String userId);
  
} 
