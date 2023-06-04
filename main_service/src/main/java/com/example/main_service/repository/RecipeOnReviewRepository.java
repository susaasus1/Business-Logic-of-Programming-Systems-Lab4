package com.example.main_service.repository;


import com.example.main_service.model.RecipeOnReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeOnReviewRepository extends JpaRepository<RecipeOnReview, Long> {

}
