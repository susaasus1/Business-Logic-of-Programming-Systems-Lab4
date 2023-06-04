package com.example.main_service.service;

import com.example.main_service.dto.request.AddDishRequest;
import com.example.main_service.dto.request.UpdateDishRequest;
import com.example.main_service.model.Dish;
import com.example.main_service.repository.DishRepository;
import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }


    public Dish findDishByName(String name) {
        return dishRepository.findDishByName(name).orElseThrow(() -> new ResourceNotFoundException("Блюдо " + name + " не найдено"));
    }

    public Dish saveDish(AddDishRequest addDishRequest) {
        Dish dish = new Dish(addDishRequest.getDishName(), addDishRequest.getDescription());
        if (dishRepository.existsDishByName(addDishRequest.getDishName()))
            throw new ResourceAlreadyExistException("Блюдо " + addDishRequest.getDishName() + " уже есть в базе данных!");
        return dishRepository.save(dish);
    }

    public void deleteDish(Long dishId) {
        if (!dishRepository.existsById(dishId))
            throw new ResourceNotFoundException("Блюдо с id=" + dishId + " не найдено!");
        dishRepository.deleteById(dishId);
    }

    public Dish updateDish(Long dishId, UpdateDishRequest updateDishRequest) {
        Dish dish = dishRepository.findDishById(dishId).orElseThrow(() -> new ResourceNotFoundException("Блюдо с id=" + dishId + " не существует!"));
        if (updateDishRequest.getName() != null) {
            dish.setName(updateDishRequest.getName());
        }
        if (updateDishRequest.getDescription() != null) {
            dish.setDescription(updateDishRequest.getDescription());
        }
        return dishRepository.save(dish);
    }

    public Dish getDish(Long dishId) {
        Dish dish = dishRepository.findDishById(dishId).orElseThrow(() -> new ResourceNotFoundException("Блюдо с id=" + dishId + " не существует!"));
        return dish;
    }

    public Page<Dish> getAllDish(int pageNum, int pageSize) {
        if (pageNum < 1 || pageSize < 1)
            throw new IllegalPageParametersException("Номер страницы и размер страницы должны быть больше 1!");
        Pageable pageRequest = createPageRequest(pageNum - 1, pageSize);
        Page<Dish> dishes = dishRepository.findAll(pageRequest);
        if (dishes.getTotalPages() < pageNum)
            throw new ResourceNotFoundException("На указанной странице не найдено записей!");
        return dishes;
    }

    private Pageable createPageRequest(int pageNum, int pageSize) {
        return PageRequest.of(pageNum, pageSize);
    }
}
