package intep.proyecto.road2roldanillo.util;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;

import intep.proyecto.road2roldanillo.R;

/**
 * Created by gurzaf on 3/11/15.
 */
public class UIHelper {

    public static void addLabelMessageToList(final String message, final Activity context, final LinearLayout linearLayout){

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView etiqueta = (TextView) context.getLayoutInflater().inflate(R.layout.label_actualizado, null);
                etiqueta.setTextAppearance(context, R.style.fuente_label_actualizado_categorias);
                etiqueta.setText(message);
                linearLayout.addView(etiqueta);
            }
        });


    }

}
