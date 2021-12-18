
package com.example.productdatabase;

import android.annotation.SuppressLint;
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
public class DynamicNamedDBAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    int row=3; //items PER row

    public Class<?> T;
    public Class<? extends Main.DBItem> I;

    public String format=null;
    protected ArrayList<Container> containers;
    public Comparator<Object> comparator; //<T>
    public Function<Object,String> contInfoToString;//display <T,String>
    public Function<Main.DBItem,Object> getInfo;//adapter <I,T>
    public int delimID;//delimiter ID for DBItem
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
        public int evalSize(){
            return Main.ceil(items.size(),row);//size of container in rows
        }
        public int evalRow(int pos){
            return position+1+pos/row;//row of (pos) in container
        }
        public int evalLast(){
            return position+evalSize();//row of last (pos) in container
        }
        public int searchById(int _pos,long id){
            if(items.get(_pos).getId()==id){
                return _pos;
            }
            Object originInfo=getInfo.apply(items.get(_pos));
            int pos=_pos;
            while(pos+1<items.size()&&comparator.compare(getInfo.apply(items.get(++pos)),originInfo)==0){
                if(items.get(pos).getId()==id){
                    return pos;
                }
            }
            pos=_pos;
            while(pos>0&&comparator.compare(getInfo.apply(items.get(--pos)),originInfo)==0){
                if(items.get(pos).getId()==id){
                    return pos;
                }
            }
            return -1;
        }
        public int getLowest(int _pos){
            int pos=_pos;
            Object originInfo=getInfo.apply(items.get(_pos));
            while(pos+1<items.size()&&comparator.compare(getInfo.apply(items.get(++pos)),originInfo)==0){}
            return (pos==_pos||pos==items.size()-1?pos:--pos);//bruh moment
        }
    }
    public DynamicNamedDBAdapter(Context context,int row, Class<Object> T, Class<? extends Main.DBItem> I, Comparator<Object> comp,
                          Function<Main.DBItem,Object> getInfo, Function<Object,String> contInfoToString,
                          String delim, Main.DBItem dummyitem){
        this.context=context;
        this.row=row;
        this.T=T;
        this.I=I;
        this.comparator=comp;
        this.getInfo=getInfo;
        this.contInfoToString=contInfoToString;
        this.delimID=dummyitem.getDelimId(delim);//because wildcard not allowed for argument type
    }


    //used in fromArrayList
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
    @SuppressLint("NotifyDataSetChanged")
    public void fromArrayList(ArrayList<Main.DBItem> _arr){//_arr remains unchanged
        ArrayList<Main.DBItem> arr=new ArrayList<>(_arr);
        arr.sort(Comparator.comparing(getInfo::apply,comparator));
        Collections.reverse(arr);
        containers=new ArrayList<>();
        for (int i = arr.size()-1; i >-1; i--) {
            addToCont(arr.get(i));
            arr.remove(i);
        }
        notifyDataSetChanged();
    }


    //evaluate positions of containers
    public void positionEval(){positionEval(0);}
    public void positionEval(int from){
        if (containers.size()==0){ return; }
        int pos=(from-1<0||from-1>=containers.size() ? 0 //truly horrifying
                : containers.get(from-1).position+1+containers.get(from-1).evalSize());
        for (int i = from; i < containers.size(); i++) {
            containers.get(i).position=pos;
            pos+=1+containers.get(i).evalSize();
        }
    }

    //find container position of row (pos) using binary search
    public int contPosSearch(int pos){
        int _pos=Collections.binarySearch(containers,pos,
                Comparator.comparingInt(c->c instanceof Integer?(int)c:((Container)c).position));
        return (_pos<0?-2-_pos:_pos);
    }
    //find container position of row (pos) using nearby check bruh apparently not allowed
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

    public void notifyDBInsert(Main.DBItem i){//also note that the code is duplicated on notifyDelete
        Object val;//value of i
        int n;//position of parent container
        if((n= Collections.binarySearch(containers, (val=T.cast(i.getDelimiterInfo(delimID))),//BAD idk if cast needed
                Comparator.comparing(c -> T.isInstance(c)
                        ? T.cast(c): ((Container) c).info, comparator))) < 0){//BAD idk if cast needed

            containers.add(-1-n, new Container(val));
            containers.get(-1-n).items.add(i);
            positionEval(-1-n);
            notifyItemRangeInserted(containers.get(-1-n).position,2);
        }else{
            int n2;//position inside //bruh spawn of hell below lmao
            containers.get(n).items.add(n2=(
                                (n2=Collections.binarySearch(containers.get(n).items, i,
                                Comparator.comparing(getInfo::apply,comparator)))<0
                                ? -1-n2 : containers.get(n).getLowest(n2)+1), i);
            if (n<containers.size()-1){
                positionEval(n+1);
            }
            int _size;
            //if(insert);else(non insert)
            if((_size=containers.get(n).items.size())%row==1){
                if(n2!=_size-1){
                    int _rows;
                    notifyItemRangeChanged((_rows=containers.get(n).evalRow(n2)),
                            containers.get(n).evalLast()-_rows);//BAD needs confirmation
                }
                notifyItemInserted(containers.get(n).evalLast());//BAD needs confirmation
            }
            else{
                int _rows;//bruh the +1 below is fucked up
                notifyItemRangeChanged((_rows=containers.get(n).evalRow(n2)),
                        containers.get(n).evalLast()-_rows+1);//BAD needs confirmation
            }
        }
    }
    public void notifyDBDelete(Main.DBItem i){//BAD if multiple items have same
        int n;
        if ((n = Collections.binarySearch(containers, T.cast(i.getDelimiterInfo(delimID)),//BAD idk if cast needed
                Comparator.comparing(c -> T.isInstance(c)
                        ? T.cast(c) : ((Container) c).info, comparator))) >= 0) {//BAD idk if cast needed
            if(containers.get(n).items.size()==1 && containers.get(n).items.get(0).getId()==i.getId()){//BAD idk if comparison fails
                notifyItemRangeRemoved(containers.get(n).position,2);
                containers.remove(n);
                positionEval(n);
            }else{
                int n2;
                if ((n2 = Collections.binarySearch(containers.get(n).items, i, Comparator.comparing(
                        getInfo::apply, comparator))) >= 0) {
                    n2=containers.get(n).searchById(n2,i.getId());
                    int _row=containers.get(n).evalRow(n2);
                    int _size;
                    if((_size=containers.get(n).items.size())%row!=1){
                        notifyItemRangeChanged(_row, containers.get(n).evalLast()-_row+1);
                    }else{
                        if(n2!=_size-1) {
                            notifyItemRangeChanged(_row, containers.get(n).evalLast()-_row);
                        }
                        notifyItemRemoved(containers.get(n).evalLast());
                    }
                    containers.get(n).items.remove(n2);
                    positionEval(n+1);
                }
            }
        }
    }
    //BAD ideally use separately in code with change handled manually to avoid item duplication
    public void notifyChange(Main.DBItem _old, Main.DBItem _new){notifyDBDelete(_old);notifyDBInsert(_new);}

    //boolean invert=false;
    //int size=0; help me

    //actual mental pain

    //bruh figure out wtf is this
    //public void resetListReference(ArrayList<Object> l){ list=l;}
    //public void setInvert(boolean n){invert=n;}
    //public boolean getInvert(){return invert;}



    public class NameDBView extends RecyclerView.ViewHolder {
        LinearLayout linLayout;//horizontal layout containing cards
        ArrayList<CardView> cards=new ArrayList<>();//clickable cards containing text
        ArrayList<TextView> txts=new ArrayList<>();//per card, get using .format()
        int active=0;
        int contPos;//container position inside containers

        public class OnClick implements View.OnClickListener{//bruh truly horrible
            int inContPos;//position inside container
            NameDBView db;//to be able to call OnClick in db
            public OnClick(int inContPos, NameDBView db){
                this.inContPos=inContPos;
                this.db=db; }
            @Override
            public void onClick(View v){ db.onClick(v,inContPos); }
        }
        public NameDBView(@NonNull View view) {//init linear layout with empty cards
            super(view);
            this.linLayout=(LinearLayout) view;
            for(int n=0;n<row;n++){
                cards.add((CardView)this.linLayout.getChildAt(n));
                txts.add((TextView) cards.get(n).getChildAt(0) );
            }
        }
        public void updateText(int contPos,int inContRow){//fill in cards and add onClick
            this.contPos=contPos;
            active=0;
            int posOffset=inContRow*row;
            for(int n=0;n<row;n++){
                int i=posOffset+n;
                if(i<containers.get(contPos).items.size()){
                    txts.get(n).setText(containers.get(contPos).items.get(i).format(format));
                    cards.get(n).setVisibility(View.VISIBLE);
                    cards.get(n).setOnClickListener(new OnClick(i,this));
                    active+=1;//bruh there is better way to get active but i cant be bothered
                }else{
                    cards.get(n).setVisibility(View.GONE);
                }
            }
            linLayout.setWeightSum(active);
        }
        public void onClick(View v,int inContPos){
            ((SecondActivity)context).onRecyclerClick(v,
                    containers.get(contPos).position+inContPos);
        }
    }
    public class Separator extends RecyclerView.ViewHolder{
        TextView text; //bruh supposed to have click functionality but cant be bothered for now
        public Separator(@NonNull View itemView){
            super(itemView);
            text=itemView.findViewById(R.id.septext);
        }
        public void setText(int contPos){this.text.setText(containers.get(contPos).getLabel());}
    }





    @Override
    public int getItemViewType(int position) {
        myRecyclerView.findViewHolderForAdapterPosition(pos);
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
