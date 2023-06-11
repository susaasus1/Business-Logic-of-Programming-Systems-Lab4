package com.example.main_service.model;


import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe_on_reviews")
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class RecipeOnReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 4096, nullable = false)
    private String description;
    private Integer countPortion;

    @JoinColumn(nullable = false, name = "user_login")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(nullable = false, name = "cuisine_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private NationalCuisine nationalCuisine;

    @JoinColumn(nullable = false, name = "dish_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Dish dish;
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "recipe_on_review_tastes",
//            joinColumns = {@JoinColumn(name = "recipe_id")},
//            inverseJoinColumns = {@JoinColumn(name = "taste_id")}
//    )
//    private List<Tastes> tastes;
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @ManyToMany(cascade = {CascadeType.REFRESH})
//    @JoinTable(
//            name = "recipe_on_review_ingredients",
//            joinColumns = {@JoinColumn(name = "recipe_id")},
//            inverseJoinColumns = {@JoinColumn(name = "ingredient_id")}
//    )
//    private List<Ingredients> ingredients;
    private Long updateRecipe;

    public RecipeOnReview(String description, Integer countPortion, User user,
                          NationalCuisine nationalCuisine, Dish dish) {
        this.description = description;
        this.countPortion = countPortion;
        this.user = user;
        this.nationalCuisine = nationalCuisine;
        this.dish = dish;
    }

//    public List<String> getAllTastesName() {
//        List<String> list = new ArrayList<>();
//        for (Tastes tastes1 : tastes) {
//            list.add(tastes1.getTaste());
//        }
//        return list;
//    }
//
//    public List<String> getAllIngredientsName() {
//        List<String> list = new ArrayList<>();
//        for (Ingredients ingredients1 : ingredients) {
//            list.add(ingredients1.getName());
//        }
//        return list;
//    }


}
