package com.example.doctorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerDrListAdapter extends RecyclerView.Adapter<VerDrListAdapter.ViewHolder> {

    List<VerbalDrListModal> verDrList;
    Context context;

    VerDrListAdapter(List<VerbalDrListModal> verDrList,Context context){
        this.context = context;
        this.verDrList = verDrList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_doctor_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VerbalDrListModal verbalDrListModal = verDrList.get(position);
        holder.profileImage.setImageResource(verbalDrListModal.getProfileImage());
        holder.drName.setText(verbalDrListModal.getDrName());
        holder.drDegree.setText(verbalDrListModal.getDeDegree());
        holder.address.setText(verbalDrListModal.getAddress());

    }

    @Override
    public int getItemCount() {
        return verDrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView drName,drDegree,address;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            drName  = itemView.findViewById(R.id.drName);
            drDegree = itemView.findViewById(R.id.degree);
            address = itemView.findViewById(R.id.address);

        }
    }
}
