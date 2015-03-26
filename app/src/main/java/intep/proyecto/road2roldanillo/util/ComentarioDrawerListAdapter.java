package intep.proyecto.road2roldanillo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.entidades.db.Comentario;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.persistencia.DBHelper;
import intep.proyecto.road2roldanillo.rest.ImageHelper;

/**
 * Created by gurzaf on 12/23/14.
 */
public class ComentarioDrawerListAdapter extends ArrayAdapter<Comentario>
        implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        PlusClient.OnPeopleLoadedListener{

    private static final String TAG = ComentarioDrawerListAdapter.class.getSimpleName();
    private PlusClient plusClient;
    private ImageView imageView;

    public ComentarioDrawerListAdapter(Context context, Lugar lugar) {
        super(context, R.layout.comment_item_row);

        plusClient = new PlusClient.Builder(context,this,this).build();

        SQLiteDatabase db = new DBHelper(context).getReadableDatabase();

        List<Comentario> comentarios = Comentario.getAllFromLugar(db,lugar);

        clear();

        addAll(comentarios);

        plusClient.connect();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v==null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.comment_item_row,null);
        }

        Comentario comentario = getItem(position);

        if(comentario!=null) {



            TextView nombreUsuario = (TextView) v.findViewById(R.id.labelNombreUsuario);
            TextView mensajeComentario = (TextView) v.findViewById(R.id.labelMensajeComentario);
            imageView = (ImageView) v.findViewById(R.id.imagenPerfilComentario);
            RatingBar ratingComentario = (RatingBar) v.findViewById(R.id.labelRatingComentario);

            if (nombreUsuario != null) {
                nombreUsuario.setText(comentario.getUsuarioNombre());
            }

            if (imageView != null) {

                if(plusClient!=null && plusClient.isConnected()){
                    plusClient.loadPeople(this,comentario.getUsuario());
                }

            }

            if(mensajeComentario!=null){
                mensajeComentario.setText(comentario.getDetalle());
            }

            if(ratingComentario!=null){
                ratingComentario.setRating(comentario.getPuntaje());
            }

        }

        return v;

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPeopleLoaded(ConnectionResult connectionResult, PersonBuffer personBuffer, String s) {

        if(connectionResult.getErrorCode()==ConnectionResult.SUCCESS){
            Person person = personBuffer.get(0);

            try {
                new LoadProfileImage(imageView).execute(person.getCover().getCoverPhoto().getUrl());
            }catch (Exception e){
                Log.w(TAG, "No tiene imagen de perfil");
            }

        }

    }
}
