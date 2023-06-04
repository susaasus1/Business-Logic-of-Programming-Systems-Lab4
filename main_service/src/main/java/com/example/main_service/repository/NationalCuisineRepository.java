package com.example.main_service.repository;


import com.example.main_service.model.NationalCuisine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NationalCuisineRepository extends JpaRepository<NationalCuisine, Long> {

    Optional<NationalCuisine> findNationalCuisineByCuisine(String cuisine);

    Optional<NationalCuisine> findNationalCuisineById(Long id);

    Page<NationalCuisine> findAll(Pageable pageable);

    boolean existsNationalCuisineByCuisine(String cuisine);
}
