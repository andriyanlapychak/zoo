package de.zoo.zoo_backend.species;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpeciesService {

    private final SpeciesRepository speciesRepository;

    public SpeciesService(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    @Transactional(readOnly = true)
    public List<SpeciesResponse> findAll() {
        return speciesRepository.findAll(Sort.by("name")).stream()
                .map(SpeciesResponse::from)
                .toList();
    }
}