package com.example.main_service.controller;

import com.example.main_service.dto.request.AddDishRequest;
import com.example.main_service.dto.request.UpdateDishRequest;
import com.example.main_service.model.Dish;
import com.example.main_service.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Dish addDish(@Valid @RequestBody AddDishRequest addDishRequest) {
        return dishService.saveDish(addDishRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@RequestParam("dishId") Long dishId) {
        dishService.deleteDish(dishId);
    }

    @PutMapping()
    public Dish updateDish(@RequestParam("dishId") Long dishId,
                           @Valid @RequestBody UpdateDishRequest updateDishRequest) {
        return dishService.updateDish(dishId, updateDishRequest);
    }

    @GetMapping()
    public List<Dish> getAllDishes(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        return dishService.getAllDish(page, size).getContent();
    }

    @GetMapping("{dishId}")
    public Dish getDish(@PathVariable Long dishId) {
        return dishService.getDish(dishId);
    }
}
