package com.rustwebdev.sweetsuite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.rustwebdev.sweetsuite.data.Ingredient;
import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
  private final List<Ingredient> ingredients;
  private final Context context;

  public IngredientsAdapter(Context context, ArrayList<Ingredient> ingredients) {
    this.context = context;
    this.ingredients = ingredients;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.ingredient_list_item, parent, false);
    return new ViewHolder(v);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Ingredient ingredient = ingredients.get(position);
    holder.ingredientNameTv.setText(
        context.getString(R.string.ingredient_name_tv_text, ingredient.getQuantity(),
            ingredient.getMeasure(), ingredient.getIngredient()));
  }

  @Override public int getItemCount() {
    return ingredients.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ingredient_tv) TextView ingredientNameTv;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
