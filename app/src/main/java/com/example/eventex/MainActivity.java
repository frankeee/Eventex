package com.example.eventex;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    public RecyclerView.LayoutManager layoutManager;
    Fragment homefragment;
    Fragment searchfragment;
    Fragment savedfragment;
    Fragment profilefragment;
    int anterior = 0;
    int actual = 1;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        Cursor maincu = myDb.getTodoDatos();
        if(maincu== null || !maincu.moveToFirst()){
            Intent intento = new Intent(this, LoginActivity.class);
            startActivity(intento);
        }
        if(maincu!=null) {
            maincu.close();
        }
        homefragment = new homefragment();
        profilefragment = new profilefragment();
        savedfragment = new savedfragment();
        searchfragment = new searchfragment();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA");
        /*
        if(message!=null){
            anterior = actual;
            actual = 2;
            loadFragment(profilefragment);
        }*/

        anterior = actual;
        actual = 1;
        loadFragment(homefragment);

        //recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);





    }





    private boolean loadFragment(Fragment fragment){
        if(anterior == 1){
            getSupportFragmentManager().beginTransaction().hide(homefragment).commit();
        }
        else if(anterior ==2){
            getSupportFragmentManager().beginTransaction().hide(profilefragment).commit();
        }else if(anterior ==3){
            getSupportFragmentManager().beginTransaction().hide(savedfragment).commit();
        }else if(anterior ==4){
            getSupportFragmentManager().beginTransaction().hide(searchfragment).commit();
        }

        if(fragment != null) {
            if (!fragment.isAdded()) {

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                return true;
            }
            else{
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                return true;
            }
        }
        return false;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        anterior = actual;
        switch(item.getItemId()){
            case R.id.navigation_home:
                actual = 1;
                fragment = homefragment;
                break;
            case R.id.Profile:
                actual = 2;
                fragment = profilefragment;
                break;
            case R.id.navigation_saved:
                actual =3;
                fragment = savedfragment;
                break;
            case R.id.navigation_search:
                actual = 4;
                fragment = searchfragment;
                break;
        }
         return loadFragment(fragment);
    }

}
