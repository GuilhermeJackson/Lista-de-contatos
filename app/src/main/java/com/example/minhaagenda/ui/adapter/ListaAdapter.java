package com.example.minhaagenda.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minhaagenda.R;
import com.example.minhaagenda.data.model.Contato;

import java.util.ArrayList;

public class ListaAdapter extends BaseAdapter {
    private ArrayList<Contato> contatos;
    private Context context;

    public ListaAdapter(Context context, ArrayList<Contato> contatos){
        this.context = context;
        this.contatos = contatos;
    }
    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Contato getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    //onde é controlado todos os valores dos contatos
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lista_contatos, parent, false);
        }
        //criado o contato
        Contato contato = getItem(position);
        TextView viewNome = convertView.findViewById(R.id.item_lista_contato_nome);
        TextView viewEmail = convertView.findViewById(R.id.item_lista_contato_email);
        ImageView viewImage = convertView.findViewById(R.id.lista_image);

        viewEmail.setText(contato.getEmail());
        viewNome.setText(contato.getNome());

        return convertView;
    }
}
