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

    public enum DDConsts{
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
        DDConsts(int pos,float perday, String desc){
            this.pos=pos;
            this.perday=perday;
            this.desc=desc;
        }
    }
    public class DD{
        public ArrayList<Float>dd;
        //public int bean,berry,fruit,leaf,noleaf,flax,nut,grain,spice;
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

    public abstract class DBItem{
        public abstract String format(@Nullable String s);
        public String getLabel(){
            return getLabel(null);
        }
        public String getLabel(@Nullable String s){
            if (s!=null){
                label=format(s);
            }
            if (label==null){
                label=format(null);
            }
            return label;
        }
        public String label = "";
        //String s=StringUtils.replace();
    }
//    public class Purchase extends DBItem{
//
//    }
    public class Product extends DBItem{
        public long id;
        public String name;

        public float kcal; //per 100 gram
        public float price;//per 1 piece
        public float gpp;//grams per 1 piece

        public DD DD;
        public String description;
        public ArrayList<Purchase> purchases=new ArrayList<>();
        public String format(@NonNull String s){
            try {
                Pattern p=Pattern.compile("%(-?\\d+(?:.\\d+)?)?(kcal|price|gpp)?([/*])(-?\\d+(?:.\\d+)?)?(kcal|price|gpp)?%");
                Matcher m=p.matcher(s);
                while(m.find()){
                    float right=(m.group(4)==null?1:Float.parseFloat(m.group(4)))*
                            (m.group(5)==null?1:Product.class.getDeclaredField(m.group(5)).getFloat(this));

                    s.replaceFirst(m.group(0),String.valueOf(
                            (m.group(1)==null?1:Float.parseFloat(m.group(1)))*
                                    (m.group(2)==null?1:Product.class.getDeclaredField(m.group(2)).getFloat(this))*
                                    (m.group(3)=="/"?1/right:right)
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
    }

    public interface DBItemAdapter<T,I extends DBItem>{
        public T getInfo(I item);
    }
    public abstract class NamedDB<T,I extends DBItem>{
        int rows=1; //items PER row
        String format=null;
        ArrayList<Container> containers=new ArrayList<>();
        Comparator<T> comparator;
        DBItemAdapter<T,I> adapter;
        public abstract class Container{
            T info;
            ArrayList<I> items;
            String label;
            int position;
            public Container(T info, ArrayList<I>items) {
                this.info=info;
                this.items=items;
            }
            public Container(T info, ArrayList<I>items, boolean genLabel) {
                this.info=info;
                this.items=items;
                if(genLabel){
                    getLabel();
                }
            }
            public abstract String _infoToString();
            public String getLabel(){
                return getLabel(false);
            }
            public String getLabel(boolean b){
                if (label==null ||b){
                    label=_infoToString();
                }
                return label;
            }
            public int evalSize(int rows){
                if (items.size()==0){return 0;}
                return ((items.size()-1)/rows)+1;
            }
        }
        public NamedDB(int rows, Comparator<T> comp,DBItemAdapter<T,I> adapter){
            this.rows=rows;
            this.comparator=comp;
            this.adapter=adapter;
        }
        public void fromArrayList(ArrayList<I> _arr){
            ArrayList<I> arr=new ArrayList<I>(_arr);
            Collections.sort(arr,Comparator.comparing(c->adapter.getInfo(c),comparator));
            Collections.reverse(arr);

        }
        public void notifyInsert(){}
        public void notifyDelete(){}//TODO????t

        public void positionEval(){
            if (containers.size()==0){ return; }
            int pos=0;
            for (int i = 0; i < containers.size(); i++) {
                containers.get(i).position=pos;
                pos+=1+containers.get(i).evalSize(rows);
            }
        }

        public void sort(){
            containers.sort(Comparator.comparing(c->c.info,comparator));
        }
        public void sortInsert(Container c){
            int pos=Collections.binarySearch(containers,c,Comparator.comparing(_c->_c.info,comparator));
            containers.add(pos<0?-1-pos:pos,c);
        }

        public int posSearch(int pos){
            int _pos=Collections.binarySearch(containers,pos+1,
                    Comparator.comparingInt(c->c instanceof Integer?(int)c:((Container)c).position));
//                    new Comparator<Object>(){public int compare(Object a,Object b){ return ((int)a)-((int)b); }});
            return (_pos<0?-1-_pos:_pos)-1;
        }
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

    public class IntDB<T extends DBItem> extends NamedDB<Integer,T>{
        public class IntContainer extends Container{
            @Override
            public String _infoToString(){
                return String.valueOf(info);
            }
            public IntContainer(int i, ArrayList<T> items){ super(i,items); }
            public IntContainer(int i, ArrayList<T> items, boolean genLabel){ super(i,items,genLabel); }
        }
        public IntDB(int rows,DBItemAdapter<Integer,T> adapter){
            super(rows, Comparator.comparingInt(c->c),adapter);
        }
        //ArrayList<Container>.add(IntContainer);

        //in the actual code, ((IntContainer)container).somemethodonlypresentinIntContainer()
    }

    public class FloatDB<T extends DBItem> extends NamedDB<Float,T>{
        public class FloatContainer extends Container{
            public String _infoToString(){
                return String.valueOf(info);
            }
            public FloatContainer(float i, ArrayList<T> items){
                super(i,items);
            }
            public FloatContainer(float i, ArrayList<T> items, boolean genLabel){
                super(i,items,genLabel);
            }
        }

        public FloatDB(int rows,DBItemAdapter<Float,T> adapter){
            super(rows,Comparator.comparingDouble(c-> c),adapter);
//                    new Comparator<Container>(){
//                public int compare(Container a, Container b){
//                    return (int)Math.signum(a.info-b.info);
//                }
//            }
        }
    }

}

