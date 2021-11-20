
package com.example.productdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DBAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Object> list;
    boolean invert=false;
    int size=0;

    int row=3;
    int separate=10;
    public void resetListReference(ArrayList<Object> l){ list=l;}
    public void setInvert(boolean n){invert=n;}
    public boolean getInvert(){return invert;}

    public class DBView extends RecyclerView.ViewHolder{
        ArrayList<CardView> cards=new ArrayList<>();
        ArrayList<TextView> txts=new ArrayList<>();
        LinearLayout linLayout;
        int active=0;
        public class OnClick implements View.OnClickListener{
            int pos;
            DBView db;
            public OnClick(int pos, DBView db){
                this.pos=pos;
                this.db=db;
            }
            @Override
            public void onClick(View v){
                this.db.onClick(v,pos);
            }
        }
        public DBView(@NonNull View itemView) {
            super(itemView);
            this.linLayout=(LinearLayout) itemView;
            for(int n=0;n<row;n++){
                cards.add((CardView)this.linLayout.getChildAt(n));
                txts.add((TextView) cards.get(n).getChildAt(0) );
                cards.get(n).setOnClickListener(new OnClick(n,this));
                //Log.e(R.class.getName(),"aaa="+n);
            }
        }
        public void updateText(int pos){
            this.active=0;
            for(int n=0;n<row;n++){

                int i=pos*row+n;
                //Log.e(R.class.getName(),"nooo="+pos);
                if(i<list.size()){
                    txts.get(n).setText(list.get(i).toString());
                    cards.get(n).setVisibility(View.VISIBLE);
                    this.active+=1;
                }else{
                    cards.get(n).setVisibility(View.GONE);
                    //Log.e(R.class.getName(),"what="+pos+","+n);
                }
                //Log.e(R.class.getName(),"idk="+cards.get(n).getVisibility());
            }
            this.linLayout.setWeightSum(this.active);
        }
        public void onClick(View v,int relpos){
            int pos=getLayoutPosition();
            pos=invert?pos-size/(separate+1)+(size-1-pos)/(separate+1)
                    :pos-(pos)/(separate+1);
            ((SecondActivity)context).onRecyclerClick(v, pos*row+relpos);
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

    public void notifyDBAppend(){
        if(list.size()%row==1){
            if ((getItemCount()-1)%(separate+1)==0) {
                notifyItemRangeInserted(size-2,2);
            }else{
                notifyItemInserted(size-1);
            }
        }else{
            notifyItemChanged(size-1);
        }
    }
    public void notifyDBInsert(int n){
        if (n<list.size()-1) {
            int pos = n / row + n / (row*separate);
            notifyItemRangeChanged(pos, getItemCount() - pos - (list.size() % row == 1 ? 1 : 0)
                    - (list.size() % (row*separate) == 1 ? 1 : 0));
        }
        if (list.size()%row==1 || n==list.size()-1) {
            notifyDBAppend();
        }
    }
    public void notifyDBDelete(int n) {
        if (list.size() % row == 0) {
            notifyItemRangeRemoved(getItemCount(), ((list.size() + 1) % row == 1 ? 1 : 0)
                    + ((list.size() + 1) % (row*separate) == 1 ? 1 : 0));
        }
        if (n < list.size()||list.size()%3!=0) {
            int pos = n / row + n / (row*separate);
            notifyItemRangeChanged(pos, getItemCount() - pos);
        }
    }
    public void notifyDBInvert(){
        int lim=getItemCount()/(separate+1);
        for(int n=(invert?lim:1);n!=(invert?0:lim+1);n+=(invert?-1:1)){
            notifyItemRemoved(invert?n*(separate+1)-1:getItemCount()-n*(separate+1));
        }
        for(int n=(invert?1:lim);n!=(invert?lim+1:0);n+=(invert?1:-1)){
            notifyItemInserted(invert?(list.size()-1)/row+1-n*(separate):n*(separate));
        }
    }
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
    @Override
    public int getItemViewType(int position) {
        return (((invert?1:0)*(size-1)-(invert?1:-1)*position)%(separate+1))/separate;
    }
    @Override
    public int getItemCount() {
        size= list == null ? 0 : (list.size()-1)/row+1+(list.size()-1)/(row*separate);
        //Log.e(R.class.getName(),"size="+size);
        return size;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
        LinearLayout linLayout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.recycle_view, parent, false);
        for(int n=0;n<row;n++){
            View view=LayoutInflater.from(context).inflate(R.layout.card_contain, parent, false);
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(view.getLayoutParams());
            params.weight=1;
            params.leftMargin=Main.dpToPxi(4,context);
            params.rightMargin=params.leftMargin;
            linLayout.addView(view, params);
        }
        linLayout.setWeightSum(row);
        //Log.e(R.class.getName(),"noway="+linLayout.getWeightSum());
        //Log.e(R.class.getName(),"child="+linLayout.getChildCount());
        //Log.e(R.class.getName(),"new view");
        return new DBView(linLayout);
        }else{
            View view =LayoutInflater.from(context).inflate(R.layout.separator, parent, false);
            return new Separator(view);
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Arrays.binarysearch
        getItemCount();
        if (holder instanceof DBView){
            int adjustedpos=invert?position-size/(separate+1)+(size-1-position)/(separate+1)
                    :position-(position)/(separate+1);
            DBView d=(DBView)holder;
            d.updateText(adjustedpos);
//            d.setTxt1(list.get(adjustedpos*row).toString());
//
//            if (list.size()>adjustedpos*row+1) {
//                d.setCard2Visible(0);
//                d.setTxt2(list.get(adjustedpos * row + 1).toString());
//            }else{d.setCard2Visible(8);}
//            if (list.size()>adjustedpos*row+2) {
//                d.setCard3Visible(0);
//                d.setTxt3(list.get(adjustedpos * row + 2).toString());
//            }else{d.setCard3Visible(8);}
        }else{
            ((Separator)holder).setText(String.valueOf(invert?((size-position)/(separate+1))*(row*separate)
                    :((position+1)/(separate+1))*(row*separate)));
        }

    }
}
