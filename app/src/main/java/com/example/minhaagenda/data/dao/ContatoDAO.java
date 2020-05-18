package com.example.minhaagenda.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.minhaagenda.data.model.Contato;

import java.util.ArrayList;

public class ContatoDAO extends SQLiteOpenHelper {

    public ContatoDAO( Context context) {
        super(context, "agenda_db", null, 1);
    }
    //Cria as tabelas do banco
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Contato (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "email TEXT, " +
                "telefone TEXT, " +
                "imagem TEXT, " +
                "excluido INT DEFAULT 0);";
        db.execSQL(sql);
    }

    //se tiver alguma mudança na tabela
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Contato> buscaContatos(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Contato WHERE excluido = 0";
        ArrayList<Contato> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null); //null por nao passar nenhum argumento na query
        if(cursor != null){
            while(cursor.moveToNext()){
                Contato contato = new Contato();
                contato.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                contato.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                contato.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                contato.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                contato.setImagem(cursor.getString(cursor.getColumnIndexOrThrow("imagem")));
                contato.setExcluido(cursor.getInt(cursor.getColumnIndexOrThrow("excluido")));
                lista.add(contato);
            }
            cursor.close();
        }
        return lista;
    }

    // Atribuido id ao contato
    public void insere(Contato contato) {
        ContentValues cv = criaContentValues(contato);

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert("Contato", null, cv); //retorna o id atribuido
        contato.setId((int) id);

    }
    private ContentValues criaContentValues(Contato contato){
        ContentValues cv = new ContentValues();
        cv.put("nome", contato.getNome());
        cv.put("email", contato.getEmail());
        cv.put("telefone", contato.getTelefone());
        cv.put("imagem", contato.getImagem());
        cv.put("excluido", contato.getExcluido());
        return cv;
    }

    public void edita(Contato contato) {
        ContentValues cv = criaContentValues(contato);
        String sql = " id = " + contato.getId();

        SQLiteDatabase db = getWritableDatabase();
        db.update("Contato", cv, sql,null);
    }
}
