
package com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DtoStep implements Parcelable {

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

  public Integer getId() {
    return id;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

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

  private DtoStep(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.shortDescription = in.readString();
    this.description = in.readString();
    this.videoURL = in.readString();
    this.thumbnailURL = in.readString();
  }

  public static final Parcelable.Creator<DtoStep> CREATOR = new Parcelable.Creator<DtoStep>() {
    @Override public DtoStep createFromParcel(Parcel source) {
      return new DtoStep(source);
    }

    @Override public DtoStep[] newArray(int size) {
      return new DtoStep[size];
    }
  };
}