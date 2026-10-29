package de.zoo.zoo_backend.species;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "species")
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "species_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "food", length = 100)
    private String food;

    @Column(name = "daily_food_kg", nullable = false, precision = 6, scale = 2)
    private BigDecimal dailyFoodKg;

    protected Species() {
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getFood() { return food; }
    public void setFood(String food) { this.food = food; }

    public BigDecimal getDailyFoodKg() { return dailyFoodKg; }
    public void setDailyFoodKg(BigDecimal dailyFoodKg) { this.dailyFoodKg = dailyFoodKg; }
}