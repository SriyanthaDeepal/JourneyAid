package com.example.journeyaid;

import static org.chromium.base.ContextUtils.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentVisitList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentVisitList extends Fragment {

    ArrayList<String> visitList;
    ListView listView;
    Button button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentVisitList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVisitList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentVisitList newInstance(String param1, String param2) {
        FragmentVisitList fragment = new FragmentVisitList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_list, container, false);

        listView = view.findViewById(R.id.visitListView);
        button = view.findViewById(R.id.btnFinish);
        visitList = FragmentHome.visitList;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.list_view_color, visitList);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DownlaodMap.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        // Inflate the layout for this fragment
        return view;


    }
}