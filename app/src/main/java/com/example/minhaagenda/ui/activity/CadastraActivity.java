package com.example.minhaagenda.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.minhaagenda.R;
import com.example.minhaagenda.data.dao.ContatoDAO;
import com.example.minhaagenda.data.model.Contato;

public class CadastraActivity extends AppCompatActivity {
    private Contato contato;
    private EditText viewNome, viewEmail, viewTelefone;
    private ImageView viewImagem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra);

        viewNome = findViewById(R.id.cadastro_nome);
        viewEmail = findViewById(R.id.cadastro_email);
        viewTelefone = findViewById(R.id.cadastro_telefone);
        viewImagem = findViewById(R.id.cadastro_image);



        contato = new Contato();
    }

    //insere o menu na tela
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastra, menu);

        return super.onCreateOptionsMenu(menu);
    }
    //chamado sempre q clicado em um item do menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_cadastra_salvar){
            salvarContato();
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvarContato() {
        pegaValoresTela();
        ContatoDAO dao = new ContatoDAO(this);
        dao.insere(contato);
        dao.close();

        finish();
    }

    private void pegaValoresTela() {
        contato.setNome(viewNome.getText().toString());
        contato.setEmail(viewEmail.getText().toString());
        contato.setTelefone(viewTelefone.getText().toString());
        //contato.setImagem(viewImagem.getText().toString());
    }
}