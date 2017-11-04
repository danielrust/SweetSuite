package com.rustwebdev.sweetsuite.datasource.database.main.recipe;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "recipe") public class Recipe implements Parcelable {
  public Recipe(String name, String servings) {
    this.name = name;
    this.servings = servings;
  }

  @PrimaryKey(autoGenerate = true) public int id;
  public final String name;
  public final String servings;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
    dest.writeString(this.servings);
  }

  protected Recipe(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
    this.servings = in.readString();
  }

  public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
    @Override public Recipe createFromParcel(Parcel source) {
      return new Recipe(source);
    }

    @Override public Recipe[] newArray(int size) {
      return new Recipe[size];
    }
  };
}