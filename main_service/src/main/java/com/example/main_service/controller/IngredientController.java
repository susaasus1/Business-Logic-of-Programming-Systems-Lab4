package com.example.main_service.controller;

import javax.validation.Valid;

import com.example.main_service.dto.request.AddIngredientRequest;
import com.example.main_service.dto.request.UpdateIngredientRequest;
import com.example.main_service.model.Ingredients;
import com.example.main_service.service.IngredientsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientsService ingredientsService;

    public IngredientController(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Ingredients addIngredient(@Valid @RequestBody AddIngredientRequest addIngredientRequest) {
        return ingredientsService.saveIngredient(addIngredientRequest);

    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@RequestParam("ingredientId") Long ingredientId) {
        ingredientsService.deleteIngredient(ingredientId);
    }

    @PutMapping()
    public Ingredients updateIngredient(@RequestParam("ingredientId") Long ingredientId,
                                        @Valid @RequestBody UpdateIngredientRequest updateIngredientRequest) {
        return ingredientsService.updateIngredient(ingredientId, updateIngredientRequest);
    }

    @GetMapping()
    public List<Ingredients> getAllIngredients(@RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ingredientsService.getIngredientsPage(page, size).getContent();
    }

    @GetMapping("{ingredientId}")
    public Ingredients getIngredient(@PathVariable Long ingredientId) {
        return ingredientsService.getIngredient(ingredientId);
    }
}
