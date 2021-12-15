package com.example.productdatabase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Main extends android.app.Application{
    public int global_i=0;
    public ArrayList<Object> global_l=new ArrayList<>();
    //public String name=getApplicationName(this);
    //http://www.developerphil.com/dont-store-data-in-the-application-object/
    public static float pxToDpf(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static int pxToDpi(float px, Context context){
        return Math.round(pxToDpf(px,context));
    }
    public static float dpToPxf(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static int dpToPxi(float dp,Context context){
        return Math.round(dpToPxf(dp,context));
    }


    public static class DD{
        public ArrayList<Float>dd;
        //public int bean,berry,fruit,leaf,noleaf,flax,nut,grain,spice;
        public enum info{
            BEAN(0,3,"130g cooked beans, 60g hummus"),
            BERRY(1,1,"60g fresh or frozen, 40g dried"),
            FRUIT(2,3, "apple, 40g dried fruit"),
            LEAF(3,3,"60g of anything green, 6 grams horseradish"),
            NOLEAF(4,2,"50g veg no leaf"),
            FLAX(5,1,"6 gram ground flaxseed"),
            NUT(6,1,"30g nuts, 12g nut butter(???)"),
            GRAIN(7,3,"100g oatmeal, 1 bread slice"),
            SPICE(8,1,"2gram turmenic");//the spice must flow
            final float perday;
            final String desc;
            final int pos;
            info(int pos,float perday, String desc){
                this.pos=pos;
                this.perday=perday;
                this.desc=desc;
            }
        }
        public DD(ArrayList<Float>list){
            dd=list;
//            this.bean=bean;this.berry=berry;this.fruit=fruit;this.leaf=leaf;this.noleaf=noleaf;
//            this.flax=flax;this.nut=nut;this.grain=grain;this.spice=spice;
        }
        public DD(){dd=new ArrayList<>(Collections.nCopies(9, (float) 0));}
        public DD(String s){//ධධධධධධධධධධධධධධධධv🧂🧂🧂
            dd=new ArrayList<>(Arrays.stream(s.replaceAll("[|]","").split("ධ")).map(Float::valueOf).collect(Collectors.toList()));
           // dd=new ArrayList<Float>(Arrays.asList(Arrays.stream(s.replaceAll("[|]","").split("ධ")).map(Float::valueOf).toArray(Float[]::new)));
        }
        //🫘🫐🍎🥬🥕🌾🌰🍞🧂
        public String toString(){
            return ((dd.get(0)==0?"":"\uD83E\uDED8"+dd.get(0)+" ")+
                    (dd.get(1)==0?"":"\uD83E\uDED0"+dd.get(1)+" ")+
                    (dd.get(2)==0?"":"\uD83C\uDF4E"+dd.get(2)+" ")+
                    (dd.get(3)==0?"":"\uD83E\uDD6C"+dd.get(3)+" ")+
                    (dd.get(4)==0?"":"\uD83E\uDD55"+dd.get(4)+" ")+
                    (dd.get(5)==0?"":"\uD83C\uDF3E"+dd.get(5)+" ")+
                    (dd.get(6)==0?"":"\uD83C\uDF30"+dd.get(6)+" ")+
                    (dd.get(7)==0?"":"\uD83C\uDF5E"+dd.get(7)+" ")+
                    (dd.get(8)==0?"":"\uD83E\uDDC2"+dd.get(8))).trim();
        }
        public String save(){
            return dd.toString().replace(",","ධ");
        }
    }

    public static abstract class DBItem{
        public abstract String format(@Nullable String s);
        //protected enum delimiters{};
        public enum ID{};
        public abstract Object getDelimiterInfo(int id);
        public abstract int getId(String s);
        //String s=StringUtils.replace();
    }
    public class Purchase extends DBItem{

    }
    public static class Product extends DBItem{
        public long id;
        public String name;

        public enum ID{
            KCAL(0),PRICE(1),GPP(2),KCALPPRICE(3);
            public final int value;
            ID(int i){
                value=i;
            }
        }
        //protected enum delimiters{}
        public int getId(String s){
            return ID.valueOf(s).value;
        }
        public Object getDelimiterInfo(int id){
            switch (id){
                case 0: //i hate java enum
                    return (kcal==-1?-1:(float)(Math.floor(kcal/50)*50));
                case 1:
                    return (price==-1?-1:(float)(Math.floor(price)));
                case 2:
                    return (gpp==-1?-1:(float)(Math.floor(gpp)));
                case 3:
                    float k=getKcalPPrice();
                    if (k==-1){
                        return -1;
                    }else{
                        return (float)(Math.floor(k/30)*30);
                    }
                default:
                    return new Object();//not supposed to happen
            }
        }

        public float kcal; //per 100 gram
        public float price;//per 1 piece
        public float gpp;//grams per 1 piece
        public float getKcalPPrice(){
            return (kcal==-1||price==-1||gpp==-1?-1:kcal*gpp/price/100);
        }
        public DD DD;
        public String description;
        public ArrayList<Purchase> purchases=new ArrayList<>();
        public String format(@NonNull String s){
            try {
                Pattern p=Pattern.compile("%(-?\\d+(?:.\\d+)?)?(kcal|price|gpp)?([/*])(-?\\d+(?:.\\d+)?)?(kcal|price|gpp)?%");
                Matcher m=p.matcher(s);
                while(m.find()){
                    //number left/right
                    float nleft=m.group(1)==null?1:Float.parseFloat(m.group(1));
                    float nright=m.group(4)==null?1:Float.parseFloat(m.group(4));
                    //property left/right
                    float pleft=m.group(2)==null?1:Product.class.getDeclaredField(m.group(2)).getFloat(this);
                    float pright=m.group(5)==null?1:Product.class.getDeclaredField(m.group(5)).getFloat(this);
                    if(nleft==-1 ||nright==-1||pleft==-1||pright==-1){
                        s.replaceFirst(m.group(0),"-1"); //-1 propagates
                    }
                    s.replaceFirst(m.group(0),String.valueOf(
                            nleft*pleft*(m.group(3)=="/"?1/nright/pright:nright*pright)
                    ));
                    m=p.matcher(s);
                }
                return s;
            }catch(Exception e){
                e.printStackTrace();
                Log.e(R.class.getName(),"product:format got inexplicably bruhed");
                return s;
            }
        }
        public String save(){
            return String.format("%dඞ%sඞ%fඞ%fඞ%fඞ%sඞ%s",id,name,kcal,price,gpp,DD.save(),description);
        }
        public Product(@Nullable Long id,@Nullable String name, @Nullable Float kcal,
                       @Nullable Float price,@Nullable Float gpp,@Nullable DD DD,@Nullable String description){
            this.id=(id==null?-1:id);
            this.name=(name==null?"":name);
            this.kcal=(kcal==null?-1:kcal);
            this.price=(price==null?-1:price);
            this.gpp=(gpp==null?-1:gpp);
            this.DD=(DD==null?new DD():DD);
            this.description=(description==null?"":description);
        }
        public Product(String s){
            String[] l=s.split("ඞ");
            id=Long.parseLong(l[0]);
            name=l[1];
            kcal=Float.parseFloat(l[2]);
            price=Float.parseFloat(l[3]);
            gpp=Float.parseFloat(l[4]);
            DD=new DD(l[5]);
            description=l[6];
        }
        public Product(){}
    }

    public interface DBItemAdapter<T,I extends DBItem>{
        public T getInfo(I item);
    }
    public class NamedDB<T,I extends DBItem>{
        int rows=1; //items PER row
        public String format=null;
        protected ArrayList<Container> containers=new ArrayList<>();
        public final Comparator<T> comparator;
        public final Class<T> t;
        public final Function<T,String> contInfoToString;//display
        public final Function<I,T> getInfo;//adapter
        public final int delimID;
        public final Function<> delimiter;
        //TODO: confirm non-sus
        public class Container{
            T info;
            ArrayList<I> items;
            int position;
            public Container(T info, ArrayList<I>items) {
                this.info=info;
                this.items=items;
            }
            public Container(T info){
                this.info=info;
                this.items=new ArrayList<I>();
            }
            public String getLabel(){
                return contInfoToString.apply(info);
            }
            public int evalSize(int rows){
                if (items.size()==0){return 0;}
                return ((items.size()-1)/rows)+1;
            }
        }
        //TODO: confirm non-sus
        public NamedDB(int rows,Class<T>t, Comparator<T> comp, Function<I,T> getInfo,
                       Function<T,String> contInfoToString,String delim,I dummyitem){
            this.rows=rows;
            this.t=t;
            this.comparator=comp;
            this.getInfo=getInfo;
            this.contInfoToString=contInfoToString;
            this.delimID=dummyitem.getId(delim);//I hate java's static/abstract exclusivity
        }
        //TODO: confirm non-sus
        public void fromArrayList(ArrayList<I> _arr){
            ArrayList<I> arr=new ArrayList<>(_arr);
            Collections.sort(arr,Comparator.comparing(getInfo::apply,comparator));
            Collections.reverse(arr);
            for (int i = arr.size()-1; i >-1; i--) {
                addToCont(arr.get(i));
                arr.remove(i);
            }
        }
        @SuppressWarnings("unchecked") //compiler can skidaddle
        public void notifyInsert(I i){
            T val;
            int n;
            if((n=Collections.binarySearch(containers, (val=(T)i.getDelimiterInfo(delimID)),
                    Comparator.comparing(c->t.isInstance(c)?(T)c:((Container)c).info,comparator)))<0){
                containers.add(-1-n,new Container(val));
                containers.get(-1-n).items.add(i);
            }else{
                containers.get(n).items.add(Collections.binarySearch(containers.get(n).items,
                        i,Comparator.comparing(getInfo::apply,comparator)),i);
            }
        }
        @SuppressWarnings("unchecked")
        public void notifyDelete(I i){
            int n;
            if ((n = Collections.binarySearch(containers, (T) i.getDelimiterInfo(delimID),
                    Comparator.comparing(c -> t.isInstance(c)
                    ?(T) c : ((Container) c).info, comparator))) >= 0) {
                if(containers.get(n).items.size()==1 && containers.get(n).items.get(0)==i){//TODO: figure out if yikes
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
        //TODO: lmao
        public void notifyChange(I _old, I _new){notifyDelete(_old);notifyInsert(_new);}

        //TODO: idk if correct
        public void positionEval(){
            if (containers.size()==0){ return; }
            int pos=0;
            for (int i = 0; i < containers.size(); i++) {
                containers.get(i).position=pos;
                pos+=1+containers.get(i).evalSize(rows);
            }
        }
        //TODO: Add full sort
        public void sort(){
            containers.sort(Comparator.comparing(c->c.info,comparator));
        }
        @SuppressWarnings("unchecked")
        public void addToCont(I i){
            T val;
            int n;
            if((n=Collections.binarySearch(containers, (val=(T)i.getDelimiterInfo(delimID)),
                    Comparator.comparing(c->t.isInstance(c)?(T)c:((Container)c).info,comparator)))<0){
                containers.add(-1-n,new Container(val));
                containers.get(-1-n).items.add(i);
            }else{
                containers.get(n).items.add(i);
            }
        }//correct
        //TODO: idk if correct
        public void contAdd(Container c){
            int pos=Collections.binarySearch(containers,c,Comparator.comparing(_c->_c.info,comparator));
            containers.add(pos<0?-2-pos:pos,c);
        }
        //TODO: idk if correct
        public int contPosSearch(int pos){
            int _pos=Collections.binarySearch(containers,pos,
                    Comparator.comparingInt(c->c instanceof Integer?(int)c:((Container)c).position));
//                    new Comparator<Object>(){public int compare(Object a,Object b){ return ((int)a)-((int)b); }});
            return (_pos<0?-2-_pos:_pos);
        }
        //TODO: idk if correct
        public int nearPosSearch(int pos,int offpos){//offpos=container position known
            int containpos=containers.get(offpos).position;//pos=current wanted position
            if (containpos>pos){//offpos-1=new container wanted position
                return offpos-1;
            }
            if(containpos<pos && offpos+1<containers.size() && containers.get(offpos+1).position<=pos){
                return offpos+1;
            }
            return offpos;
        }
    }
}

