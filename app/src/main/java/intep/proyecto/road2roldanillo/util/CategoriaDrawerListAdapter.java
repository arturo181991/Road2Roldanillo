package intep.proyecto.road2roldanillo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.rest.ImageHelper;

/**
 * Created by gurzaf on 12/23/14.
 */
public class CategoriaDrawerListAdapter extends ArrayAdapter<Categoria> {

    public CategoriaDrawerListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CategoriaDrawerListAdapter(Context context, int resource, List<Categoria> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v==null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.menu_item_row,null);
        }

        Categoria categoria = getItem(position);

        if(categoria!=null) {

            TextView sw = (TextView) v.findViewById(R.id.labelMenuItem);
            ImageView imageView = (ImageView) v.findViewById(R.id.iconMenu);

            if (sw != null) {
                sw.setText(categoria.getNombre());
            }

            if (imageView != null) {

                Bitmap bm = ImageHelper.getImageForCategoria(categoria,getContext());
                if(bm==null){
                    imageView.setImageResource(R.drawable.me);
                }else{
                    imageView.setImageBitmap(bm);
                }

            }
        }

        return v;

    }
}
