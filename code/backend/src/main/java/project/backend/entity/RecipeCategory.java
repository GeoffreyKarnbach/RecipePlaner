package project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "recipe_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recipe_category")
    @SequenceGenerator(name = "seq_recipe_category", allocationSize = 5)
    @EqualsAndHashCode.Exclude
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon_source", nullable = true)
    private String iconSource;

    @OneToMany(mappedBy = "recipeCategory", fetch = FetchType.EAGER)
    private List<Recipe> recipes;
}
