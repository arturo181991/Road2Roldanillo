package intep.proyecto.road2roldanillo.util;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import intep.proyecto.road2roldanillo.R;
import intep.proyecto.road2roldanillo.entidades.Categoria;

/**
 * Created by gurzaf on 12/23/14.
 */
public class NavigatioDrawerListAdapter extends ArrayAdapter<Categoria> {

    public NavigatioDrawerListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public NavigatioDrawerListAdapter(Context context, int resource, Categoria[] items) {
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
                sw.setText(categoria.toString());
            }

            if (imageView != null) {
                imageView.setImageResource(categoria.getResourceIcon());
            }
        }

        return v;

    }
}
