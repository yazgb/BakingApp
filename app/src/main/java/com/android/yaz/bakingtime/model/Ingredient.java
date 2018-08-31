package com.android.yaz.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{

    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredient(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    private Ingredient(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public String getIngredient() { return ingredient; }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
