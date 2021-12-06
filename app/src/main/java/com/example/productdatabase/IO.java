package com.example.productdatabase;

//import android.os.Build;
import android.os.Environment;
import android.util.Log;


//import java.io.BufferedReader;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;

public class IO {


    public abstract class ClassListLineReader<T> implements Iterable<T>{
        protected RandomAccessFile reader;
        protected abstract boolean isEnd(@Nullable String s);
        protected abstract T parse(@Nullable String s);
        public ClassListLineReader(String file){
            try {
                reader = new RandomAccessFile(file, "r");
            }catch(Exception e){
                Log.e(R.class.getName(),"READER START FAILED::BRUH");
                e.printStackTrace();
            }
        }//i hate java specifically
        public ClassListLineReader(String file,long offset){
            try {
                reader = new RandomAccessFile(file, "r");
                reader.seek(offset);
            }catch(Exception e){
                Log.e(R.class.getName(),"READER START FAILED WITH OFFSET::HMMMM");
                e.printStackTrace();
            }
        }
        public long getReaderPointer(){
            try {
                return reader.getFilePointer();
            }catch(Exception e) {
                Log.e(R.class.getName(), "READER POINTER GET FAILED::huh????");
                e.printStackTrace();
                return -1;
            }
        }
        public void close(){
            try {
                reader.close();
                return;
            }catch(Exception e) {
                Log.e(R.class.getName(), "READER CLOSE FAILED::wtf xdddddd");
                e.printStackTrace();
                return;
            }
        }
        protected boolean checked=false;
        protected String next="";
        @Nullable
        protected String readLine(){
            try {
                    next=reader.readLine();
                    if (next==null){ return null; }
                    next = new String(next.getBytes("ISO-8859-1"),"UTF-8");
                    return next;
            }catch(Exception e){
                Log.e(R.class.getName(),"READER LINE FAILED::DEATH");
                e.printStackTrace();
                return null;
            }
        }
        @Override
        public Iterator<T> iterator(){
            Iterator<T> iter = new Iterator<T>() {
                private int index = 0;
                @Override
                public boolean hasNext(){
                    if(!checked){
                        checked=true;
                        return (readLine()!=null)&&(!isEnd(next));
                    }
                    return !isEnd(next);
                }
                @Override
                public T next() {
                    if (checked){
                        checked=false;
                        return parse(next);
                    }
                    return parse(readLine());
                }
//                @Override
//                public void remove() {
//                    throw new UnsupportedOperationException();
//                } //i don't know what this does so it's commented out xd
            };
            return iter;
        }
    }
    public static void mkdir(String name){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS+name);
        directory.mkdirs();
    }
    public static void store(String filename,String text){
        store("",filename,text,false);
    }
    public static void store(String dir,String filename,String text){
        store(dir,filename,text,false);
    }
    public static void store(String dir,String filename,String text,boolean append){
        dir=dir!=""? "/"+dir:dir;
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS+dir);
        File file = new File(directory, filename);
        Log.e(R.class.getName(),"dir"+directory);
        FileOutputStream outputStream = null;
        try {
            //Log.e(R.class.getName(),"ASDASDASDASD="+directory.mkdirs());
            directory.mkdirs();
            if(!file.exists()) {
                file.createNewFile();
            }

            outputStream = new FileOutputStream(file, append);

            outputStream.write(text.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String retrieveWholeFile(String filename){
        return retrieveWholeFile("",filename);
    }
    public static String retrieveWholeFile(String dir,String filename) {
        dir=dir!=""? "/"+dir:dir;
        String data = "";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS+dir), filename);
        FileInputStream inputStream = null;
        //StringBuilder text = new StringBuilder();
        try {
//            if(!file.exists()) {
//                file.createNewFile();
//            }

//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                text.append(line);
//                text.append('\n');
//            }
//            br.close();

            inputStream = new FileInputStream(file);
            data = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            inputStream.close();
        } catch (Exception e) { e.printStackTrace(); }
        //System.out.println(data);
        return data;//data.substring(0,data.length()-(data.length()>2 ? 2 : 0));
    }

}
