package com.rustwebdev.sweetsuite.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

  @SerializedName("name") @Expose private final String name;
  @SerializedName("ingredients") @Expose private ArrayList<Ingredient> ingredients = null;
  @SerializedName("steps") @Expose private List<Step> steps = null;
  @SerializedName("servings") @Expose private final String servings;


  public String getName() {
    return name;
  }

  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public String getServings() {
    return servings;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeTypedList(ingredients);
    dest.writeTypedList(steps);
    dest.writeString(servings);
  }

  private Recipe(Parcel in) {
    name = in.readString();
    ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    steps = in.createTypedArrayList(Step.CREATOR);
    servings = in.readString();
  }

  public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
    @Override public Recipe createFromParcel(Parcel in) {
      return new Recipe(in);
    }

    @Override public Recipe[] newArray(int size) {
      return new Recipe[size];
    }
  };
}
