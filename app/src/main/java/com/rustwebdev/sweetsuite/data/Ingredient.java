package com.rustwebdev.sweetsuite.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

  @SerializedName("quantity") @Expose private final String quantity;
  @SerializedName("measure") @Expose private final String measure;
  @SerializedName("ingredient") @Expose private final String ingredient;

  public String getQuantity() {
    return quantity;
  }

  public String getMeasure() {
    return measure;
  }

  public String getIngredient() {
    return ingredient;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.quantity);
    dest.writeString(this.measure);
    dest.writeString(this.ingredient);
  }

  private Ingredient(Parcel in) {
    this.quantity = in.readString();
    this.measure = in.readString();
    this.ingredient = in.readString();
  }

  public static final Parcelable.Creator<Ingredient> CREATOR =
      new Parcelable.Creator<Ingredient>() {
        @Override public Ingredient createFromParcel(Parcel source) {
          return new Ingredient(source);
        }

        @Override public Ingredient[] newArray(int size) {
          return new Ingredient[size];
        }
      };
}