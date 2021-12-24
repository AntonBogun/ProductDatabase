package com.example.productdatabase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.*;
import java.util.*;
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

    public static int ceil(int a,int b){
        return a/b+(a%b!=0&&(a>0)==(b>0)?1:0);
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
        public DD(String s){//1ධ3ධ5ධ6ධ7ධ8ධ9ධ1ධ2ධ3ධ4ධ5ධ6ධ7ධධධv🧂🧂🧂
            dd= Arrays.stream(s.replaceAll("[|]", "").split("ධ")).
                    map(Float::valueOf).collect(Collectors.toCollection(ArrayList::new));//cursed
            //dd=new ArrayList<>(Arrays.stream(s.replaceAll("[|]","").split("ධ")).map(Float::valueOf).collect(Collectors.toList()));

            // dd=new ArrayList<Float>(Arrays.asList(Arrays.stream(s.replaceAll("[|]","").split("ධ")).map(Float::valueOf).toArray(Float[]::new)));
        }
        //bruh the beans are unavailable🫘🫐🍎🥬🥕🌾🌰🍞🧂
        public String toString(){
            return ((dd.get(0)==0?"":"\uD83C\uDF31"+dd.get(0)+" ")+//bruh for now use 🌱
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
//    public static <T> String format(@Nullable String s, Class<T> _class,T _obj,String _deflt,String _pattern,
//                                Function<String,String> _afterReplace){
//        if(s==null){s="%name%";}
//        try {
//            Pattern p=Pattern.compile("%(-?\\d+(?:\\.\\d+)?)?("+_pattern+")?([/*])(-?\\d+(?:\\.\\d+)?)?("+_pattern+")?%");
//            Matcher m=p.matcher(s);
//            while(m.find()){
//                //number left/right
//                float nleft=m.group(1)==null?1:Float.parseFloat(m.group(1));
//                float nright=m.group(4)==null?1:Float.parseFloat(m.group(4));
//                //property left/right
//                float pleft=m.group(2)==null?1:_class.getDeclaredField(m.group(2)).getFloat(_obj);
//                float pright=m.group(5)==null?1:_class.getDeclaredField(m.group(5)).getFloat(_obj);
//                if(nleft==-1 ||nright==-1||pleft==-1||pright==-1){
//                    s=m.replaceFirst("-1"); //-1 propagates
//                }
//                s=m.replaceFirst(String.format(Locale.US,"%.2f",
//                        nleft*pleft*(m.group(3)=="/"?1/nright/pright:nright*pright) ));
//                m=p.matcher(s);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//            Log.e(R.class.getName(),"product:format got inexplicably bruhed");
//            return s;
//        }
//
//        return _afterReplace.apply(s);
//    }

    public static abstract class DBItem{
        public abstract String format(@Nullable String s);
        //protected enum delimiters{};
        public enum ID{};
        public abstract Object getDelimiterInfo(int id);
        public abstract int getDelimId(String s);

        //no two items stored in same list should have same id, given both share same value field
        public abstract long getId();
        public abstract String save();
        //String s=StringUtils.replace();
    }
    public static class Purchase extends DBItem{
        public long id;
        public long productId;
        protected Product _product;
        @Override
        public long getId(){return id;}
        public enum ID{
            PIECES(0),DISCOUNT(1),DATE(2),EXPIRY(3),SCORE(4);
            public final int value;
            ID(int i){
                value=i;
            }
        }
        @Override
        public int getDelimId(String s){
            return ID.valueOf(s.toUpperCase()).value;
        }
        @Override
        public Object getDelimiterInfo(int id){
            switch (id){
                case 0: //i hate java enum
                    return (pieces==-1?-1:(float)Math.floor(pieces));//bruh imagine sorting by pieces
                case 1:
                    return (discount==-1?-1:(float)(Math.floor(discount*10)/10));//bruh awful
                case 2:
                    return (DateUtils.truncate(date,Calendar.DATE));
                case 3:
                    return (DateUtils.truncate(expiry,Calendar.DATE));
                case 4:
                    return (score==-1?-1:(float)(Math.floor(score)));
                default:
                    return new Object();//not supposed to happen
            }
        }
        public float pieces;
        public float discount;//ratio of normal price (eg 0.5)

        public float score;//0-10, -1 unset
        public int gone;
        public Date date;//of purchase
        public Date expiry;

        public String note;
        //bruh add ignore null later
        @Override
        public String format(@Nullable String s){
            if(s==null){s="%name%";}
            try {
                Pattern p=Pattern.compile("%(id|name|description|DD|score|lscore|lid|ldate [^%]+|lexpiry .+)%");
                Matcher m=p.matcher(s);
                while(m.find()){
                    String _value;
                    if(m.group(1).charAt(0)=='l'){
                        if(m.group(1).startsWith("ldate")||m.group(1).startsWith("lexpiry")){
                            String[] _s=m.group(1).split(" ",2);
                            Date _d=(Date)Purchase.class.getDeclaredField(_s[0].substring(1)).get(this);
                            try{
                                _value=DBDate.toReadableString(_d,(_s.length==1?null:_s[1]));
                            }catch(Exception e){
                                //e.printStackTrace();
                                Log.e(R.class.getName(),"bad date");
                                _value=DBDate.toReadableString(_d,null);
                            }
                        }else {
                            _value = String.valueOf(Purchase.class.getDeclaredField(m.group(1).substring(1)).get(this));
                        }
                    } else if(m.group(1).equals("score")){
                        _value=String.valueOf(_product.getScore());
                    } else{
                        _value=String.valueOf(Product.class.getDeclaredField(m.group(1)).get(_product));
                    }
                    s=m.replaceFirst(_value);
                    m=p.matcher(s);
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.e(R.class.getName(),"product:property format got inexplicably bruhed");
                return s;
            }
            try {
                Pattern p=Pattern.compile("%(-?\\d+(?:\\.\\d+)?)?(kcal|price|gpp|kcalpprice|lpieces|ldiscount)?" +
                        "([/*])(-?\\d+(?:\\.\\d+)?)?(kcal|price|gpp|kcalpprice|lpieces|ldiscount)?%");
                Matcher m=p.matcher(s);
                while(m.find()){
                    //number left/right
                    float nleft=m.group(1)==null?1:Float.parseFloat(m.group(1));
                    float nright=m.group(4)==null?1:Float.parseFloat(m.group(4));
                    //property left/right
                    float pleft;
                    if(m.group(2)==null){pleft=1;}else{
                        if(m.group(2).charAt(0)=='l'){
                            pleft=Purchase.class.getDeclaredField(m.group(2).substring(1)).getFloat(this);
                        }else {
                            if (!m.group(2).equals("kcalpprice")) {
                                pleft = Product.class.getDeclaredField(m.group(2)).getFloat(_product);
                            } else {
                                pleft = _product.getKcalPPrice();
                            }
                        }
                    }
                    float pright;
                    if(m.group(5)==null){pright=1;}else{
                        if(m.group(5).charAt(0)=='l'){
                            pright=Purchase.class.getDeclaredField(m.group(5).substring(1)).getFloat(this);
                        }else {
                            if (!m.group(5).equals("kcalpprice")) {
                                pright = Product.class.getDeclaredField(m.group(5)).getFloat(_product);
                            } else {
                                pright = _product.getKcalPPrice();
                            }
                        }
                    }
                    if(nleft==-1 ||nright==-1||pleft==-1||pright==-1){
                        s=m.replaceFirst("-1"); //-1 propagates
                    }
                    s=m.replaceFirst(String.format(Locale.US,"%.2f",
                            nleft*pleft*(m.group(3).equals("/") ?1/nright/pright:nright*pright)
                    ));
                    m=p.matcher(s);
                }
                return s;
            }catch(Exception e){
                e.printStackTrace();
                Log.e(R.class.getName(),"product:float format got inexplicably bruhed");
                return s;
            }
        }
        @Override
        public String save(){//                         1   2  3  4  5   6  7  8
            return String.format(Locale.ENGLISH,"%dඞ%dඞ%fඞ%fඞ%sඞ%sඞ%fඞ%s",id,productId,pieces,discount,
                    DBDate.toString(date),DBDate.toString(expiry),score,note);
        }//bruh put locale everywhere later


    }
    public static class Product extends DBItem{
        public long id;
        @Override
        public long getId(){return id;}
        public String name;

        public enum ID{
            KCAL(0),PRICE(1),GPP(2),KCALPPRICE(3);
            public final int value;
            ID(int i){
                value=i;
            }
        }
        //protected enum delimiters{}
        @Override
        public int getDelimId(String s){
            return ID.valueOf(s.toUpperCase()).value;
        }
        @Override
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

        protected float score;
        public ArrayList<Purchase> purchases;
        public float getScore(){return score;}
        public void setScore(float score){this.score=score;}
        @Override
        public String format(@Nullable String s){
            if(s==null){s="%name%";}
            try {
                Pattern p=Pattern.compile("%(id|name|description|DD|score)%");
                Matcher m=p.matcher(s);
                while(m.find()){
                    String _value;
                    if(m.group(1).equals("score")){_value=String.valueOf(getScore());}else{
                        _value=String.valueOf(Product.class.getDeclaredField(m.group(1)).get(this));
                    }
                    s=m.replaceFirst(_value);
                    m=p.matcher(s);
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.e(R.class.getName(),"product:property format got inexplicably bruhed");
                return s;
            }

            try {
                Pattern p=Pattern.compile("%(-?\\d+(?:\\.\\d+)?)?(kcal|price|gpp|kcalpprice)?" +
                        "([/*])(-?\\d+(?:\\.\\d+)?)?(kcal|price|gpp|kcalpprice)?%");
                Matcher m=p.matcher(s);
                while(m.find()){
                    //number left/right
                    float nleft=m.group(1)==null?1:Float.parseFloat(m.group(1));
                    float nright=m.group(4)==null?1:Float.parseFloat(m.group(4));
                    //property left/right
                    float pleft;
                    if(m.group(2)==null){pleft=1;}else{
                    if(!m.group(2).equals("kcalpprice")) {
                        pleft = Product.class.getDeclaredField(m.group(2)).getFloat(this);
                    }else{
                        pleft=getKcalPPrice();
                    }
                    }
                    float pright;
                    if(m.group(5)==null){pright=1;}else{
                        if(!m.group(5).equals("kcalpprice")) {
                            pright = Product.class.getDeclaredField(m.group(5)).getFloat(this);
                        }else{
                            pright=getKcalPPrice();
                        }
                    }
                    if(nleft==-1 ||nright==-1||pleft==-1||pright==-1){
                        s=m.replaceFirst("-1"); //-1 propagates
                    }
                    s=m.replaceFirst(String.format(Locale.US,"%.3f",
                            nleft*pleft*(m.group(3).equals("/") ?1/nright/pright:nright*pright)
                    ));
                    m=p.matcher(s);
                }
                return s;
            }catch(Exception e){
                e.printStackTrace();
                Log.e(R.class.getName(),"product:float format got inexplicably bruhed");
                return s;
            }
            }
//        public String format(@Nullable String s){
//            return Main.format(s,Product.class,this,"%name%","kcal|price|gpp",
//                    (S->S.replace("%id%",String.valueOf(id))
//                            .replace("%name%",name)
//                            .replace("%description%",description)
//                            .replace("%DD%",DD.toString())));
//        }
        @Override
        public String save(){
            return String.format("%dඞ%sඞ%fඞ%fඞ%fඞ%sඞ%s",id,name,kcal,price,gpp,DD.save(),description);
        }
        public Product(@Nullable Long id,@Nullable String name, @Nullable Float kcal,
                       @Nullable Float price,@Nullable Float gpp,@Nullable DD DD,@Nullable String description){
            this.id=(id==null?-1:id);           this.name=(name==null?"":name);
            this.kcal=(kcal==null?-1:kcal);     this.price=(price==null?-1:price);
            this.gpp=(gpp==null?-1:gpp);        this.DD=(DD==null?new DD():DD);
            this.description=(description==null?"":description);
        }
        public Product(String s){
            String[] l=s.split("ඞ");    id=Long.parseLong(l[0]);      name=l[1];
            kcal=Float.parseFloat(l[2]);      price=Float.parseFloat(l[3]);
            gpp=Float.parseFloat(l[4]);       DD=new DD(l[5]);              description=l[6];
        }
        public Product(){}//for ""static"" methods
    }
}

