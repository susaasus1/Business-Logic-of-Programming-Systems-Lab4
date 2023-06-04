package com.example.main_service.service;

import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.model.*;
import com.example.main_service.repository.RecipeOnReviewRepository;
import com.example.main_service.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.main_service.dto.EmailDetails;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeOnReviewService {
    private final RecipeRepository recipeRepository;
    private final RecipeOnReviewRepository recipeOnReviewRepository;

    private final UserService userService;

    private final DishService dishService;

    private final IngredientsService ingredientsService;

    private final TastesService tastesService;

    private final NationalCuisineService nationalCuisineService;

    private final EmailService emailService;


    public RecipeOnReviewService(RecipeRepository recipeRepository,
                                 RecipeOnReviewRepository recipeOnReviewRepository,
                                 UserService userService, DishService dishService,
                                 IngredientsService ingredientsService, TastesService tastesService,
                                 NationalCuisineService nationalCuisineService, EmailService emailService) {
        this.recipeRepository = recipeRepository;
        this.recipeOnReviewRepository = recipeOnReviewRepository;
        this.userService = userService;
        this.dishService = dishService;
        this.ingredientsService = ingredientsService;
        this.tastesService = tastesService;
        this.nationalCuisineService = nationalCuisineService;
        this.emailService = emailService;
    }

    public void saveRecipe(Long id, String admin) {
        Optional<RecipeOnReview> recipeOnReview = recipeOnReviewRepository.findById(id);
        if (recipeOnReview.isEmpty()) {
            throw new ResourceNotFoundException("Рецепта с id=" + id + " не существует!");
        }

        Recipe recipe = new Recipe();
        Dish dish = dishService.findDishByName(recipeOnReview.get().getDish().getName());
        User user = userService.findUserByLogin(recipeOnReview.get().getUser().getLogin());
        NationalCuisine nationalCuisine = nationalCuisineService.findNationalCuisineByName(recipeOnReview.get().getNationalCuisine().getCuisine());
        List<Tastes> tastesList = tastesService.findAllTastesByTasteNames(recipeOnReview.get().getAllTastesName());
        List<Ingredients> ingredientsList = ingredientsService.findAllIngredientsByNames(recipeOnReview.get().getAllIngredientsName());
        recipe.setDish(dish);
        recipe.setDescription(recipeOnReview.get().getDescription());
        recipe.setId(recipeOnReview.get().getId());
        recipe.setTastes(tastesList);
        recipe.setIngredients(ingredientsList);
        recipe.setCountPortion(recipeOnReview.get().getCountPortion());
        recipe.setNationalCuisine(nationalCuisine);
        recipe.setUser(user);
        if (recipeOnReview.get().getUpdateRecipe() != null) {
            recipeRepository.deleteById(recipeOnReview.get().getUpdateRecipe());
        }
        recipeOnReviewRepository.deleteById(id);
        recipeRepository.save(recipe);
        emailService.sendSimpleMail(
                new EmailDetails(user.getEmail(),
                        "Ваш рецепт №" + recipe.getId() + " был проверен и добавлен администратором " + admin,
                        "Добавление рецепта на сайте povarenok.ru"
                )
        );
    }

    public void deleteRecipe(Long id, String admin, String declineReason) {
        Optional<RecipeOnReview> recipe = recipeOnReviewRepository.findById(id);
        if (recipe.isEmpty()) {
            throw new ResourceNotFoundException("Рецепта с id=" + id + " не существует!");
        }

        String email = recipe.get().getUser().getEmail();

        recipeOnReviewRepository.deleteById(id);
        emailService.sendSimpleMail(
                new EmailDetails(email,
                        "Ваш рецепт №" + recipe.get().getId() + " был отклонен администратором " + admin + " по причине: " + declineReason,
                        "Добавление рецепта на сайте povarenok.ru"
                )
        );
    }

    public Page<RecipeOnReview> getAllRecipesOnReview(int page, int size, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
        return recipeOnReviewRepository.findAll(pageable);

    }
}
