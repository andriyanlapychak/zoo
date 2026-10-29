package de.zoo.zoo_backend.animal;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Transactional(readOnly = true)
    public List<AnimalResponse> findAll() {
        return animalRepository.findAll().stream()
                .map(AnimalResponse::from)
                .toList();
    }
}