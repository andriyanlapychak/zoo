package de.zoo.zoo_backend.species;

import java.math.BigDecimal;

public record SpeciesResponse(
        Long id,
        String name,
        String category,
        String food,
        BigDecimal dailyFoodKg) {

    public static SpeciesResponse from(Species species) {
        return new SpeciesResponse(
                species.getId(),
                species.getName(),
                species.getCategory(),
                species.getFood(),
                species.getDailyFoodKg());
    }
}