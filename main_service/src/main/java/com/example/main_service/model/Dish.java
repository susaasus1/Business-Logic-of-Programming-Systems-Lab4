package com.example.main_service.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, length = 512, unique = true)
    private String description;

    public Dish(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
