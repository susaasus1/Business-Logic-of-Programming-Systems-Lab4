package com.example.main_service.repository;

import com.example.main_service.model.RecipeOnReviewIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecipeOnReviewIngredientsRepository extends JpaRepository<RecipeOnReviewIngredients, Long> {
    List<RecipeOnReviewIngredients> getAllByRecipeId(Long id);

    void deleteAllByRecipeId(Long id);
}
