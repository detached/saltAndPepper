/*
 * This file is generated by jOOQ.
 */
package de.w3is.recipes.infra.persistence.generated.tables.records;


import de.w3is.recipes.infra.persistence.generated.tables.Recipes;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RecipesRecord extends UpdatableRecordImpl<RecipesRecord> implements Record11<Integer, String, String, String, String, String, String, String, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.recipes.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.recipes.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.recipes.recipe_id</code>.
     */
    public void setRecipeId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.recipes.recipe_id</code>.
     */
    public String getRecipeId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.recipes.author_id</code>.
     */
    public void setAuthorId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.recipes.author_id</code>.
     */
    public String getAuthorId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.recipes.title</code>.
     */
    public void setTitle(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.recipes.title</code>.
     */
    public String getTitle() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.recipes.category</code>.
     */
    public void setCategory(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.recipes.category</code>.
     */
    public String getCategory() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.recipes.cuisine</code>.
     */
    public void setCuisine(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.recipes.cuisine</code>.
     */
    public String getCuisine() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.recipes.yields</code>.
     */
    public void setYields(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.recipes.yields</code>.
     */
    public String getYields() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.recipes.ingredients</code>.
     */
    public void setIngredients(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.recipes.ingredients</code>.
     */
    public String getIngredients() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.recipes.instructions</code>.
     */
    public void setInstructions(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.recipes.instructions</code>.
     */
    public String getInstructions() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.recipes.modifications</code>.
     */
    public void setModifications(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.recipes.modifications</code>.
     */
    public String getModifications() {
        return (String) get(9);
    }

    /**
     * Setter for <code>public.recipes.images</code>.
     */
    public void setImages(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.recipes.images</code>.
     */
    public String getImages() {
        return (String) get(10);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record11 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row11<Integer, String, String, String, String, String, String, String, String, String, String> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    @Override
    public Row11<Integer, String, String, String, String, String, String, String, String, String, String> valuesRow() {
        return (Row11) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Recipes.RECIPES.ID;
    }

    @Override
    public Field<String> field2() {
        return Recipes.RECIPES.RECIPE_ID;
    }

    @Override
    public Field<String> field3() {
        return Recipes.RECIPES.AUTHOR_ID;
    }

    @Override
    public Field<String> field4() {
        return Recipes.RECIPES.TITLE;
    }

    @Override
    public Field<String> field5() {
        return Recipes.RECIPES.CATEGORY;
    }

    @Override
    public Field<String> field6() {
        return Recipes.RECIPES.CUISINE;
    }

    @Override
    public Field<String> field7() {
        return Recipes.RECIPES.YIELDS;
    }

    @Override
    public Field<String> field8() {
        return Recipes.RECIPES.INGREDIENTS;
    }

    @Override
    public Field<String> field9() {
        return Recipes.RECIPES.INSTRUCTIONS;
    }

    @Override
    public Field<String> field10() {
        return Recipes.RECIPES.MODIFICATIONS;
    }

    @Override
    public Field<String> field11() {
        return Recipes.RECIPES.IMAGES;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getRecipeId();
    }

    @Override
    public String component3() {
        return getAuthorId();
    }

    @Override
    public String component4() {
        return getTitle();
    }

    @Override
    public String component5() {
        return getCategory();
    }

    @Override
    public String component6() {
        return getCuisine();
    }

    @Override
    public String component7() {
        return getYields();
    }

    @Override
    public String component8() {
        return getIngredients();
    }

    @Override
    public String component9() {
        return getInstructions();
    }

    @Override
    public String component10() {
        return getModifications();
    }

    @Override
    public String component11() {
        return getImages();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getRecipeId();
    }

    @Override
    public String value3() {
        return getAuthorId();
    }

    @Override
    public String value4() {
        return getTitle();
    }

    @Override
    public String value5() {
        return getCategory();
    }

    @Override
    public String value6() {
        return getCuisine();
    }

    @Override
    public String value7() {
        return getYields();
    }

    @Override
    public String value8() {
        return getIngredients();
    }

    @Override
    public String value9() {
        return getInstructions();
    }

    @Override
    public String value10() {
        return getModifications();
    }

    @Override
    public String value11() {
        return getImages();
    }

    @Override
    public RecipesRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public RecipesRecord value2(String value) {
        setRecipeId(value);
        return this;
    }

    @Override
    public RecipesRecord value3(String value) {
        setAuthorId(value);
        return this;
    }

    @Override
    public RecipesRecord value4(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public RecipesRecord value5(String value) {
        setCategory(value);
        return this;
    }

    @Override
    public RecipesRecord value6(String value) {
        setCuisine(value);
        return this;
    }

    @Override
    public RecipesRecord value7(String value) {
        setYields(value);
        return this;
    }

    @Override
    public RecipesRecord value8(String value) {
        setIngredients(value);
        return this;
    }

    @Override
    public RecipesRecord value9(String value) {
        setInstructions(value);
        return this;
    }

    @Override
    public RecipesRecord value10(String value) {
        setModifications(value);
        return this;
    }

    @Override
    public RecipesRecord value11(String value) {
        setImages(value);
        return this;
    }

    @Override
    public RecipesRecord values(Integer value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10, String value11) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RecipesRecord
     */
    public RecipesRecord() {
        super(Recipes.RECIPES);
    }

    /**
     * Create a detached, initialised RecipesRecord
     */
    public RecipesRecord(Integer id, String recipeId, String authorId, String title, String category, String cuisine, String yields, String ingredients, String instructions, String modifications, String images) {
        super(Recipes.RECIPES);

        setId(id);
        setRecipeId(recipeId);
        setAuthorId(authorId);
        setTitle(title);
        setCategory(category);
        setCuisine(cuisine);
        setYields(yields);
        setIngredients(ingredients);
        setInstructions(instructions);
        setModifications(modifications);
        setImages(images);
    }
}
