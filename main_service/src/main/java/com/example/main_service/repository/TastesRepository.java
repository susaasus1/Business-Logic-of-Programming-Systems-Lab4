package com.example.main_service.repository;


import com.example.main_service.model.Tastes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TastesRepository extends JpaRepository<Tastes, Long> {

    Optional<Tastes> findTastesByTaste(String taste);

    Optional<Tastes> findTastesById(Long id);

    Page<Tastes> findAll(Pageable pageable);

    boolean existsTastesByTaste(String taste);
}
