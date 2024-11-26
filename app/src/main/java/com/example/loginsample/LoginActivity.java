package com.example.loginsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsample.databinding.ActivityMainBinding;
import com.example.loginsample.utils.UsuarioLogueado;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ActivityMainBinding binding;
    private AccountEntity accountEntity;
    private String accountEntityString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        EditText edtUsername = binding.edtUsername;
        EditText edtPassword = binding.edtPassword;
        Button btnLogin = binding.btnLogin;
        Button btnAddAccount = binding.btnAddAccount;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                // Simular autenticación de usuario
                if (username.equals("admin") && password.equals("admin")) {
                    Toast.makeText(getApplicationContext(), "Bienvenido a mi app", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Bienvenido a mi app");

                    // Guardar los datos del usuario en UsuarioLogueado
                    UsuarioLogueado.getInstance().setUsuario(new AccountEntity(username, "Admin", "admin@example.com"));

                    // Navegar a la pantalla principal
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("ACCOUNT", accountEntityString);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Error en la autenticación", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Error en la autenticación");
                }
            }
        });

        // Recuperar datos desde otra actividad
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult activityResult) {
                        Integer resultCode = activityResult.getResultCode();
                        Log.d(TAG, "resultCode: " + resultCode);

                        if (resultCode == AccountActivity.ACCOUNT_ACEPTAR) {
                            Intent data = activityResult.getData(); // Recuperar datos
                            accountEntityString = data.getStringExtra(AccountActivity.ACCOUNT_RECORD);

                            // Convertir JSON a objeto
                            Gson gson = new Gson();
                            accountEntity = gson.fromJson(accountEntityString, AccountEntity.class);

                            // Mostrar el nombre del usuario
                            String firstname = accountEntity.getFirstname();
                            Toast.makeText(getApplicationContext(), "Nombre: " + firstname, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Nombre: " + firstname);

                        } else if (resultCode == AccountActivity.ACCOUNT_CANCELAR) {
                            Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Cancelado");
                        }
                    }
                }
        );

        btnAddAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            activityResultLauncher.launch(intent);
        });
    }
}
