package com.example.main_service.repository;

import com.example.main_service.model.RecipeIngredients;
import com.example.main_service.model.RecipeTastes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Long> {

    List<RecipeIngredients> getAllByRecipeId(Long id);
    void deleteAllByRecipeId(Long id);
}
