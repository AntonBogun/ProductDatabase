package com.example.productdatabase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

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

    public enum DDConsts{
        BEAN(3,"130g cooked beans, 60g hummus"),
        BERRY(1,"60g fresh or frozen, 40g dried"),
        FRUIT(3, "apple, 40g dried fruit"),
        LEAF(3,"60g of anything green, 6 grams horseradish"),
        NOLEAF(2,"50g veg no leaf"),
        FLAX(1,"6 gram ground flaxseed"),
        NUT(1,"30g nuts, 12g nut butter(???)"),
        GRAIN(3,"100g oatmeal, 1 bread slice"),
        SPICE(1,"2gram turmenic");//the spice must flow
        final float day;
        final String desc;
        DDConsts(float day, String desc){
            this.day=day;
            this.desc=desc;
        }
    }
    public class DD{

    }
    public class Product extends DBItem{
        public String name="";
        public String description="";
//        public String name="";
//        public String name="";
//        public String name="";
        public String format(@Nullable String s){

        }

    }

}
