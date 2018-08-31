package com.android.yaz.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable{

    private int id;
    private String name;
    private Ingredient[] ingredients;
    private RecipeStep[] steps;
    private String servings;
    private String image;

    public Recipe(int id, String name, Ingredient[] ingredients, RecipeStep[] steps,
                  String servings, String image) {

        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArray(Ingredient.CREATOR);
        steps = in.createTypedArray(RecipeStep.CREATOR);
        servings = in.readString();
        image = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public RecipeStep[] getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(RecipeStep[] steps) {
        this.steps = steps;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedArray(ingredients, i);
        parcel.writeTypedArray(steps, i);
        parcel.writeString(servings);
        parcel.writeString(image);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };
}

