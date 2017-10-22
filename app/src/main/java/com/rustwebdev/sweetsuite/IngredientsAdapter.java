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

/**
 * Created by flanhelsinki on 10/21/17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
  private List<Ingredient> ingredients;
  private IngredientItemListener itemListener;
  private Context context;

  public IngredientsAdapter(Context context, ArrayList<Ingredient> ingredients, IngredientItemListener itemListener) {
    this.context = context;
    this.ingredients = ingredients;
    this.itemListener = itemListener;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
    return new ViewHolder(v, this.itemListener);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Ingredient ingredient = ingredients.get(position);
    holder.ingredientNameTv.setText("· " + ingredient.getQuantity() + " " + ingredient.getMeasure() + " "  + ingredient.getIngredient());
  }

  @Override public int getItemCount() {
    return ingredients.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.ingredient_tv) TextView ingredientNameTv;
    IngredientItemListener itemListener;

    public ViewHolder(View itemView, IngredientItemListener itemListener) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      this.itemListener = itemListener;
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      Ingredient ingredient = ingredients.get(getAdapterPosition());
      this.itemListener.onIngredientClick(ingredient);
    }
  }

  public interface IngredientItemListener {
    void onIngredientClick(Ingredient ingredient);
  }
}
