package com.farmu.challenge.adapter.persistance;

import com.farmu.challenge.domain.Img;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Img, Long> {
}
