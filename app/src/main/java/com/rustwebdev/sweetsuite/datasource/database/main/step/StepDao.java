package com.rustwebdev.sweetsuite.datasource.database.main.step;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface StepDao {
  @Insert long insertStep(Step step);

}
