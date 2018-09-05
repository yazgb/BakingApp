package com.android.yaz.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.yaz.bakingtime.model.RecipeStep;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsAdapterViewHolder> {

    private static final String TAG = RecipeStepsAdapter.class.getSimpleName();

    private RecipeStep[] mRecipeStepsData;
    private final RecipeStepClickListener mOnClickListener;

    public interface RecipeStepClickListener {
        void stepClick(int stepIndex);
    }

    public RecipeStepsAdapter(RecipeStep[] recipeStepsData, RecipeStepClickListener recipeStepClickListener) {
        mRecipeStepsData = recipeStepsData;
        mOnClickListener = recipeStepClickListener;
    }

    @NonNull
    @Override
    public RecipeStepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_steps_item_layout, parent, false);

        return new RecipeStepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapterViewHolder holder, int position) {
        RecipeStep recipeStep = mRecipeStepsData[holder.getAdapterPosition()];
        holder.mRecipeStepTextView.setText(recipeStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(mRecipeStepsData == null)
            return 0;
        else
            return mRecipeStepsData.length;
    }

    public void setRecipeStepsData(RecipeStep[] recipeStepsData) {
        mRecipeStepsData = recipeStepsData;
        notifyDataSetChanged();
    }

    public class RecipeStepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_step_tv) TextView mRecipeStepTextView;

        public RecipeStepsAdapterViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int recipeStepIndex = getAdapterPosition();
            mOnClickListener.stepClick(recipeStepIndex);
        }
    }
}