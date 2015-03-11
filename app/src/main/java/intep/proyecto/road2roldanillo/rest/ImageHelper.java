package intep.proyecto.road2roldanillo.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import intep.proyecto.road2roldanillo.entidades.db.Categoria;
import intep.proyecto.road2roldanillo.entidades.db.Foto;
import intep.proyecto.road2roldanillo.entidades.db.Lugar;
import intep.proyecto.road2roldanillo.util.Constantes;

/**
 * Created by gurzaf on 1/9/15.
 */
public class ImageHelper {

    private static final String TAG = ImageHelper.class.getSimpleName();

    enum SIZE{
        mdpi,
        hdpi,
        xhdpi,
        xxhdpi
    }

    private static Bitmap getBitmapFromURL(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (IOException e) {
            Log.e(TAG, e.getMessage().toString(),e);
            return null;
        }
    }

    public static boolean saveImageForCategoria(Categoria categoria, Context context){

        Log.i(TAG,"Obteniendo imagenes para la categoria: ".concat(categoria.getNombre()));
        Bitmap[] imagenes = new Bitmap[SIZE.values().length];
        for (int i = 0; i<SIZE.values().length; i++){
            SIZE size = SIZE.values()[i];
            String url = Constantes.concatPath(Constantes.getBASE_PATH(),Constantes.CATEGORIAS_IMAGES_PATH);
            String[] parts = url.split("#");
            url = parts[0].concat(size.toString()).concat(parts[1]).concat(categoria.getIcono());
            Log.i(TAG,"URL para la imagen para la categoria: ".concat(categoria.getNombre()
                .concat(" y el tamaño: ").concat(size.toString()))
                .concat(" ===> ").concat(url));
            Bitmap bitmap = getBitmapFromURL(url);
            if(bitmap==null){
                Log.w(TAG,"No se encontró la imagen para la categoria");
                return false;
            }else{
                imagenes[i] = bitmap;
            }
        }

        for (int i = 0; i<SIZE.values().length; i++){
            String name = "categoria_"
                    .concat(SIZE.values()[i].toString()).concat("_")
                    .concat(categoria.getNombre());
            Log.i(TAG,"Nombre de la imagen: ".concat(name));
            if(!saveImageToInternalStorage(name,imagenes[i],context)){
                Log.i(TAG,"No se pudo guardar una de las imagenes");
                return false;
            }
        }

        Log.i(TAG,"Se crean todas las imagenes para la categoria");

        return true;

    }

    public static boolean saveImageForLugar(String nombreLugar, List<Foto> fotos, Context context){

        Log.i(TAG,"Obteniendo imagenes para el lugar: ".concat(nombreLugar));

        Bitmap[] imagenes = new Bitmap[fotos.size()];
        for (int i = 0; i<fotos.size(); i++){
            Foto foto = fotos.get(i);

            String url = Constantes.concatPath(Constantes.getBASE_PATH(),Constantes.LUGARES_IMAGES_PATH,foto.getFoto());


            Bitmap bitmap = getBitmapFromURL(url);
            if(bitmap==null){
                Log.w(TAG,"No se encontró la imagen para el lugar");
                return false;
            }else{
                String name = "lugar_"+i+"_".concat(nombreLugar);
                fotos.get(i).setFoto(name);
                imagenes[i] = bitmap;
            }
        }

        for(int i=0; i<imagenes.length; i++){


            Log.i(TAG,"Nombre de la imagen: ".concat(fotos.get(i).getFoto()));
            if(!saveImageToInternalStorage(fotos.get(i).getFoto(),imagenes[i],context)){
                Log.i(TAG,"No se pudo guardar una de las imagenes");
                return false;
            }
        }

        Log.i(TAG,"Se crean todas las imagenes para el lugar");

        return true;

    }

    private static boolean saveImageToInternalStorage(String name, Bitmap image, Context context) {

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    private static Bitmap loadImageFromInternalStorage(String name, Context context){
        Bitmap image = null;
        try {
            File filePath = context.getFileStreamPath(name);
            FileInputStream fi = new FileInputStream(filePath);
            image = BitmapFactory.decodeStream(fi);
        } catch (Exception ex) {
            Log.e(TAG,"Error obteniendo la imagen desde la memoria interna", ex);
        }
        return image;
    }

    public static Bitmap getImageForFoto(Foto foto, Context context){
        Bitmap bitmap = loadImageFromInternalStorage(foto.getFoto(),context);
        return bitmap;
    }

    public static Bitmap getImageForCategoria(Categoria categoria, Context context){

        String size;
        switch (context.getResources().getDisplayMetrics().densityDpi){
            case DisplayMetrics.DENSITY_MEDIUM:
                size = SIZE.mdpi.toString();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                size = SIZE.hdpi.toString();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                size = SIZE.xhdpi.toString();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                size = SIZE.xxhdpi.toString();
                break;
            default:
                size = SIZE.hdpi.toString();
                break;
        }

        String name = "categoria_"
                .concat(size).concat("_")
                .concat(categoria.getNombre());

        Bitmap bitmap = loadImageFromInternalStorage(name,context);

        return bitmap;

    }


}
