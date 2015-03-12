package intep.proyecto.road2roldanillo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import intep.proyecto.road2roldanillo.entidades.db.Foto;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.map.MapHelper;


public class DescripcionFragment extends Fragment {

    private LinearLayout linearLayout;
    private Lugar site;

    public static DescripcionFragment newInstance(Lugar lugar) {
        DescripcionFragment fragment = new DescripcionFragment();
        Bundle args = new Bundle();
        args.putSerializable(MapHelper.KEY_SITE, lugar);
        fragment.setArguments(args);
        return fragment;
    }

    public DescripcionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            site = (Lugar) getArguments().getSerializable(MapHelper.KEY_SITE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descripcion, container, false);

        linearLayout = (LinearLayout) view.findViewById(R.id.layoutImagenes);


        if(site.getFotos()!=null && !site.getFotos().isEmpty()){
            for (Foto foto : site.getFotos()){
                ImageView imageView = (ImageView) inflater.inflate(R.layout.imagen_descripcion, null);
                imageView.setImageBitmap(foto.getImage(getActivity()));
                linearLayout.addView(imageView);
            }
        }
        /*ImageView imageView = (ImageView) inflater.inflate(R.layout.imagen_descripcion, null);
        imageView.setImageResource(R.drawable.imagen1);
        imageView.setImageResource(R.drawable.imagen2);
        imageView.setImageResource(R.drawable.imagen3);
        linearLayout.addView(imageView);*/

        TextView textViewNombre = (TextView) view.findViewById(R.id.textViewNombre);
        textViewNombre.setText(site.getNombre());

        TextView textViewDetalle = (TextView) view.findViewById(R.id.textViewDescripcion);
        textViewDetalle.setText(site.getDescripcion());

        TextView textViewDireccion = (TextView) view.findViewById(R.id.textViewDireccion);
        textViewDireccion.setText(site.getDireccion());

        TextView textViewWeb = (TextView) view.findViewById(R.id.textViewWeb);
        textViewWeb.setText(site.getSitio());

        TextView textViewTel = (TextView) view.findViewById(R.id.textViewTelefono);
        textViewTel.setText(site.getTelefono());

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        ratingBar.setRating(site.getPuntaje());

        return view;
    }

}
