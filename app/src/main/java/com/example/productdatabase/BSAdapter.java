
package com.example.productdatabase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productdatabase.R;

//import java.lang.reflect.Array;
import java.util.ArrayList;

public class BSAdapter extends RecyclerView.Adapter<BSAdapter.BSView> {
    Context context;
    ArrayList<Integer> list = new ArrayList<Integer>();


    public class BSView extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView card1,card2,card3;
        TextView txt1,txt2,txt3;
        public BSView(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.txt1);
            txt2 = itemView.findViewById(R.id.txt2);
            txt3 = itemView.findViewById(R.id.txt3);
            card1= itemView.findViewById(R.id.Card1);
            card1.setOnClickListener(this);
            card2= itemView.findViewById(R.id.Card1);
            card2.setOnClickListener(this);
            card3= itemView.findViewById(R.id.Card1);
            card3.setOnClickListener(this);
            //txt2.setOnClickListener(this);
            //txt3.setOnClickListener(this);
        }
        public void setTxt1(String s){
            this.txt1.setText(s);
        }
        @Override
        public void onClick(View v){
            ((SecondActivity)context).onRecyclerClick(v, getLayoutPosition());
        }
    }
    public BSAdapter(Context context,ArrayList<Integer> l){ this.context=context; this.list=l; }
    public void resetListReference(ArrayList l){ list=l;}

    @Override
    public int getItemCount() { return list == null ? 0 : list.size(); }
    @Override
    public BSView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view, parent, false);
        return new BSView(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BSView holder, int position) {
        Integer i = list.get(position);
        holder.setTxt1(i.toString());
    }
}
