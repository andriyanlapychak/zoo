package de.zoo.zoo_backend.enclosure;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/enclosures")
public class EnclosureController {

    private final EnclosureService enclosureService;

    public EnclosureController(EnclosureService enclosureService) {
        this.enclosureService = enclosureService;
    }

    @GetMapping
    public List<EnclosureResponse> findAll() {
        return enclosureService.findAll();
    }
} 
