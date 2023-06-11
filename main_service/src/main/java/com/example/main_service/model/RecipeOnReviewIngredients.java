package com.example.main_service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "recipe_on_review_ingredients")
@Getter
@Setter
@NoArgsConstructor
public class RecipeOnReviewIngredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "ingredient_id")
    private Long ingredientId;


    public RecipeOnReviewIngredients(Long recipeId, Long ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
    }

}
