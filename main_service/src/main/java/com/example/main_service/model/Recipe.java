package com.example.main_service.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@ToString

public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 4096, nullable = false)
    private String description;
    @Column(name = "count_portion")
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

    public Recipe(Long id, String description, Integer countPortion, User user,
                  NationalCuisine nationalCuisine, Dish dish) {
        this.id = id;
        this.description = description;
        this.countPortion = countPortion;
        this.user = user;
        this.nationalCuisine = nationalCuisine;
        this.dish = dish;
    }

}
