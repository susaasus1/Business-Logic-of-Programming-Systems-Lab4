package com.example.main_service.service;

import com.example.main_service.dto.request.AddTasteRequest;
import com.example.main_service.dto.request.UpdateTasteRequest;
import com.example.main_service.model.Tastes;
import com.example.main_service.repository.TastesRepository;
import com.example.main_service.exception.IllegalPageParametersException;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TastesService {
    private final TastesRepository tastesRepository;


    public TastesService(TastesRepository tastesRepository) {
        this.tastesRepository = tastesRepository;
    }

    public Tastes findTasteByTasteName(String taste) {
        return tastesRepository.findTastesByTaste(taste).orElseThrow(() -> new ResourceNotFoundException("Вкус " + taste + " не найден"));
    }

    public List<Tastes> findAllTastesByTasteNames(List<String> tasteNames) {
        List<Tastes> tastesList = new ArrayList<>();
        for (String taste_name : tasteNames) {
            Tastes taste = findTasteByTasteName(taste_name);
            tastesList.add(taste);
        }
        return tastesList;
    }

    public Tastes saveTaste(AddTasteRequest addTasteRequest) {
        Tastes taste = new Tastes(addTasteRequest.getTaste());
        if (tastesRepository.existsTastesByTaste(addTasteRequest.getTaste()))
            throw new ResourceAlreadyExistException("Вкус " + addTasteRequest.getTaste() + " уже есть в базе данных!");
        return tastesRepository.save(taste);
    }

    public void deleteTaste(Long tasteId) {
        if (!tastesRepository.existsById(tasteId))
            throw new ResourceNotFoundException("Вкус с id=" + tasteId + " не существует!");
        tastesRepository.deleteById(tasteId);
    }

    public Tastes updateTaste(Long tasteId, UpdateTasteRequest updateTasteRequest) {
        Tastes taste = tastesRepository.findTastesById(tasteId).orElseThrow(() -> new ResourceNotFoundException("Вкус с id=" + tasteId + " не существует!"));
        taste.setTaste(updateTasteRequest.getTaste());
        return tastesRepository.save(taste);
    }

    public Tastes getTaste(Long tasteId) {
        Tastes taste = tastesRepository.findTastesById(tasteId).orElseThrow(() -> new ResourceNotFoundException("Вкус с id=" + tasteId + " не существует!"));
        return taste;
    }

    public Page<Tastes> getAllTaste(int pageNum, int pageSize) {
        if (pageNum < 1 || pageSize < 1)
            throw new IllegalPageParametersException("Номер страницы и размер страницы должны быть больше 1!");
        Pageable pageRequest = createPageRequest(pageNum - 1, pageSize);
        Page<Tastes> tastes = tastesRepository.findAll(pageRequest);
        if (tastes.getTotalPages() < pageNum)
            throw new ResourceNotFoundException("На указанной странице не найдено записей!");
        return tastes;
    }

    private Pageable createPageRequest(int pageNum, int pageSize) {
        return PageRequest.of(pageNum, pageSize);
    }
}
