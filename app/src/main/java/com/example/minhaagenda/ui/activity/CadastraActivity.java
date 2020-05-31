package com.example.minhaagenda.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.minhaagenda.R;
import com.example.minhaagenda.data.dao.ContatoDAO;
import com.example.minhaagenda.data.model.Contato;
import com.example.minhaagenda.util.ImagemUtils;

import java.io.File;

public class CadastraActivity extends AppCompatActivity {
    public static final String PARAMETRO_CONTATO = "PARAMETRO_CONTATO";
    private static final int CAMERA_REQUEST_CODE = 495;
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
        Intent intent = getIntent();
        if(intent.hasExtra(PARAMETRO_CONTATO)){
            Contato contatoRecuperado = (Contato) intent.getSerializableExtra(PARAMETRO_CONTATO);
            contato = contatoRecuperado;
            popularTela();
        }
        
        viewImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarCamera();
            }
        });
    }

    private void chamarCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String caminhoImagem = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
        contato.setImagem(caminhoImagem);
        //monta a foto
        File foto = new File(caminhoImagem);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(
                this, "com.example.minhaagenda.fileProvider", foto)
        );
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ImagemUtils.setImagem(viewImagem, contato.getImagem());
        }
    }

    private void popularTela() {
        viewNome.setText(contato.getNome());
        viewEmail.setText(contato.getEmail());
        viewTelefone.setText(contato.getTelefone());
        //viewImagem.setText(contato.getNome());
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
        if(contato.getId() == 0)
            dao.insere(contato);
        else
            dao.edita(contato);

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
