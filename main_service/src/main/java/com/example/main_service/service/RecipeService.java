package com.example.main_service.service;

import com.example.main_service.dto.request.AddRecipeRequest;
import com.example.main_service.dto.request.UpdateRecipeRequest;
import com.example.main_service.model.*;
import com.example.main_service.repository.RecipeOnReviewIngredientsRepository;
import com.example.main_service.repository.RecipeOnReviewRepository;
import com.example.main_service.repository.RecipeOnReviewTastesRepository;
import com.example.main_service.repository.RecipeRepository;
import com.example.main_service.exception.PermissionDeniedException;
import com.example.main_service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RecipeService {
    private Recipe recipeOfTheDay;
    private final RecipeRepository recipeRepository;
    private final RecipeOnReviewRepository recipeOnReviewRepository;

    private final UserService userService;

    private final DishService dishService;

    private final IngredientsService ingredientsService;

    private final TastesService tastesService;

    private final NationalCuisineService nationalCuisineService;

    private final RecipeOnReviewIngredientsRepository recipeOnReviewIngredientsRepository;

    private final RecipeOnReviewTastesRepository recipeOnReviewTastesRepository;


    public RecipeService(RecipeRepository recipeRepository, RecipeOnReviewRepository recipeOnReviewRepository, UserService userService, DishService dishService,
                         IngredientsService ingredientsService, TastesService tastesService,
                         NationalCuisineService nationalCuisineService,
                         @Qualifier("singletonRecipe") Recipe recipeOfTheDay,
                         RecipeOnReviewIngredientsRepository recipeOnReviewIngredientsRepository, RecipeOnReviewTastesRepository recipeOnReviewTastesRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeOnReviewRepository = recipeOnReviewRepository;
        this.userService = userService;
        this.dishService = dishService;
        this.ingredientsService = ingredientsService;
        this.tastesService = tastesService;
        this.nationalCuisineService = nationalCuisineService;
        this.recipeOfTheDay = recipeOfTheDay;
        this.recipeOnReviewIngredientsRepository = recipeOnReviewIngredientsRepository;
        this.recipeOnReviewTastesRepository = recipeOnReviewTastesRepository;
    }


    @Transactional(transactionManager = "transactionManager")
    public RecipeOnReview saveRecipe(String login, AddRecipeRequest addRecipeRequest) {
        Dish dish = dishService.findDishByName(addRecipeRequest.getDishName());
        User user = userService.findUserByLogin(login);
        NationalCuisine nationalCuisine = nationalCuisineService.findNationalCuisineByName(addRecipeRequest.getNationalCuisineName());
        List<Tastes> tastesList = tastesService.findAllTastesByTasteNames(addRecipeRequest.getTastesNames());
        List<Ingredients> ingredientsList = ingredientsService.findAllIngredientsByNames(addRecipeRequest.getIngredientsNames());
        RecipeOnReview recipe = new RecipeOnReview(addRecipeRequest.getDescription(),
                addRecipeRequest.getCountPortion(), user, nationalCuisine, dish);
        recipeOnReviewRepository.save(recipe);

        for (Ingredients ingredient : ingredientsList) {
            recipeOnReviewIngredientsRepository.save(new RecipeOnReviewIngredients(recipe.getId(), ingredient.getId()));
        }

        for (Tastes taste : tastesList) {
            recipeOnReviewTastesRepository.save(new RecipeOnReviewTastes(recipe.getId(), taste.getId()));
        }

        return recipe;
    }

    public Recipe findRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Рецепт с номером " + id + " не найден в базе!"));
    }

    public void checkUserOnRecipeOwner(User user, Recipe recipe) {
        if (!user.getLogin().equals(recipe.getUser().getLogin()))
            throw new PermissionDeniedException("Пользователь " + user.getLogin() +
                    " не является владельцем рецепта по номеру " + recipe.getId());
    }

    public void deleteRecipe(String login, Long id) {
        Recipe recipe = findRecipeById(id);
        User user = userService.findUserByLogin(login);
        checkUserOnRecipeOwner(user, recipe);

        recipeRepository.delete(recipe);
    }

    @Transactional(transactionManager = "transactionManager")
    public RecipeOnReview updateRecipe(String login, Long id, UpdateRecipeRequest updateRecipeRequest) {
        Recipe recipe = findRecipeById(id);
        User user = userService.findUserByLogin(login);
        checkUserOnRecipeOwner(user, recipe);
        RecipeOnReview recipeOnReview = new RecipeOnReview();
        recipeOnReview.setId(id);
        Dish dish = dishService.findDishByName(updateRecipeRequest.getDishName());
        NationalCuisine nationalCuisine = nationalCuisineService.
                findNationalCuisineByName(updateRecipeRequest.getNationalCuisineName());
        List<Tastes> tastesList = tastesService.
                findAllTastesByTasteNames(updateRecipeRequest.getTastesNames());
        List<Ingredients> ingredientsList = ingredientsService.
                findAllIngredientsByNames(updateRecipeRequest.getIngredientsNames());
        recipeOnReview.setUser(user);
        recipeOnReview.setDish(dish);
        recipeOnReview.setNationalCuisine(nationalCuisine);
//        recipeOnReview.setIngredients(ingredientsList);
//        recipeOnReview.setTastes(tastesList);
        for (Ingredients ingredient : ingredientsList) {
            recipeOnReviewIngredientsRepository.save(new RecipeOnReviewIngredients(recipeOnReview.getId(), ingredient.getId()));
        }

        for (Tastes taste : tastesList) {
            recipeOnReviewTastesRepository.save(new RecipeOnReviewTastes(recipeOnReview.getId(), taste.getId()));
        }
        recipeOnReview.setDescription(updateRecipeRequest.getDescription());
        recipeOnReview.setCountPortion(updateRecipeRequest.getCountPortion());
        recipeOnReview.setUpdateRecipe(id);
        return recipeOnReviewRepository.save(recipeOnReview);
    }

    public Page<Recipe> getAllRecipes(int page, int size, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
        return recipeRepository.findAll(pageable);
    }

    @Scheduled(fixedRate = 3600000L)
    public void updateRecipeOfTheDay() {
        List<Long> ids = recipeRepository.findIds();
        if (!ids.isEmpty()) {
            Random random = new Random();
            int id = random.nextInt(ids.size());
            recipeOfTheDay = recipeRepository.findById(ids.get(id)).get();
        }
    }

    public Recipe getRecipeOfTheDay() {
        return recipeOfTheDay;
    }
}
