package com.android.yaz.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.yaz.bakingtime.model.Ingredient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private Ingredient[] mIngredientsData;

    public IngredientsAdapter(Ingredient[] ingredientsData) {
        mIngredientsData = ingredientsData;
    }

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredients_item_layout, parent, false);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        Ingredient ingredient = mIngredientsData[holder.getAdapterPosition()];
        holder.mQuantityTextView.setText(ingredient.getQuantity());
        holder.mMeasureTextView.setText(ingredient.getMeasure());
        holder.mIngredientTextView.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if(mIngredientsData == null)
            return 0;
        else
            return mIngredientsData.length;
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.quantity_tv) TextView mQuantityTextView;
        @BindView(R.id.measure_tv) TextView mMeasureTextView;
        @BindView(R.id.ingredient_tv) TextView mIngredientTextView;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
