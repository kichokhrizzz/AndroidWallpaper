package com.example.fondos_de_pantalla.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fondos_de_pantalla.MainActivityAdministrador;
import com.example.fondos_de_pantalla.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class RegistrarAdmin extends Fragment {

    TextView FechaRegistro;
    EditText Password,Correo, Nombres, Apellidos, Edad;
    Button Registrar;

    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_registro_admin, container, false);

        FechaRegistro = view.findViewById(R.id.FechaRegistro);
        Correo = view.findViewById(R.id.Correo);
        Password = view.findViewById(R.id.Password);
        Nombres = view.findViewById(R.id.Nombres);
        Apellidos = view.findViewById(R.id.Apellidos);
        Edad = view.findViewById(R.id.Edad);

        Registrar = view.findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();//Iniciar FirebaseAuth

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String SFecha = fecha.format(date);//Convertir fecha a String
        FechaRegistro.setText(SFecha);

        //Al hacer click en registrar
        Registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String correo = Correo.getText().toString();
                String password = Password.getText().toString(); //Convertimos en string estos campos
                String nombre = Nombres.getText().toString();
                String apellidos = Apellidos.getText().toString();
                String edad = Edad.getText().toString();

                if(correo.equals("") || password.equals("") || nombre
                .equals("") || apellidos.equals("") || edad.equals(""))
                {
                    Toast.makeText(getActivity(), "Por favor llene todos los campos",Toast.LENGTH_SHORT).show();
                }else
                {

                    //Validacion del correo electronico
                    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches())
                    {
                        Correo.setError("Correo Inválido");
                        Correo.setFocusable(true);
                    }
                    else if(password.length() < 6)
                    {
                        Password.setError("Contraseña debe ser mayor a 6 caracteres");
                        Password.setFocusable(true);
                    }
                    else
                    {
                        RegistroAdministradores(correo, password);
                    }

                }

            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando Usuario");
        progressDialog.setCancelable(false);

        return view;
    }

    //Metodo para registrar administradores
    private void RegistroAdministradores(String correo, String password) {

        progressDialog.show();

        auth.createUserWithEmailAndPassword(correo,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Si el administrador fue creado correctamente
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;//Afirmar que el admin no es nulo

                        //Convertir a cadena los datos de los admins
                        String UID = user.getUid();
                        String correo = Correo.getText().toString();
                        String password = Password.getText().toString();
                        String nombre = Nombres.getText().toString();
                        String apellidos = Apellidos.getText().toString();
                        String edad = Edad.getText().toString();
                        int edadInt = Integer.parseInt(edad);

                        HashMap<Object, Object> Administradores = new HashMap<>();

                        Administradores.put("UID",UID);
                        Administradores.put("CORREO", correo);
                        Administradores.put("PASSWORD", password);
                        Administradores.put("NOMBRES",nombre);
                        Administradores.put("APELLIDOS",apellidos);
                        Administradores.put("EDAD",edadInt);

                        Administradores.put("IMAGEN","");

                        //INICIALIZAR FIREBASE DATABSE
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("BASE DE DATOS ADMINISTRADORES");
                        reference.child(UID).setValue(Administradores);

                        startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                        Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Ha Ocurrido un Error",Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

    }
}