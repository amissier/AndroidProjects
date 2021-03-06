/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.materialme;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/***
 * Main Activity for the Material Me app, a mock sports news application with poor design choices
 */
public class MainActivity extends AppCompatActivity {

    //Member variables
    private RecyclerView mRecyclerView;
    private ArrayList<Sport> mSportsData;
    private SportsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialize the RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        //Set the Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the ArrayLIst that will contain the data
        mSportsData = new ArrayList<>();

        if(savedInstanceState != null)
        {
            mSportsData = savedInstanceState.getParcelableArrayList("SPORTS_DAT");
        }



            //Initialize the adapter and set it ot the RecyclerView
            mAdapter = new SportsAdapter(this, mSportsData);
            mRecyclerView.setAdapter(mAdapter);

            if(savedInstanceState == null) {
                //Get the data
                initializeData();
            }

            ItemTouchHelper ith = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT |
                    ItemTouchHelper.RIGHT | ItemTouchHelper.UP
                    | ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    int currpos = viewHolder.getAdapterPosition();
                    int nextpos = target.getAdapterPosition();
                    Collections.swap(mSportsData, currpos, nextpos);

                    mAdapter.notifyItemMoved(currpos, nextpos);
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    mSportsData.remove(viewHolder.getAdapterPosition());

                    mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            });

            ith.attachToRecyclerView(mRecyclerView);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("SPORTS_DAT",mSportsData);
    }

    /**
     * Method for initializing the sports data from resources.
     */
    private void initializeData() {
        //Get the resources from the XML file
        String[] sportsList = getResources().getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources().getStringArray(R.array.sports_info);
        TypedArray sportsimages = getResources().obtainTypedArray(R.array.sports_images);

        //Clear the existing data (to avoid duplication)
        mSportsData.clear();

        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i=0;i<sportsList.length;i++){
            mSportsData.add(new Sport(sportsList[i],sportsInfo[i],sportsimages.getResourceId(i,0)));
        }

        sportsimages.recycle();

        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }

    public void resetdata(View view) {
        initializeData();
    }
}
