package de.zoo.zoo_backend;

import de.zoo.zoo_backend.animal.AnimalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DebugRunner implements CommandLineRunner {

    private final AnimalRepository animalRepository;

    public DebugRunner(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public void run(String... args) {
        animalRepository.findAll().forEach(animal ->
            System.out.println(">>> " + animal.getName() + " ist ein " + animal.getSpecies().getName()
                + ", Gehege: " + (animal.getEnclosure() == null ? "keins" : animal.getEnclosure().getName())));
    }
}