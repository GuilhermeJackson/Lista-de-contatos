package com.example.minhaagenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minhaagenda.R;
import com.example.minhaagenda.data.dao.ContatoDAO;
import com.example.minhaagenda.data.model.Contato;
import com.example.minhaagenda.ui.adapter.ListaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ListView listaContatosView;
    ArrayList<Contato> contatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaContatosView = findViewById(R.id.lista_lista_contatos);

        listaContatosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = contatos.get(position);

                Intent intent = new Intent(ListActivity.this, CadastraActivity.class);
                intent.putExtra(CadastraActivity.PARAMETRO_CONTATO, contato);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, CadastraActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaContatosView.setAdapter(new ListaAdapter(this, getContatos()));
    }

    private ArrayList<Contato> getContatos() {
        contatos = new ArrayList<>();
        ContatoDAO contatoDAO = new ContatoDAO(this);
        contatos = contatoDAO.buscaContatos();
        contatoDAO.close();
        return contatos;
    }
}
