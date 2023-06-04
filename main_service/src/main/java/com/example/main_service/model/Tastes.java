package com.example.main_service.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tastes")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Tastes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32, unique = true)
    private String taste;

    public Tastes(String taste) {
        this.taste = taste;
    }
}
