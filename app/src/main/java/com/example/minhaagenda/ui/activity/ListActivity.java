package com.example.minhaagenda.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.minhaagenda.R;
import com.example.minhaagenda.data.dao.ContatoDAO;
import com.example.minhaagenda.data.model.Contato;
import com.example.minhaagenda.ui.adapter.ListaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 821;
    ListView listaContatosView;
    ArrayList<Contato> contatos;

    private String[] permissoes = new String[]{
            Manifest.permission.SEND_SMS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaContatosView = findViewById(R.id.lista_lista_contatos);

        validaPermissoes(permissoes);

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

        //Registrar menu de contexto (quando deixa clicado no item exibe mensagem de apagar)
        registerForContextMenu(listaContatosView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        criaAdapter();
    }

    private void criaAdapter() {
        listaContatosView.setAdapter(new ListaAdapter(this, getContatos()));
    }

    private ArrayList<Contato> getContatos() {
        contatos = new ArrayList<>();
        ContatoDAO contatoDAO = new ContatoDAO(this);
        contatos = contatoDAO.buscaContatos();
        contatoDAO.close();
        return contatos;
    }

    //Cria ação do contextMenu (pressionar o item e exibir mensagem)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contato contato = (Contato) listaContatosView.getItemAtPosition(info.position);

        MenuItem sms = menu.add("Envia sms");
        sms.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LayoutInflater inflater = LayoutInflater.from(ListActivity.this);
                View view = inflater.inflate(R.layout.dialog_envia_sms, null);

                TextView viewTelefone = view.findViewById(R.id.lista_dialog_telefone);
                final EditText viewMensagem = view.findViewById(R.id.lista_dialog_mensagem);

                String[] split = contato.getNome().split(" ");
                String telefone = "Para: " + split[0] + " " + contato.getTelefone();
                viewTelefone.setText(telefone);

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle(getString(R.string.app_name));
                builder.setView(view);

                builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mensagem = viewMensagem.getText().toString();
                        if (mensagem.isEmpty())
                            return;
                        eviarSMS(contato.getTelefone().trim(), mensagem);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });

        MenuItem del = menu.add("Apagar esse contato");
        del.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ContatoDAO dao = new ContatoDAO(ListActivity.this);
                dao.remove(contato);
                dao.close();
                criaAdapter();
                return false;
            }
        });
    }

    public String replaceAll(String regex, String replacement) {
        return Pattern.compile(regex).matcher("0123456789").replaceAll(replacement);
    }

    private void eviarSMS(String telefone, String mensagem) {
        try {
            String telefoneTratado = replaceAll(telefone, "");
            telefoneTratado = "+55" + telefoneTratado;
            
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefoneTratado, null, mensagem, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validaPermissoes(String[] permissoes) {
        //verifica se a aplicação roda em um abiente q precisa de permissao
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listaPermissoes = new ArrayList<>();
            for (String permissao : permissoes) {
                //se a permissao q esta solicitando ja foi atribuida a aplicação
                Boolean validaPermissao = ContextCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validaPermissao) {
                    listaPermissoes.add(permissao);
                }
            }
            if (!listaPermissoes.isEmpty()) {
                String[] novasPermissoes = new String[listaPermissoes.size()];
                listaPermissoes.toArray(novasPermissoes);
                ActivityCompat.requestPermissions(ListActivity.this, novasPermissoes, REQUEST_CODE_PERMISSION);
            }
        }
    }

    //metodo para vê se as permissoes foram aceitas
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado : grantResults) {
            //se a permissao for negada, faz uma ação
            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidaPermissao();
            }
        }
    }

    private void alertaValidaPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utlizar esse APP, é necessário aceitar todas as permissões");
        builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validaPermissoes(permissoes);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
