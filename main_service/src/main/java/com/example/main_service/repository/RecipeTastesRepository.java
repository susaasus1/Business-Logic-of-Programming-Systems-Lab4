package com.example.main_service.repository;


import com.example.main_service.model.RecipeOnReviewIngredients;
import com.example.main_service.model.RecipeTastes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeTastesRepository extends JpaRepository<RecipeTastes, Long> {
    List<RecipeTastes> getAllByRecipeId(Long id);

    void deleteAllByRecipeId(Long id);
}
