package com.android.yaz.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yaz.bakingtime.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private Context context;
    private List<Recipe> mRecipesData;
    private final ItemClickListener mOnClickListener;

    public interface ItemClickListener {
        void itemClick(Recipe clickedRecipe);
    }

    public RecipesAdapter(Context context, List<Recipe> recipesData, ItemClickListener onClickListener) {
        this.context = context;
        this.mRecipesData = recipesData;
        this.mOnClickListener = onClickListener;
    }

    public List<Recipe> getRecipesData() {
        return mRecipesData;
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.baking_item_layout, parent, false);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipesData.get(holder.getAdapterPosition());
        String recipeName = recipe.getName();
        String recipeImage = recipe.getImage();

        if(recipeImage == null || recipeImage.isEmpty()) {
            holder.mRecipeImageView.setImageResource(R.drawable.ic_cake);
        } else {
            Picasso.with(holder.itemView.getContext())
                    .load(recipeImage)
                    .placeholder(R.drawable.ic_cake)
                    .error(R.drawable.ic_error)
                    .into(holder.mRecipeImageView);
        }

        holder.mRecipeTextView.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        if(mRecipesData == null)
            return 0;
        else
            return mRecipesData.size();
    }

    public void setRecipesData(List<Recipe> mRecipesData) {
        this.mRecipesData = mRecipesData;
        notifyDataSetChanged();
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_image) ImageView mRecipeImageView;
        @BindView(R.id.recipe_name_tv) TextView mRecipeTextView;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            Recipe recipe = mRecipesData.get(itemPosition);
            mOnClickListener.itemClick(recipe);
        }
    }
}
