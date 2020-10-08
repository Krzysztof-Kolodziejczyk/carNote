package com.example.carnote.historylist;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carnote.R;
import com.example.carnote.model.TankUpRecord;

import java.text.DateFormat;
import java.util.List;


// Adapter budujący elementy listy historii na podstawie historii auta

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>//RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
{

    private final List<TankUpRecord> tankList;
    private final Context context;
    private TankUpRecord tankUpRecord;
    private Drawable drawable;

    public HistoryAdapter(Context context, List<TankUpRecord> tankList)
    {
        this.context = context;
        this.tankList = tankList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_item, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        tankUpRecord = tankList.get(position);
        if(tankUpRecord.getColliedCarName() == null)
        {
            drawable = context.getResources().getDrawable(R.drawable.ic_new_tankup);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.leftLabelTopTextVIew.setText(dateFormat.format(tankUpRecord.getTankUpDate()));
            holder.rightLabelTopTextVIew.setText(tankUpRecord.getMiallage().toString() + " KM");
            holder.leftLabelBottomTextVIew.setText(tankUpRecord.getLiters() + " L");
            holder.rightLabelBottomTextVIew.setText("-" +tankUpRecord.getCost().toString() + " PLN");

            holder.trashImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tankList.remove(position);
                    HistoryAdapter.this.notifyDataSetChanged();
                }
            });
        }
        else if(tankUpRecord.getLiters() == -1)
        {
            tankUpRecord = tankList.get(position);
            drawable = context.getResources().getDrawable(R.drawable.ic_new_crush);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.leftLabelTopTextVIew.setText(dateFormat.format(tankUpRecord.getTankUpDate()));
            holder.rightLabelTopTextVIew.setText("");
            holder.leftLabelBottomTextVIew.setText(tankUpRecord.getColliedCarName());
            holder.rightLabelBottomTextVIew.setText("");

            holder.trashImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tankList.remove(position);
                    HistoryAdapter.this.notifyDataSetChanged();
                }
            });
        }
        else
        {
            tankUpRecord = tankList.get(position);
            drawable = context.getResources().getDrawable(R.drawable.ic_new_repair);
            holder.activityImageView.setImageDrawable(drawable);
            DateFormat dateFormat = DateFormat.getDateInstance();
            holder.leftLabelTopTextVIew.setText(dateFormat.format(tankUpRecord.getTankUpDate()));
            holder.rightLabelTopTextVIew.setText("");
            holder.leftLabelBottomTextVIew.setText(tankUpRecord.getColliedCarName());
            holder.rightLabelBottomTextVIew.setText("-" +tankUpRecord.getLiters().toString() + " PLN");

            holder.trashImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tankList.remove(position);
                    HistoryAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }



    @Override
    public int getItemCount()
    {
        if(tankList == null)
        {
            return 0;
        }
        else
        {
            return tankList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView trashImageView;
        protected ImageView activityImageView;

        protected TextView leftLabelTopTextVIew;
        protected TextView rightLabelTopTextVIew;
        protected TextView rightLabelBottomTextVIew;
        protected TextView leftLabelBottomTextVIew;

        public ViewHolder(View itemView)
        {
            super(itemView);

            this.activityImageView = (ImageView) itemView.findViewById(R.id.activity_image_view);
            this.trashImageView = (ImageView) itemView.findViewById(R.id.trash);

            this.leftLabelBottomTextVIew = (TextView) itemView.findViewById(R.id.left_label_top);
            this.leftLabelTopTextVIew = (TextView) itemView.findViewById(R.id.left_label_bottom);
            this.rightLabelTopTextVIew = (TextView) itemView.findViewById(R.id.right_label_top);
            this.rightLabelBottomTextVIew = (TextView) itemView.findViewById(R.id.right_label_bottom);


        }
    }
}