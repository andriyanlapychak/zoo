package de.zoo.zoo_backend.enclosure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "enclosure")
public class Enclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enclosure_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "size_m2", nullable = false)
    private Integer sizeM2;

    @Column(name = "description", length = 500)
    private String description;

    protected Enclosure() {
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getSizeM2() { return sizeM2; }
    public void setSizeM2(Integer sizeM2) { this.sizeM2 = sizeM2; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}