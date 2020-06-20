package com.example.findyourstyle.Adampters;


import com.example.findyourstyle.R;

public class AdapterCategoriaTienda {
    //JSON URL
    static final String ip = String.valueOf(R.string.ip);
    public static final String DATA_URL = ip + "/findyourstyleBDR/wsJSONCategoriaTienda.php";

    //Tags used in the JSON String
    public static final String TAG_ID = "id_categoria";
    public static final String TAG_NAME = "nombre_categoria";

    //JSON array name
    public static final String JSON_ARRAY = "categoria";
}
