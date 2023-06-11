package com.example.main_service.repository;

import com.example.main_service.model.RecipeOnReviewTastes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeOnReviewTastesRepository extends JpaRepository<RecipeOnReviewTastes, Long> {
    List<RecipeOnReviewTastes> getAllByRecipeId(Long id);


    void deleteAllByRecipeId(Long id);

}
