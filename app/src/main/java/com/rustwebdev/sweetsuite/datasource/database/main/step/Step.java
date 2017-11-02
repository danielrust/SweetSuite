package com.rustwebdev.sweetsuite.datasource.database.main.step;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "step") public class Step {
  public Step(long recipe_id, String shortDescription, String description, String videoURL,
      String thumbnailURL) {
    this.recipe_id = recipe_id;
    this.shortDescription = shortDescription;
    this.description = description;
    this.videoURL = videoURL;
    this.thumbnailURL = thumbnailURL;
  }

  @PrimaryKey(autoGenerate = true) int id;
  long recipe_id;
  String shortDescription;

  String description;

  String videoURL;

  String thumbnailURL;
}
