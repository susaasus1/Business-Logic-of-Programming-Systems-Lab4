package com.example.main_service.controller;

import com.example.main_service.dto.request.AddCuisineRequest;
import com.example.main_service.dto.request.UpdateCuisineRequest;
import com.example.main_service.model.NationalCuisine;
import com.example.main_service.service.NationalCuisineService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cuisine")
public class CuisineController {
    private final NationalCuisineService cuisineService;

    public CuisineController(NationalCuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public NationalCuisine addCuisine(@Valid @RequestBody AddCuisineRequest addCuisineRequest) {
        return cuisineService.saveCuisine(addCuisineRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCuisine(@RequestParam("cuisineId") Long cuisineId) {
        cuisineService.deleteCuisine(cuisineId);
    }

    @PutMapping()
    public NationalCuisine updateCuisine(@RequestParam("cuisineId") Long cuisineId,
                                         @Valid @RequestBody UpdateCuisineRequest updateCuisineRequest) {
        return cuisineService.updateCuisine(cuisineId, updateCuisineRequest);
    }

    @GetMapping()
    public List<NationalCuisine> getAllCuisines(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return cuisineService.getCuisinesPage(page, size).getContent();
    }

    @GetMapping("{cuisineId}")
    public NationalCuisine getCuisine(@PathVariable Long cuisineId) {
        return cuisineService.getCuisine(cuisineId);
    }
}
