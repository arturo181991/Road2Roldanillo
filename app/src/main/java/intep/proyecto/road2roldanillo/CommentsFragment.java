package intep.proyecto.road2roldanillo;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.util.Calendar;

import intep.proyecto.road2roldanillo.entidades.db.Comentario;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.map.MapHelper;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.util.rest.ActualizarComentarios;


public class CommentsFragment extends Fragment
        implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        PlusClient.OnPeopleLoadedListener{

    private static final String TAG = CommentsFragment.class.getSimpleName();

    private Lugar lugar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private LinearLayout layoutComentario;

    private EditText campoComentario;
    private RatingBar campoRatingComentario;
    private Button botonEnviarComentario;

    private PlusClient plusClient;
    private Person person;


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
    public void onStart() {
        super.onStart();
        plusClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        plusClient.disconnect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lugar = (Lugar) getArguments().getSerializable(MapHelper.KEY_SITE);
        }
        plusClient = new PlusClient.Builder(getActivity().getApplicationContext(),this,this).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ActualizarComentarios actualizarComentarios = new ActualizarComentarios(lugar,CommentsFragment.this);
                actualizarComentarios.execute();
            }
        });

        listView = (ListView) view.findViewById(R.id.listViewComments);
        layoutComentario = (LinearLayout) view.findViewById(R.id.layoutComentario);

        botonEnviarComentario = (Button) view.findViewById(R.id.botonAgregarComentario);
        campoComentario = (EditText) view.findViewById(R.id.campoComentario);
        campoRatingComentario = (RatingBar) view.findViewById(R.id.campoRatingLugar);

        botonEnviarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarComentario();
            }

        });

        return view;
    }

    public void updateCommentList(){

    }

    public void hideRefreshing(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }


    private void agregarComentario() {

        try {
            String textoComentario = campoComentario.getText().toString();
            float rating = campoRatingComentario.getRating();

            Comentario comentario = new Comentario(0);
            comentario.setBorrado(0);
            comentario.setDetalle(textoComentario);
            comentario.setFecha(Calendar.getInstance().getTime());
            comentario.setLugar(lugar.getId());
            comentario.setPuntaje(new Float(rating).intValue());
            comentario.setUsuario(person.getId());
            comentario.setUsuarioNombre(person.getDisplayName());

            SQLiteDatabase db = new DBHelper(getActivity()).getWritableDatabase();

            comentario.insert(db);

            Toast.makeText(getActivity(),"Se guardo tu comentario",Toast.LENGTH_SHORT).show();

            campoComentario.setText("");
            campoRatingComentario.setRating(3);

        }catch (Exception e){
            Toast.makeText(getActivity(),"No pudimos guardar tu comentario, intenta de nuevo",Toast.LENGTH_SHORT).show();
            Log.e(TAG,"Error creando el comentario",e);
        }

    }


    @Override
    public void onConnected(Bundle bundle) {

        plusClient.loadPeople(this,"me");
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPeopleLoaded(ConnectionResult connectionResult, PersonBuffer personBuffer, String s) {
        if(connectionResult.getErrorCode()==ConnectionResult.SUCCESS) {
            person = personBuffer.get(0);
            layoutComentario.setVisibility(View.VISIBLE);
        }
    }
}
