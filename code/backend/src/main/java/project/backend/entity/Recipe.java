package project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import project.backend.enums.MealType;
import jakarta.persistence.JoinColumn;

import java.util.List;

@Entity
@Table(name = "recipe")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recipe")
    @SequenceGenerator(name = "seq_recipe", allocationSize = 5)
    @EqualsAndHashCode.Exclude
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(name = "preparation_time", nullable = false)
    private int preparationTime;

    @Column(name = "difficulty", nullable = false)
    private int difficulty;

    @ManyToMany
    @JoinTable(
        name = "recipe_recipe_tag",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_tag_id")
    )
    private List<RecipeTag> recipeTags;

    @OneToMany
    private List<RecipeImage> recipeImages;

    @OneToMany
    private List<RecipeRating> recipeRatings;

    @OneToMany
    private List<RecipeStep> recipeSteps;

    @ManyToOne
    @JoinColumn(name = "recipe_category_id", nullable = false)
    private RecipeCategory recipeCategory;

    @ManyToMany
    @JoinTable(
        name = "recipe_ingredient",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @OneToMany
    private List<PlannedRecipe> plannedRecipes;


}
