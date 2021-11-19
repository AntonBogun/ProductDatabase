
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

public class DBAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Object> list;
    int invert=0;
    int size=0;
    public class DBView extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView card1,card2,card3;
        TextView txt1,txt2,txt3;
        public DBView(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.txt1);
            txt2 = itemView.findViewById(R.id.txt2);
            txt3 = itemView.findViewById(R.id.txt3);
            card1= itemView.findViewById(R.id.Card1);
            card1.setOnClickListener(this);
            card2= itemView.findViewById(R.id.Card2);
            card2.setOnClickListener(this);
            card3= itemView.findViewById(R.id.Card3);
            card3.setOnClickListener(this);
            //Log.e(R.class.getName(),String.format("make %s,%s,%s",txt1.getText(),txt2.getText(),txt3.getText()));
            //txt2.setOnClickListener(this);
            //txt3.setOnClickListener(this);
        }

        public void setTxt1(String s){
            this.txt1.setText(s);
        }
        public void setTxt2(String s){
            this.txt2.setText(s);
        }
        public void setTxt3(String s){
            this.txt3.setText(s);
        }
        public void setCard1Visible(int b){
            this.card1.setVisibility(b);
        }
        public void setCard2Visible(int b){
            this.card2.setVisibility(b);
        }
        public void setCard3Visible(int b){
            this.card3.setVisibility(b);
        }
        @Override
        public void onClick(View v){
            ((SecondActivity)context).onRecyclerClick(v, getLayoutPosition());
        }
    }
    public class Separator extends RecyclerView.ViewHolder{
        TextView text;
        public Separator(@NonNull View itemView){
            super(itemView);
            text=itemView.findViewById(R.id.septext);
        }
        public void setText(String s){this.text.setText(s);}
    }
    public DBAdapter(Context context,ArrayList<Object> l){
        this.context=context;
        this.list=l;
        //setHasStableIds(true);
    }

    public void notifyItemAppend(){
        if(list.size()%3==1){
            if ((getItemCount()-1)%11==0) {
                notifyItemRangeInserted(size-2,2);
            }else{
                notifyItemInserted(size-1);
            }
        }else{
            notifyItemChanged(size-1);
        }
    }
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
    @Override
    public int getItemViewType(int position) {
        return ((invert*(size-1)-((invert*2)-1)*position)%11)/10;
    }
    public void resetListReference(ArrayList<Object> l){ list=l;}

    @Override
    public int getItemCount() {
        size= list == null ? 0 : (list.size()-1)/3+1+(list.size()-1)/30;
        //Log.e(R.class.getName(),"size="+size);
        return size;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view, parent, false);
        //Log.e(R.class.getName(),"new view");
        return new DBView(view);
        }else{
            View view =LayoutInflater.from(context).inflate(R.layout.separator, parent, false);
            return new Separator(view);
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Arrays.binarysearch

        if (holder instanceof DBView){
            int adjustedpos=position-(position)/11;
            DBView d=(DBView)holder;
            d.setTxt1(list.get(adjustedpos*3).toString());

            if (list.size()>adjustedpos*3+1) {
                d.setCard2Visible(0);
                d.setTxt2(list.get(adjustedpos * 3 + 1).toString());
            }else{d.setCard2Visible(8);}
            if (list.size()>adjustedpos*3+2) {
                d.setCard3Visible(0);
                d.setTxt3(list.get(adjustedpos * 3 + 2).toString());
            }else{d.setCard3Visible(8);}
        }else{
            ((Separator)holder).setText(String.valueOf(((position+1)/11)*30));
        }

    }
}
