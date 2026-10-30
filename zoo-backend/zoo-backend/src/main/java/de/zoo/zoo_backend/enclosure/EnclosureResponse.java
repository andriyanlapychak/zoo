package de.zoo.zoo_backend.enclosure;

public record EnclosureResponse(
        Long id,
        String name,
        Integer sizeM2,
        String description) {

    public static EnclosureResponse from(Enclosure enclosure) {
        return new EnclosureResponse(
                enclosure.getId(),
                enclosure.getName(),
                enclosure.getSizeM2(),
                enclosure.getDescription());
    }
}