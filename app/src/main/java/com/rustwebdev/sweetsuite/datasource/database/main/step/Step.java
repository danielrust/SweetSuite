package com.rustwebdev.sweetsuite.datasource.database.main.step;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "step") public class Step implements Parcelable {
  public Step(long recipe_id, String shortDescription, String description, String videoURL,
      String thumbnailURL) {
    this.recipe_id = recipe_id;
    this.shortDescription = shortDescription;
    this.description = description;
    this.videoURL = videoURL;
    this.thumbnailURL = thumbnailURL;
  }

  @PrimaryKey(autoGenerate = true) public int id;
  public final long recipe_id;
  public final String shortDescription;

  public final String description;

  public final String videoURL;

  public final String thumbnailURL;

  protected Step(Parcel in) {
    id = in.readInt();
    recipe_id = in.readLong();
    shortDescription = in.readString();
    description = in.readString();
    videoURL = in.readString();
    thumbnailURL = in.readString();
  }

  public static final Creator<Step> CREATOR = new Creator<Step>() {
    @Override public Step createFromParcel(Parcel in) {
      return new Step(in);
    }

    @Override public Step[] newArray(int size) {
      return new Step[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeLong(recipe_id);
    dest.writeString(shortDescription);
    dest.writeString(description);
    dest.writeString(videoURL);
    dest.writeString(thumbnailURL);
  }
}
