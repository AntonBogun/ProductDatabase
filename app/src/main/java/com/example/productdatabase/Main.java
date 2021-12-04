package com.example.productdatabase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        DDConsts(int pos,float perday, String desc){
            this.perday=perday;
            this.desc=desc;
        }
    }
    public class DD{
        public ArrayList<Integer>dd;
        //public int bean,berry,fruit,leaf,noleaf,flax,nut,grain,spice;
        public DD(ArrayList<Integer>list){
            dd=list;
//            this.bean=bean;this.berry=berry;this.fruit=fruit;this.leaf=leaf;this.noleaf=noleaf;
//            this.flax=flax;this.nut=nut;this.grain=grain;this.spice=spice;
        }
        public DD(){dd=new ArrayList<>(Collections.nCopies(9,0));}
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
    public class Product extends DBItem{
        public long id;
        public String name;
        public String description;
        public DD DD;
        public float kcal; //per 100 gram
        public float gram_per_num;
        public String format(@Nullable String s){
            return name;//TODO: insanity
        }
    }


    public abstract class NamedDB<T,I extends DBItem>{
        int rows=1;
        String format=null;
        ArrayList<Container> containers=new ArrayList<>();
        Comparator<Container> comparator;
        public abstract class Container{
            T info;
            ArrayList<I> items;
            String label;
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
        }
        public NamedDB(int rows, Comparator<Container> comp){
            this.rows=rows;
            this.comparator=comp;
        }
    }
    public class IntDB<T extends DBItem> extends NamedDB<Integer,T>{
        public class IntContainer extends Container{
            public String _infoToString(){
                return String.valueOf(info);
            }
            public IntContainer(int i, ArrayList<T> items){
                super(i,items);
            }
            public IntContainer(int i, ArrayList<T> items, boolean genLabel){
             super(i,items,genLabel);
            }

        }
        public IntDB(int rows){
            super(rows,new Comparator<Container>(){
                public int compare(Container a, Container b){
                    return a.info-b.info;
                }
            });
        }
    }
}
