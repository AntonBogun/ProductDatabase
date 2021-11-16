package com.example.productdatabase;

//import android.os.Build;
import android.os.Environment;
import android.util.Log;


//import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class IO {
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
    public static String retrieve(String filename){
        return retrieve("",filename);
    }
    public static String retrieve(String dir,String filename) {
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
