package intep.proyecto.road2roldanillo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import intep.proyecto.road2roldanillo.entidades.Site;


public class DescripcionFragment extends TabbedActivity.PlaceholderFragment {

    private Site site;

    public static DescripcionFragment newInstance(Site site) {
        DescripcionFragment fragment = new DescripcionFragment();
        Bundle args = new Bundle();
        args.putSerializable(MapsActivity.KEY_SITE, site);
        fragment.setArguments(args);
        return fragment;
    }

    public DescripcionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            site = (Site) getArguments().getSerializable(MapsActivity.KEY_SITE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descripcion, container, false);


        TextView textViewNombre = (TextView) view.findViewById(R.id.textViewNombre);
        textViewNombre.setText(site.getNombres());

        TextView textViewDetalle = (TextView) view.findViewById(R.id.textViewDescripcion);
        textViewDetalle.setText(site.getDetalle());

        TextView textViewDireccion = (TextView) view.findViewById(R.id.textViewDireccion);
        textViewDireccion.setText(site.getDireccion());

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setRating(site.getRating());

        return view;
    }

}
