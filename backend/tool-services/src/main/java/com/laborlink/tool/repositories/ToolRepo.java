package com.laborlink.tool.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.laborlink.tool.entities.Tool;

@Repository
public interface ToolRepo extends CrudRepository<Tool,Long> {
    Optional<Tool> findByName(String name);
}