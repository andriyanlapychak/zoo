package de.zoo.zoo_backend.enclosure;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
public class EnclosureService {
    private final EnclosureRepository enclosureRepository;

    public EnclosureService(EnclosureRepository enclosureRepository) {
        this.enclosureRepository = enclosureRepository;
    }
    @Transactional(readOnly = true)
    public List<EnclosureResponse> findAll() {
        return enclosureRepository.findAll(Sort.by("name")).stream()
                .map(EnclosureResponse::from)
                .toList();
    }
}
