package intep.proyecto.road2roldanillo;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.map.MapHelper;


public class CommentsFragment extends Fragment {

    public static CommentsFragment newInstance(Lugar lugar) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(MapHelper.KEY_SITE, lugar);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentsFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }


}
