package com.otaviojava.entities;

import jakarta.data.repository.PageableRepository;
import jakarta.data.repository.Repository;

@Repository
public interface Zoo extends PageableRepository<Animal, String>{
}
