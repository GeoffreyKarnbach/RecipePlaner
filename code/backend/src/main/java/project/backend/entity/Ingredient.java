package project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import project.backend.enums.IngredientUnit;

import java.util.List;

@Entity
@Table(name = "ingredient")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ingredient")
    @SequenceGenerator(name = "seq_ingredient", allocationSize = 5)
    @EqualsAndHashCode.Exclude
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_source", nullable = true)
    private String imageSource;

    @Column(name = "count", nullable = false)
    private float count;

    @Column(name = "unit", nullable = false)
    private IngredientUnit unit;

    @ManyToOne
    @JoinColumn(name = "ingredient_category_id", nullable = false)
    private IngredientCategory ingredientCategory;

    @ManyToMany(mappedBy = "ingredients")
    private List<Recipe> recipes;

}
