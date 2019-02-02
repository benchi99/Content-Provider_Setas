package com.rubenbermejo.fml.contentprovidersetas;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class proveeContenido extends ContentProvider {

    private static final String uRI = "content://" + Utilidades.AUTHORITY + "/" + Utilidades.NOMBRE_TABLA;

    private static final Uri CONTENT_URI = Uri.parse(uRI);
    private static final int ACCEDE_TABLA = 1;
    private static final int ACCEDE_SETA_ID = 2;
    private SetasSQLiteHelper helper;
    private static final UriMatcher uriM;
    static {
        uriM = new UriMatcher(UriMatcher.NO_MATCH);
        uriM.addURI(Utilidades.AUTHORITY, Utilidades.NOMBRE_TABLA, ACCEDE_TABLA);
        uriM.addURI(Utilidades.AUTHORITY, Utilidades.NOMBRE_TABLA + "/#", ACCEDE_SETA_ID);
    }

    @Override
    public boolean onCreate() {

        helper = new SetasSQLiteHelper(getContext(), "Setas", null, Utilidades.VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String donde = selection;
        if(uriM.match(uri) == ACCEDE_SETA_ID){
            donde = "id = " + uri.getLastPathSegment();
        }

        SQLiteDatabase bd = helper.getWritableDatabase();

        Cursor c = bd.query(Utilidades.NOMBRE_TABLA, projection, donde, selectionArgs, null, null, sortOrder);

        return c;
    }

    @Override
    public String getType(Uri uri) {

        int match = uriM.match(uri);

        switch (match) {
            case ACCEDE_TABLA:
                return "vnd.android.cursor.dir/vnd.rubenbermejo.seta";
            case ACCEDE_SETA_ID:
                return "vnd.android.cursor.item/vnd.rubenbermejo.seta";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long reg = 1;
        SQLiteDatabase bd = helper.getWritableDatabase();
        reg = bd.insert(Utilidades.NOMBRE_TABLA, null, values);
        return ContentUris.withAppendedId(CONTENT_URI, reg);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int cont;

        String donde = selection;
        if(uriM.match(uri) == ACCEDE_SETA_ID){
            donde = "id = " + uri.getLastPathSegment();
        }

        SQLiteDatabase bd = helper.getWritableDatabase();

        cont = bd.delete(Utilidades.NOMBRE_TABLA, donde, selectionArgs);

        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;

        String donde = selection;
        if(uriM.match(uri) == ACCEDE_SETA_ID){
            donde = "id = " + uri.getLastPathSegment();
        }

        SQLiteDatabase bd = helper.getWritableDatabase();

        cont = bd.update(Utilidades.NOMBRE_TABLA, values, donde, selectionArgs);

        return cont;
    }
}
