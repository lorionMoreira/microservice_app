package com.example.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Query;

@Repository
public interface QueryRepository extends MongoRepository<Query, String> {
}
