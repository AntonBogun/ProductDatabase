
package com.example.productdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class NameDBAdapter extends DBAdapter {
    //Context context;
    public class DynamicNamedDB{
        public final Class<?> T;
        public final Class<? extends Main.DBItem> I;

        int rows; //items PER row
        public String format=null;
        protected ArrayList<Container> containers=new ArrayList<>();
        public final Comparator<Object> comparator; //<T>
        public final Function<Object,String> contInfoToString;//display <T,String>
        public final Function<Main.DBItem,Object> getInfo;//adapter <I,T>
        public final int delimID;//delimiter ID for DBItem
        //TODO: looks fine
        public class Container{
            Object info; //<T>
            ArrayList<Main.DBItem> items;//<I>
            int position;
            public Container(Object info, ArrayList<Main.DBItem>items) {
                this.info=info;
                this.items=items;
            }
            public Container(Object info){
                this.info=info;
                this.items=new ArrayList<>();
            }
            public String getLabel(){
                return contInfoToString.apply(info);
            }
            public int evalSize(int rows){
                if (items.size()==0){return 0;}
                return ((items.size()-1)/rows)+1;
            }
        }
        public DynamicNamedDB(int rows, Class<Object> T, Class<? extends Main.DBItem> I, Comparator<Object> comp,
                              Function<Main.DBItem,Object> getInfo, Function<Object,String> contInfoToString,
                              String delim, Main.DBItem dummyitem){
            this.rows=rows;
            this.T=T;
            this.I=I;
            this.comparator=comp;
            this.getInfo=getInfo;
            this.contInfoToString=contInfoToString;
            this.delimID=dummyitem.getId(delim);//because wildcard not allowed for argument type
        }
        public void fromArrayList(ArrayList<Main.DBItem> _arr){//_arr remains unchanged
            ArrayList<Main.DBItem> arr=new ArrayList<>(_arr);
            arr.sort(Comparator.comparing(getInfo::apply,comparator));
            Collections.reverse(arr);
            for (int i = arr.size()-1; i >-1; i--) {
                addToCont(arr.get(i));
                arr.remove(i);
            }
        }


        //initialize positions of containers
        public void positionEval(){
            if (containers.size()==0){ return; }
            int pos=0;
            for (int i = 0; i < containers.size(); i++) {
                containers.get(i).position=pos;
                pos+=1+containers.get(i).evalSize(rows);
            }
        }
        @SuppressWarnings("unchecked")
        public void addToCont(Main.DBItem i){//append to container if exists, otherwise create container
            Object val;  int n;
            if((n=Collections.binarySearch(containers, (val=T.cast(i.getDelimiterInfo(delimID))),//BAD idk if cast needed
                    Comparator.comparing(c -> T.isInstance(c)
                            ? T.cast(c) : ((Container)c).info, comparator)))<0){//BAD idk if cast needed
                containers.add(-1-n,new Container(val));
                containers.get(-1-n).items.add(i);
            }else{
                containers.get(n).items.add(i);
            }
        }
        @SuppressWarnings("unchecked")//find container position of row (pos) using binary search
        public int contPosSearch(int pos){
            int _pos=Collections.binarySearch(containers,pos,
                    Comparator.comparingInt(c->c instanceof Integer?(int)c:((Container)c).position));
            return (_pos<0?-2-_pos:_pos);
        }
        //find container position of row (pos) using nearby check
        public int nearPosSearch(int pos,int contpos){//contpos=container position known
            int contrealpos=containers.get(contpos).position;//pos=current wanted position
            if (contrealpos>pos){//self explanatory
                return contpos-1;
            }//when next cont exists and pos is in next cont
            if(contrealpos<pos && contpos+1<containers.size() && containers.get(contpos+1).position<=pos){
                return contpos+1;
            }
            return contpos;
        }
        @SuppressWarnings("unchecked") //compiler can skidaddle
        public void notifyInsert(Main.DBItem i){//also note that the code is duplicated on notifyDelete
            Object val;//because making a function for this mess loses val output
            int n;
            if((n= Collections.binarySearch(containers, (val=T.cast(i.getDelimiterInfo(delimID))),//BAD idk if cast needed
                    Comparator.comparing(c -> T.isInstance(c)
                            ? T.cast(c): ((Container) c).info, comparator))) < 0){//BAD idk if cast needed
                containers.add(-1-n, new Container(val));
                containers.get(-1-n).items.add(i);
            }else{
                containers.get(n).items.add(Collections.binarySearch(containers.get(n).items,
                        i,Comparator.comparing(getInfo::apply,comparator)),i);
            }
        }
        @SuppressWarnings("unchecked")
        public void notifyDelete(Main.DBItem i){
            int n;
            if ((n = Collections.binarySearch(containers, T.cast(i.getDelimiterInfo(delimID)),//BAD idk if cast needed
                    Comparator.comparing(c -> T.isInstance(c)
                            ? T.cast(c) : ((Container) c).info, comparator))) >= 0) {//BAD idk if cast needed
                if(containers.get(n).items.size()==1 && containers.get(n).items.get(0)==i){//TODO: figure out if comparison fails
                    containers.remove(n);
                }else{
                    int n2;
                    if ((n2 = Collections.binarySearch(containers.get(n).items, i, Comparator.comparing(
                            getInfo::apply, comparator))) >= 0) {
                        containers.get(n).items.remove(n2);
                    }
                }
            }
        }
        public void notifyChange(Main.DBItem _old, Main.DBItem _new){notifyDelete(_old);notifyInsert(_new);}
    }
    



    DynamicNamedDB DB;
    //boolean invert=false;
    //int size=0; help me

    int row=3;
    //actual mental pain

    //public void resetListReference(ArrayList<Object> l){ list=l;}
    //public void setInvert(boolean n){invert=n;}
    //public boolean getInvert(){return invert;}

    public NameDBAdapter(Context context, ArrayList<Object> l){
        super(context,l);
        this.context=context;
        this.list=l;
        //setHasStableIds(true);
    }

    public class NameDBView extends DBView {
        //ArrayList<CardView> cards=new ArrayList<>();
        //ArrayList<TextView> txts=new ArrayList<>();
        //LinearLayout linLayout;
        //int active=0;
        public class OnClick implements View.OnClickListener{
            int posglobal;
            int poslocal;
            NameDBView db;
            public OnClick(int pos1,int pos2, NameDBView db){
                this.posglobal=pos1;
                this.poslocal=pos2;
                this.db=db; }
            @Override
            public void onClick(View v){
                this.db.onClick(v,posglobal,poslocal); }
        }
        public NameDBView(@NonNull View itemView) {
            super(itemView);
            this.linLayout=(LinearLayout) itemView;
            for(int n=0;n<row;n++){
                cards.add((CardView)this.linLayout.getChildAt(n));
                txts.add((TextView) cards.get(n).getChildAt(0) );
                cards.get(n).setOnClickListener(new OnClick(n,this));
            }
        }
        public void updateText(int pos){
            this.active=0;
            for(int n=0;n<row;n++){
                int i=pos*row+n;
                if(i<list.size()){
                    txts.get(n).setText(list.get(i).toString());
                    cards.get(n).setVisibility(View.VISIBLE);
                    this.active+=1;
                }else{
                    cards.get(n).setVisibility(View.GONE);
                }
            }
            this.linLayout.setWeightSum(this.active);
        }
        public void onClick(View v,int globalpos,int localpos){
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

    //BAD ::::::::: FIX NONSENSE

    //BAD: shouldnt be used without adapter
    @SuppressWarnings("unchecked") //compiler can skidaddle
    public void notifyInsert(Main.DBItem i){//also note that the code is duplicated on notifyDelete
        Object val;//because making a function for this mess loses val output
        int n;
        if((n= Collections.binarySearch(containers, (val=T.cast(i.getDelimiterInfo(delimID))),//BAD idk if cast needed
                Comparator.comparing(c -> T.isInstance(c)
                        ? T.cast(c): ((Main.DynamicNamedDB.Container) c).info, comparator))) < 0){//BAD idk if cast needed
            containers.add(-1-n, new Main.DynamicNamedDB.Container(val));
            containers.get(-1-n).items.add(i);
        }else{
            containers.get(n).items.add(Collections.binarySearch(containers.get(n).items,
                    i,Comparator.comparing(getInfo::apply,comparator)),i);
        }
    }
    //BAD: shouldnt be used without adapter
    @SuppressWarnings("unchecked")
    public void notifyDelete(Main.DBItem i){
        int n;
        if ((n = Collections.binarySearch(containers, T.cast(i.getDelimiterInfo(delimID)),//BAD idk if cast needed
                Comparator.comparing(c -> T.isInstance(c)
                        ? T.cast(c) : ((Main.DynamicNamedDB.Container) c).info, comparator))) >= 0) {//BAD idk if cast needed
            if(containers.get(n).items.size()==1 && containers.get(n).items.get(0)==i){//TODO: figure out if comparison fails
                containers.remove(n);
            }else{
                int n2;
                if ((n2 = Collections.binarySearch(containers.get(n).items, i, Comparator.comparing(
                        getInfo::apply, comparator))) >= 0) {
                    containers.get(n).items.remove(n2);
                }
            }
        }
    }
    //BAD: shouldnt be used without adapter
    public void notifyChange(Main.DBItem _old, Main.DBItem _new){notifyDelete(_old);notifyInsert(_new);}






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
    public void notifyDBInvert(){ //pointless because invert already does notifyDataChanged()
        int lim=getItemCount()/(separate+1);
        for(int n=(invert?lim:1);n!=(invert?0:lim+1);n+=(invert?-1:1)){
            notifyItemRemoved(invert?n*(separate+1)-1:getItemCount()-n*(separate+1));
        }
        for(int n=(invert?1:lim);n!=(invert?lim+1:0);n+=(invert?1:-1)){
            notifyItemInserted(invert?(list.size()-1)/row+1-n*(separate):n*(separate));
        }
    }
    @Override
    public int getItemViewType(int position) {
        return (((invert?1:0)*(size-1)-(invert?1:-1)*position)%(separate+1))/separate;
    }
    @Override
    public int getItemCount() {
        size= list == null ? 0 : (list.size()-1)/row+1+(list.size()-1)/(row*separate);
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
            return new NameDBView(linLayout);
        }else{
            View view =LayoutInflater.from(context).inflate(R.layout.separator, parent, false);
            return new Separator(view);
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        getItemCount();
        if (holder instanceof NameDBView){
            int adjustedpos=invert?position-size/(separate+1)+(size-1-position)/(separate+1)
                    :position-(position)/(separate+1);
            NameDBView d=(NameDBView)holder;
            d.updateText(adjustedpos);
        }else{
            ((Separator)holder).setText(String.valueOf(invert?((size-position)/(separate+1))*(row*separate)
                    :((position+1)/(separate+1))*(row*separate)));
        }
    }
}
