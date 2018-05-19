package br.com.bossini.agendacomfirebasefatecipinoite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contatosReference;

    private void configuraFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        contatosReference = firebaseDatabase.getReference("contatos");
    }

    @Override
    protected void onStart() {
        super.onStart();
        contatosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contatos.clear();
                for (DataSnapshot filho : dataSnapshot.getChildren()){
                    Contato contato = filho.getValue(Contato.class);
                    contato.setChave(filho.getKey());
                    contatos.add(contato);
                }
                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,
                        getString(R.string.erro_firebase),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ListView contatosListView;
    private ArrayAdapter <Contato> contatosAdapter;
    private List <Contato> contatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contatosListView = findViewById(R.id.contatosListView);
        configuraFirebase();
        contatos = new Stack <Contato> ( );
        contatosAdapter = new ArrayAdapter<Contato>(
                this, android.R.layout.simple_list_item_1,
                                                    contatos);
        contatosListView.setAdapter(contatosAdapter);

        contatosListView.
                setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view,final  int position, long l) {
                        AlertDialog.Builder dBuilder = new AlertDialog.Builder(MainActivity.this);
                        dBuilder.setPositiveButton(getString(R.string.deletar_contato),
                                new DialogInterface.OnClickListener() {                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Contato vaiSerApagado = contatos.get(position);
                                        contatosReference.child(vaiSerApagado.getChave()).removeValue();
                                    Toast.makeText(MainActivity.this,
                                            getString(R.string.contato_removido),
                                            Toast.LENGTH_SHORT).show();

                                    }
                                }).setNegativeButton(getString(R.string.atualizar_contato),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AlertDialog.Builder dBuilder =
                                                new AlertDialog.Builder(MainActivity.this);
                                        LayoutInflater inflater =
                                                LayoutInflater.from(MainActivity.this);
                                        View view =
                                                inflater.inflate (R.layout.activity_adiciona_contato, null);

                                        final EditText nomeEditText =
                                                view.findViewById(R.id.nomeEditText);
                                        final EditText foneEditText =
                                                view.findViewById(R.id.foneEditText);
                                        final EditText emailEditText =
                                                view.findViewById(R.id.emailEditText);
                                        FloatingActionButton fab =
                                                view.findViewById(R.id.fab);
                                        final Contato caraQueVaiSerAtualizado = contatos.get(position);
                                        nomeEditText.setText(caraQueVaiSerAtualizado.getNome());
                                        foneEditText.setText(caraQueVaiSerAtualizado.getFone());
                                        emailEditText.setText(caraQueVaiSerAtualizado.getEmail());
                                        final AlertDialog dialog = dBuilder.setView (view).create();
                                        fab.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String nome =
                                                        nomeEditText.getEditableText().toString();
                                                String fone =
                                                        foneEditText.getEditableText().toString();
                                                String email =
                                                        emailEditText.getEditableText().toString();
                                                Contato subir =
                                                        new Contato (nome, fone, email );
                                                contatosReference.
                                                        child(caraQueVaiSerAtualizado.getChave()).
                                                        setValue(subir);
                                                dialog.cancel();
                                            }
                                        });
                                        dialog.show();

                                    }
                                }).create().show();
                        return true;
                    }
                });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i =
                       new Intent(MainActivity.this,
                               AdicionaContatoActivity.class);
               startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
