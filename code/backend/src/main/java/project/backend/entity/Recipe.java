package project.backend.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import lombok.ToString;
import project.backend.enums.MealType;
import jakarta.persistence.JoinColumn;

import java.util.List;
import java.util.Map;

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

    @Column(name = "description", nullable = false, length = 10000)
    private String description;

    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(name = "preparation_time", nullable = false)
    private int preparationTime;

    @Column(name = "difficulty", nullable = false)
    private int difficulty;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(
        name = "recipe_recipe_tag",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_tag_id")
    )
    private List<RecipeTag> recipeTags;

    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    private List<RecipeImage> recipeImages;

    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    private List<RecipeRating> recipeRatings;

    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    private List<RecipeStep> recipeSteps;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "recipe_category_id", nullable = false)
    private RecipeCategory recipeCategory;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(
        name = "recipe_ingredient",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @ElementCollection
    @ToString.Exclude
    @CollectionTable(name = "recipe_ingredient_count", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "quantity")
    private Map<Ingredient, Float> ingredientCount;

    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
    private List<PlannedRecipe> plannedRecipes;


}
