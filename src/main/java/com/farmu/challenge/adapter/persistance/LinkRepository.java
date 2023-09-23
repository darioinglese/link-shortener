package com.farmu.challenge.adapter.persistance;

import com.farmu.challenge.domain.Link;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface LinkRepository extends CrudRepository<Link, Long>{
    Optional<Link> findByLongLink(String link);
}
