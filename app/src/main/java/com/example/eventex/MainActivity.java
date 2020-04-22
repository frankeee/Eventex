package com.example.eventex;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    public RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new homefragment());

        //recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);





    }





    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        switch(item.getItemId()){
            case R.id.navigation_home:
                fragment = new homefragment();
                break;
            case R.id.Profile:
                fragment = new profilefragment();
                break;
            case R.id.navigation_saved:
                fragment = new savedfragment();
                break;
            case R.id.navigation_search:
                fragment = new searchfragment();
                break;
        }
         return loadFragment(fragment);
    }

}
