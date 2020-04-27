package com.example.apptesta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Receiver{
    private RecyclerView rvMensagens;
    private Button btnEnviar;
    private EditText txtMensagem;
    private ArrayList<Mensagem> mensagens;
    private AdapterMensagens adapt;
    private String endpoitIDConectado;
    private NearbyObj nearbyObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);

        rvMensagens = (RecyclerView) findViewById(R.id.rvMensagens);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        txtMensagem = (EditText) findViewById(R.id.txtMensagem);

        mensagens = new ArrayList<>();

        adapt = new AdapterMensagens(mensagens);
        rvMensagens.setAdapter(adapt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GlobalApplication.getContext().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        rvMensagens.setLayoutManager(layoutManager);

        nearbyObj = new NearbyObj("NicknameA", this);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(endpoitIDConectado != null){
                    nearbyObj.enviarMensagem(txtMensagem.getText().toString(),endpoitIDConectado);
                    txtMensagem.setText("");
                    Mensagem m = new Mensagem(txtMensagem.getText().toString(),"enviada",endpoitIDConectado);
                    mensagens.add(m);
                    adapt.notifyDataSetChanged();
                }else{
                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Não é possivel enviar mensagem!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void receberMensagem(Mensagem m) {
        mensagens.add(m);
        adapt.notifyDataSetChanged();
    }

    @Override
    public void receberEndpointID(String endpointID) {
        this.endpoitIDConectado = endpointID;
        Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Conexão estabelecida!", Toast.LENGTH_LONG).show();
    }
}
