package com.rustwebdev.sweetsuite.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


public class ListRemoteViewService implements RemoteViewsService.RemoteViewsFactory {
  final Context mContext;
  Cursor mCursor;

  public ListRemoteViewService(Context mContext) {
    this.mContext = mContext;
  }

  @Override public void onCreate() {

  }

  @Override public void onDataSetChanged() {

  }

  @Override public void onDestroy() {

  }

  @Override public int getCount() {
    return 0;
  }

  @Override public RemoteViews getViewAt(int position) {
    return null;
  }

  @Override public RemoteViews getLoadingView() {
    return null;
  }

  @Override public int getViewTypeCount() {
    return 0;
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public boolean hasStableIds() {
    return false;
  }
}
