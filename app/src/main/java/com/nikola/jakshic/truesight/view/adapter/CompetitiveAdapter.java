package com.nikola.jakshic.truesight.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemCompetitiveBinding;
import com.nikola.jakshic.truesight.inspector.CompetitiveInspector;
import com.nikola.jakshic.truesight.model.Competitive;
import com.nikola.jakshic.truesight.view.activity.MatchActivity;

public class CompetitiveAdapter extends DataAdapter<Competitive, ItemCompetitiveBinding> {

    public CompetitiveAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemCompetitiveBinding> holder, Competitive item) {
        holder.binding.setInspector(new CompetitiveInspector(context, item));
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, MatchActivity.class);
            intent.putExtra("match-id", item.getMatchId());
            context.startActivity(intent);
        });
    }

    @Override
    public ItemCompetitiveBinding createBinding(ViewGroup parent) {
        return ItemCompetitiveBinding.inflate(
                LayoutInflater.from(
                        parent.getContext()), parent, false);
    }
}