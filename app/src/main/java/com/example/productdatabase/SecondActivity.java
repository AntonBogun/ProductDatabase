package com.example.productdatabase;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import android.app.Activity;


import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
//import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.rx3.TedPermission;

public class SecondActivity extends AppCompatActivity{

    public class textwatch implements TextWatcher{
//        public void textwatch(){
//            context=context;
//
//        }
        @Override
        public void beforeTextChanged(CharSequence chrs,int i1,int i2,int i3){
            Log.e(R.class.getName(),"befor??="+chrs+","+i1+","+i2+","+i3);
        }
        @Override
        public void onTextChanged(CharSequence chrs,int i1,int i2,int i3){
            Log.e(R.class.getName(),"tHe="+chrs+","+i1+","+i2+","+i3);
        }
        @Override
        public void afterTextChanged(Editable what){
            Log.e(R.class.getName(),"after???="+what);
        }
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
    String name;
    Context context;
    ArrayList<Object> l=new ArrayList<>();
    DBAdapter adapter=new DBAdapter(this,l);
    RecyclerView recyclerView;
    int scroll=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        name=getApplicationName(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        context=this;
        //Log.e(R.class.getName(), "START" );

//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
////                try { restore(); }catch (Exception e){ e.printStackTrace(); }
//                Log.e(R.class.getName(), "Lmao got perms");
//                Log.e(R.class.getName(), "perm1=" + ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE));
//                Log.e(R.class.getName(), "perm2=" + ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
//                Log.e(R.class.getName(), "perm3=" + ContextCompat.checkSelfPermission(context, Manifest.permission.MANAGE_EXTERNAL_STORAGE));
//            }
//            @Override
//            public void onPermissionDenied(List<String> deniedPermissions) {
//                Toast.makeText(SecondActivity.this, "Permission Denied\n" + deniedPermissions, Toast.LENGTH_SHORT).show();
//            }
//        };


//        TedPermission.create().setPermissionListener(permissionlistener).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE).check();

        //Log.e(R.class.getName(), "show manage="+shouldShowRequestPermissionRationale(Manifest.permission.MANAGE_EXTERNAL_STORAGE));
//        ActivityResultLauncher<String> requestPermissionLauncher =
//                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                    if (isGranted) {
//                        Log.e(R.class.getName(),"Granted???");
//                    } else {
//                        Log.e(R.class.getName(),"Denied??");
//                    }
//
//                });
        //requestPermissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        //Log.e(R.class.getName(), "perm1="+ ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE));
        //Log.e(R.class.getName(), "perm2=" + ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        //Log.e(R.class.getName(), "perm3=" + ContextCompat.checkSelfPermission(context, Manifest.permission.MANAGE_EXTERNAL_STORAGE));




        l=((Main) getApplication()).global_l;
        adapter.resetListReference(l);

        l.add(((Main) getApplication()).global_i);
        adapter.notifyItemInserted(l.size()-1);

        Intent change_to_main = new Intent(SecondActivity.this, MainActivity.class);

        //Log.e(R.class.getName(),"name="+name);
        IO.store(name,"nowayyy.txt","oh god");
        String data=IO.retrieve(name,"nowayyy.txt");
        if (!data.equals("")){
            Log.e(R.class.getName(),data);
        }
        
        EditText txt=findViewById(R.id.textView2);
        textwatch txtwatch=new textwatch();
        //txt.addTextChangedListener(txtwatch);
        txt.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            Log.e(R.class.getName(),"done pressed:"+txt.getText());
                        }
                        return false;
                    }
                });

        Button button=findViewById(R.id.button2);
        button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ((Main) getApplication()).global_i+=1;
                Log.e(R.class.getName(),"global_i="+((Main) getApplication()).global_i);
                SecondActivity.this.startActivity(change_to_main);
            }

        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        Button recyclebutton=findViewById(R.id.recyclebutton);
        Random rng = new Random();
        recyclebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //button.setText("i hate this");
                Log.e(R.class.getName(),"BUTTON CLICKED");
                l.add(String.valueOf(rng.nextInt(1000000)));
                //Log.d(R.class.getName(),"l="+l.toString());
                //adapter.notifyItemInserted(l.size()-1);
                //adapter.notifyItemAppend();
                adapter.notifyDataSetChanged();
                //Log.d(R.class.getName(),"adapter="+adapter.list.toString());
                //byte [] e=new byte[8];
                //recyclerView.smoothScrollToPosition(1);
                recyclerView.scrollToPosition(3);
                Log.e(R.class.getName(),l.toString());
                //CardView c=((BSAdapter.BSView)recyclerView.findViewHolderForAdapterPosition(0)).card;
                //c.setElevation(c.getCardElevation()+10000000);
                //Log.e(R.class.getName(),"elev="+c.getCardElevation());
                View fill=findViewById(R.id.fill);
                //fill.setVisibility((fill.getVisibility()+4)%8);
                Log.e(R.class.getName(),"vis="+fill.getVisibility());
            }

        });
    }

    public void onRecyclerClick(View v,int id){
        Log.e(R.class.getName(),"Bruh clicked="+id);
        //Log.d(R.class.getName(),v.toString());
        if (v instanceof TextView){
            TextView txt=(TextView)v;
            //Log.e(R.class.getName(), txt.getText()+", id:"+id );//i literally can not
            txt.setText("LMAO");
            scroll=recyclerView.computeVerticalScrollOffset();
            Log.e(R.class.getName(),"scrol="+scroll);
        }
    }
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        Log.e(R.class.getName(),"CLOSED");
//    }
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        Log.e(R.class.getName(),"SAVED");
//        //savedInstanceState.putIntegerArrayList("RecycleView",l);
//    }
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.e(R.class.getName(), "LOAD" );
//        super.onRestoreInstanceState(savedInstanceState);
//        //l=savedInstanceState.getIntegerArrayList("RecycleView");
//
//        //l=savedInstanceState.getIntegerArrayList("RecycleView");
//        //adapter.resetListReference(l);
//    }

}
