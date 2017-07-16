package thiagocury.eti.br.excadastroclientefirebaseapi19;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Widgets
    private EditText etNome;
    private EditText etRG;
    private EditText etRenda;
    private Button btnCadastrar;
    private ListView lvClientes;

    //ArrayList + ArrayAdapter
    private ArrayAdapter<Cliente> adapter;
    private ArrayList<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        etNome = (EditText) findViewById(R.id.et_nome);
        etRG = (EditText) findViewById(R.id.et_rg);
        etRenda = (EditText) findViewById(R.id.et_renda);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastrar);
        lvClientes = (ListView) findViewById(R.id.lv_clientes);

        //ArrayList + ArrayAdapter
        clientes = new ArrayList<>();
        adapter = new ArrayAdapter<Cliente>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                clientes);

        lvClientes.setAdapter(adapter);

        //Firebase
        FirebaseApp.initializeApp(MainActivity.this);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference banco = db.getReference("clientes");

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!etNome.getText().toString().isEmpty() &&
                   !etRG.getText().toString().isEmpty() &&
                   !etRenda.getText().toString().isEmpty()) {

                    Cliente c = new Cliente();
                    c.setNome(etNome.getText().toString());
                    c.setRG(Integer.parseInt(etRG.getText().toString()));
                    c.setRenda(Double.parseDouble(etRenda.getText().toString()));

                    //Enviando para o Firebase
                    banco.push().setValue(c);

                    limpar();

                    Toast.makeText(
                            getBaseContext(),
                            getResources().getString(R.string.toast_sucesso_adicionar),
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(
                            getBaseContext(),
                            getResources().getString(R.string.toast_erro),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        banco.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                clientes.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Cliente c = data.getValue(Cliente.class);
                    c.setKey(data.getKey()); //Colocando key manualmente no objeto
                    clientes.add(c);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //objeto que tem a key
                final int posSelec = i;

                //Buscando objeto selecionado
                final Cliente c = clientes.get(i);

                AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View editarCliente = inflater.inflate(R.layout.editar_cliente, null);
                msg.setView(editarCliente);

                final EditText etLogin = (EditText) editarCliente.findViewById(R.id.editar_et_nome);
                final EditText etRG = (EditText) editarCliente.findViewById(R.id.editar_et_rg);
                final EditText etRenda = (EditText) editarCliente.findViewById(R.id.editar_et_renda);

                etLogin.setText(c.getNome());
                etRG.setText(String.valueOf(c.getRG()));
                etRenda.setText(String.valueOf(c.getRenda()));

                msg.setTitle(getResources().getString(R.string.alert_titulo));
                msg.setMessage(getResources().getString(R.string.alert_alterar_msg));
                msg.setPositiveButton(getResources().getString(R.string.alert_botao_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Recebendo dados alterados
                        c.setNome(etLogin.getText().toString());
                        c.setRG(Integer.parseInt(etRG.getText().toString()));
                        c.setRenda(Double.parseDouble(etRenda.getText().toString()));

                        //Alterando através da chave(key) no firebase setando o novo valor
                        banco.child(c.getKey()).setValue(c);

                        Toast.makeText(
                                getBaseContext(),
                                getResources().getString(R.string.toast_sucesso_alterar),
                                Toast.LENGTH_LONG).show();
                    }
                });
                msg.setNegativeButton(getResources().getString(R.string.alert_botao_nao), null);
                msg.show();
            }
        });

        lvClientes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {

                        final int posSelec = i;

                        AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);
                        msg.setTitle(getResources().getString(R.string.alert_titulo));
                        msg.setMessage(getResources().getString(R.string.alert_msg));
                        msg.setPositiveButton(getResources().getString(R.string.alert_botao_sim), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Cliente c = clientes.get(posSelec);

                            //Removendo através da chave(key) no firebase
                            banco.child(c.getKey()).removeValue();

                            clientes.remove(posSelec);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(
                                    getBaseContext(),
                                    getResources().getString(R.string.toast_sucesso_remocao),
                                    Toast.LENGTH_LONG).show();
                    }
                });
                msg.setNegativeButton(getResources().getString(R.string.alert_botao_nao), null);
                msg.show();
                return true;
            }
        });
    }

    private void limpar(){
        etNome.setText(null);
        etRG.setText(null);
        etRenda.setText(null);
    }
}