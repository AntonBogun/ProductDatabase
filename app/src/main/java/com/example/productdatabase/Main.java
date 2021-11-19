package com.example.productdatabase;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.ArrayList;

public class Main extends android.app.Application{
    public int global_i=0;
    public ArrayList<Object> global_l=new ArrayList<>();
    //public String name=getApplicationName(this);
    //http://www.developerphil.com/dont-store-data-in-the-application-object/
}
