package com.rustwebdev.sweetsuite.datasource.database.main.step;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao public interface StepDao {
  @Insert void insertStep(Step step);

  @Query("SELECT * FROM step WHERE recipe_id = :id") List<Step> getSteps(long id);
}
