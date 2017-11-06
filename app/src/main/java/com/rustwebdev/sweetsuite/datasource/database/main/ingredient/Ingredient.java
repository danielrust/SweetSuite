package com.rustwebdev.sweetsuite.datasource.database.main.ingredient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "ingredient") public class Ingredient implements Parcelable {
  public Ingredient(long recipe_id, String quantity, String measure, String ingredient) {
    this.quantity = quantity;
    this.measure = measure;
    this.ingredient = ingredient;
    this.recipe_id = recipe_id;
  }

  @PrimaryKey(autoGenerate = true) public int id;
  public final long recipe_id;
  public final String quantity;
  public final String measure;
  public final String ingredient;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeLong(this.recipe_id);
    dest.writeString(this.quantity);
    dest.writeString(this.measure);
    dest.writeString(this.ingredient);
  }

  protected Ingredient(Parcel in) {
    this.id = in.readInt();
    this.recipe_id = in.readLong();
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
