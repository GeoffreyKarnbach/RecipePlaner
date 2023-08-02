package project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipe_image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recipe_image")
    @SequenceGenerator(name = "seq_recipe_image", allocationSize = 5)
    @EqualsAndHashCode.Exclude
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "image_source", nullable = false)
    private String imageSource;

    @Column(name = "image_position", nullable = false)
    private int imagePosition;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
