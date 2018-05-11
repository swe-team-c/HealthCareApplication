package edu.swe.healthcareapplication.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import edu.swe.healthcareapplication.R;
import edu.swe.healthcareapplication.model.Trainer;
import edu.swe.healthcareapplication.view.adapter.SelectTrainerAdapter.ViewHolder;
import edu.swe.healthcareapplication.view.listener.OnItemSelectListener;
import java.util.ArrayList;
import java.util.List;

public class SelectTrainerAdapter extends RecyclerView.Adapter<ViewHolder> {

  private List<Trainer> mTrainerList = new ArrayList<>();
  private OnItemSelectListener<Trainer> mListener;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_select_trainer, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Trainer trainer = mTrainerList.get(position);
    holder.bind(trainer, mListener);
  }

  @Override
  public int getItemCount() {
    return mTrainerList.size();
  }

  public void setTrainerList(List<Trainer> mTrainerList) {
    this.mTrainerList = mTrainerList;
    notifyDataSetChanged();
  }

  public void setOnItemSelectListener(
      OnItemSelectListener<Trainer> onItemSelectListener) {
    this.mListener = onItemSelectListener;
  }

  protected class ViewHolder extends RecyclerView.ViewHolder {

    private TextView nameView;
    private TextView educationView;
    private TextView awardsView;
    private Button selectButton;

    public ViewHolder(View itemView) {
      super(itemView);
      nameView = itemView.findViewById(R.id.tv_name);
      educationView = itemView.findViewById(R.id.tv_education);
      awardsView = itemView.findViewById(R.id.tv_awards);
      selectButton = itemView.findViewById(R.id.btn_select);
    }

    public void bind(Trainer trainer, OnItemSelectListener<Trainer> listener) {
      nameView.setText(trainer.name);
      educationView.setText(trainer.education);
      awardsView.setText(trainer.awards.get(0));
      selectButton.setOnClickListener(v -> {
        if (listener != null) {
          listener.onItemSelected(trainer);
        }
      });
    }
  }
}
