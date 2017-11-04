package com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class DtoRecipe implements Parcelable {

  @SerializedName("name") @Expose private final String name;
  @SerializedName("ingredients") @Expose private ArrayList<DtoIngredient> ingredients;
  @SerializedName("steps") @Expose private List<DtoStep> steps ;
  @SerializedName("servings") @Expose private final String servings;

  public String getName() {
    return name;
  }

  public ArrayList<DtoIngredient> getIngredients() {
    return ingredients;
  }

  public List<DtoStep> getDtoSteps() {
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

  public DtoRecipe(Parcel in) {
    name = in.readString();
    ingredients = in.createTypedArrayList(DtoIngredient.CREATOR);
    steps = in.createTypedArrayList(DtoStep.CREATOR);
    servings = in.readString();
  }

  public static final Creator<DtoRecipe> CREATOR = new Creator<DtoRecipe>() {
    @Override public DtoRecipe createFromParcel(Parcel in) {
      return new DtoRecipe(in);
    }

    @Override public DtoRecipe[] newArray(int size) {
      return new DtoRecipe[size];
    }
  };
}
