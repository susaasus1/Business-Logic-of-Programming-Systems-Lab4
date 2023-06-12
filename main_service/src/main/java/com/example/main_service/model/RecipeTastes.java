package com.example.main_service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "recipe_tastes")
@Getter
@Setter
@NoArgsConstructor
public class RecipeTastes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "taste_id")
    private Long tasteId;


    public RecipeTastes(Long recipeId, Long tasteId) {
        this.recipeId = recipeId;
        this.tasteId = tasteId;
    }

}
