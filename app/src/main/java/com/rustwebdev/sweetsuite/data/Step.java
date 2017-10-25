
package com.rustwebdev.sweetsuite.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {

  @SerializedName("id")
  @Expose
  private final Integer id;
  @SerializedName("shortDescription")
  @Expose
  private final String shortDescription;
  @SerializedName("description")
  @Expose
  private final String description;
  @SerializedName("videoURL")
  @Expose
  private final String videoURL;
  @SerializedName("thumbnailURL")
  @Expose
  private final String thumbnailURL;

  public String getShortDescription() {
    return shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public String getVideoURL() {
    return videoURL;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.shortDescription);
    dest.writeString(this.description);
    dest.writeString(this.videoURL);
    dest.writeString(this.thumbnailURL);
  }

  private Step(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.shortDescription = in.readString();
    this.description = in.readString();
    this.videoURL = in.readString();
    this.thumbnailURL = in.readString();
  }

  public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
    @Override public Step createFromParcel(Parcel source) {
      return new Step(source);
    }

    @Override public Step[] newArray(int size) {
      return new Step[size];
    }
  };
}