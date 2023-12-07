package com.example.gabinet_psychologiczny.Other;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabinet_psychologiczny.Database.Relations.VisitWithAnnotationsAndPatientAndService;
import com.example.gabinet_psychologiczny.Model.Annotation;
import com.example.gabinet_psychologiczny.R;

import java.util.ArrayList;
import java.util.List;

public class AnnotationsRecyclerViewAdapter extends RecyclerView.Adapter<AnnotationsRecyclerViewAdapter.MyViewHolder>{
    List<Annotation> annotationsList = new ArrayList<>();
    private AnnotationsRecyclerViewAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public AnnotationsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.annotations_recycler_view_item, parent, false);
        return new AnnotationsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnotationsRecyclerViewAdapter.MyViewHolder holder, int position) {
        Annotation currentAnnotation = annotationsList.get(position);
        String name = currentAnnotation.getName();
        int type = currentAnnotation.getType();

        holder.name.setText(name);
        setTypeIcon(type, holder.icon, holder.resources);
    }
    private void setTypeIcon(int annotationType, ImageView annotationIcon, Resources resources){

        switch (annotationType){
            case 0:
                annotationIcon.setImageResource(R.drawable.baseline_image_24); //image
                break;
            case 1:
                annotationIcon.setImageResource(R.drawable.baseline_audio_file_24); //audio
                break;
            case 2:
                annotationIcon.setImageResource(R.drawable.baseline_text_snippet_24); //text
                break;
        }

    }

    @Override
    public int getItemCount() {
        return annotationsList.size();
    }

    public void setVisitsList(List<Annotation> annotationList){
        this.annotationsList = annotationList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        ImageView editIcon;
        Resources resources;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            resources = itemView.getResources();

            name = itemView.findViewById(R.id.annotationName);
            icon = itemView.findViewById(R.id.annotationTypeIcon);
            editIcon = itemView.findViewById(R.id.editAnnotationIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(annotationsList.get(position));
                    }
                }
            });

            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(annotationsList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Annotation annotation);
        void onEditClick(Annotation annotation);
    }

    public void setOnItemClickListener(AnnotationsRecyclerViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
