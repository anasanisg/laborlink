package com.laborlink.activity.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.laborlink.activity.entities.Activity;

@Repository
public interface ActivityRepo extends CrudRepository<Activity,String> {}
