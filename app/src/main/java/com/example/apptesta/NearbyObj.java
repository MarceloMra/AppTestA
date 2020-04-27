package com.example.apptesta;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class NearbyObj {
    private String nickname;
    private final String SERVICE_ID;
    private Receiver receiver;

    public NearbyObj(String nickname, Receiver receiver) {
        this.nickname = nickname;
        this.receiver = receiver;
        SERVICE_ID = GlobalApplication.getContext().getString(R.string.service_id);
    }

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endPointId, Payload payload) {
            Mensagem m = new Mensagem(new String(payload.asBytes(), StandardCharsets.UTF_8), "recebida" ,endPointId);
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate payloadTransferUpdate) {
            //Notifying the receivers

        }
    };

    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(final String endpointId, final DiscoveredEndpointInfo discoveredEndpointInfo) {
            com.google.android.gms.nearby.Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).requestConnection(
                    nickname,
                    endpointId,
                    mConnectionLifecycleCallback)
                    .addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unusedResult) {
                                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint encontrado! ", Toast.LENGTH_LONG).show();

                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Falha no endpoint encontrado! ", Toast.LENGTH_LONG).show();

                                }
                            });
        }

        @Override
        public void onEndpointLost(String endpointId) {
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint perdido! ", Toast.LENGTH_LONG).show();
        }
    };

    private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(final String endpointId, ConnectionInfo connectionInfo) {
            com.google.android.gms.nearby.Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).acceptConnection(endpointId, mPayloadCallback);
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Conexão aceita! ", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {

            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    receiver.receberEndpointID(endpointId);
                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint conectado com sucesso! ", Toast.LENGTH_LONG).show();
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Conexão rejeitada! ", Toast.LENGTH_LONG).show();
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Erro após aceitar a conexão! ", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        @Override
        public void onDisconnected(String endpointId) {
            Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Endpoint desconectou-se! ", Toast.LENGTH_LONG).show();
        }
    };

    public void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext())
                .startAdvertising(nickname, SERVICE_ID, mConnectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                //Notifying the receivers
                                Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Anunciamento iniciado!", Toast.LENGTH_LONG).show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Notifying the receivers
                                Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Erro ao tentar anunciar: "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }

    public void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build();
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext())
                .startDiscovery(SERVICE_ID, mEndpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Descobrimento iniciado!", Toast.LENGTH_LONG).show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GlobalApplication.getContext().getApplicationContext(), "Erro ao tentar descobrir: "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }

    public void enviarMensagem(String msg, String endpointID){
        Payload p = Payload.fromBytes(msg.getBytes());
        Nearby.getConnectionsClient(GlobalApplication.getContext().getApplicationContext()).sendPayload(endpointID, p);
    }
}
