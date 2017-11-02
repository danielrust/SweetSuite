package com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DtoIngredient implements Parcelable {

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

  private DtoIngredient(Parcel in) {
    this.quantity = in.readString();
    this.measure = in.readString();
    this.ingredient = in.readString();
  }

  public static final Parcelable.Creator<DtoIngredient> CREATOR =
      new Parcelable.Creator<DtoIngredient>() {
        @Override public DtoIngredient createFromParcel(Parcel source) {
          return new DtoIngredient(source);
        }

        @Override public DtoIngredient[] newArray(int size) {
          return new DtoIngredient[size];
        }
      };
}