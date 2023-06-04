package com.example.main_service.controller;
import javax.validation.Valid;

import com.example.main_service.dto.request.AddTasteRequest;
import com.example.main_service.dto.request.UpdateTasteRequest;
import com.example.main_service.model.Tastes;
import com.example.main_service.service.TastesService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taste")
public class TasteController {
    private final TastesService tastesService;

    public TasteController(TastesService tastesService) {
        this.tastesService = tastesService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Tastes addTaste(@Valid @RequestBody AddTasteRequest addTasteRequest) {
        return tastesService.saveTaste(addTasteRequest);

    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaste(@RequestParam("tasteId") Long tasteId) {
        tastesService.deleteTaste(tasteId);
    }

    @PutMapping()
    public Tastes updateTaste(@RequestParam("tasteId") Long tasteId,
                              @Valid @RequestBody UpdateTasteRequest updateTasteRequest) {

        return tastesService.updateTaste(tasteId, updateTasteRequest);
    }

    @GetMapping()
    public List<Tastes> getAllTastes(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return tastesService.getAllTaste(page, size).getContent();
    }

    @GetMapping("{tasteId}")
    public Tastes getTaste(@PathVariable Long tasteId) {
        return tastesService.getTaste(tasteId);
    }
}
