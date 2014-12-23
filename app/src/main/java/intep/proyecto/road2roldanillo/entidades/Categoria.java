package intep.proyecto.road2roldanillo.entidades;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import intep.proyecto.road2roldanillo.R;

/**
 * Created by gurzaf on 12/23/14.
 */
public enum Categoria {

    HOTEL(R.drawable.hotel),
    RESTAURANT(R.drawable.restaurant);

    private int resource_icon;

    private Categoria(int resource_icon){
        this.resource_icon = resource_icon;
    }

    public BitmapDescriptor getBitMapDescriptor(){
        return BitmapDescriptorFactory.fromResource(resource_icon);
    }

    public int getResourceIcon(){
        return this.resource_icon;
    }
    
}
