package com.example.journeyaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class UserGoogleMapActivity extends AppCompatActivity {

    private boolean isFragmentProfileOpen, isFragmentVisitListOpen, isFragmentChatOpen = false;
    Button btnProfile, btnVisitingList,btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentProfile fragmentClassProfile = new FragmentProfile();
        FragmentVisitList fragmentClassVisitList = new FragmentVisitList();
        FragmentChat fragmentClassChat = new FragmentChat();

        setContentView(R.layout.activity_user_google_map);
        btnProfile = findViewById(R.id.btnProfile);
        btnVisitingList = findViewById(R.id.btnVisitingList);
        btnChat = findViewById(R.id.btnChat);

        getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentHome, FragmentHome.class, null)
                        .setReorderingAllowed(true)
                        .commit();
               //getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragmentHome, FragmentHome.class,null).commit();


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isFragmentVisitListOpen){
                    fragmentManager.popBackStack("FragmentVisitList", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentVisitListOpen = false;
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentProfile, fragmentClassProfile, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentProfileOpen = true;
                }

                else if(isFragmentChatOpen){
                    fragmentManager.popBackStack("FragmentChat", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentChatOpen = false;
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentProfile, fragmentClassProfile, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentProfileOpen = true;
                }

                else if (isFragmentProfileOpen) {
                    // Close the fragment
                    fragmentManager.popBackStack("FragmentHome", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentProfileOpen = false;
                } else {
                    // Open the fragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentProfile, fragmentClassProfile, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentProfileOpen = true;


                }

            }
        });


        btnVisitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(isFragmentProfileOpen){
                    fragmentManager.popBackStack("FragmentProfile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentProfileOpen = false;
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentVisitList, fragmentClassVisitList, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentVisitListOpen = true;
                }

                else if(isFragmentChatOpen){
                    fragmentManager.popBackStack("FragmentChat", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentChatOpen = false;
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentVisitList, fragmentClassVisitList, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentVisitListOpen = true;
                }

                else if (isFragmentVisitListOpen) {
                    // Close the fragment
                    fragmentManager.popBackStack("FragmentHome", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentVisitListOpen = false;
                } else {
                    // Open the fragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentVisitList, fragmentClassVisitList, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentVisitListOpen = true;


                }

            }
        });


        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(isFragmentVisitListOpen){
                    fragmentManager.popBackStack("FragmentVisitList", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentVisitListOpen = false;
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentChat, fragmentClassChat, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentChatOpen = true;
                }

                else if (isFragmentProfileOpen) {
                    // Close the fragment
                    fragmentManager.popBackStack("FragmentProfile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentProfileOpen = false;
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentChat, fragmentClassChat, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("FragmentHome")
                            .commit();
                    isFragmentChatOpen = true;
                }

                else if(isFragmentChatOpen) {
                    // Close the fragment
                    fragmentManager.popBackStack("FragmentHome", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    isFragmentChatOpen = false;
                }
                else{
                        // Open the fragment
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentChat, fragmentClassChat, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("FragmentHome")
                                .commit();
                        isFragmentProfileOpen = true;


                    }
                }

        });

    }

}