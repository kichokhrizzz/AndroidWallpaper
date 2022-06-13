package com.example.fondos_de_pantalla.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fondos_de_pantalla.InicioSesion;
import com.example.fondos_de_pantalla.R;


public class AcercaDeCliente extends Fragment {

    Button Acceder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_acerca_de_cliente, container, false);
        // Inflate the layout for this fragment
        Acceder = view.findViewById(R.id.Acceder);

        Acceder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InicioSesion.class));


            }
        });

        return view;
    }
}