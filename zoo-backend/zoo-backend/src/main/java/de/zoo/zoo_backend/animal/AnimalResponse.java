package de.zoo.zoo_backend.animal;

import java.time.LocalDate;

public record AnimalResponse(
        Long id,
        String name,
        LocalDate dateOfBirth,
        String speciesName,
        String enclosureName) {

    public static AnimalResponse from(Animal animal) {
        return new AnimalResponse(
                animal.getId(),
                animal.getName(),
                animal.getDateOfBirth(),
                animal.getSpecies().getName(),
                animal.getEnclosure() == null ? null : animal.getEnclosure().getName());
    }
}