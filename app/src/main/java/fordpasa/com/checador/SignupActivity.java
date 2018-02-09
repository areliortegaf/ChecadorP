package fordpasa.com.checador;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;



public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                signup();
           /*     String claveUsuario = _nameText.getText().toString();
                String correoUsuario = _emailText.getText().toString();
                String contraUsuario = _passwordText.getText().toString();
                RegistrarUsuario x = new RegistrarUsuario(claveUsuario, correoUsuario, contraUsuario);
                if(activaToast==1){
                    Toast.makeText(getBaseContext(), "El registro fue exitoso", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(), "Error al registrar el usuario", Toast.LENGTH_LONG).show();
                }
*/

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    private RegistroListener listener;

    public void cambiarListener(RegistroListener listener){
        this.listener = listener;
    }

    public RegistroListener obetenerListener(){
        return this.listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando Registro...");
        progressDialog.show();

        String claveUsuario = _nameText.getText().toString();
        String correoUsuario = _emailText.getText().toString();
        String contraUsuario = _passwordText.getText().toString();

        /*Implementacion del listener*/
        int num = 0;

        RegistrarUsuario x = new RegistrarUsuario(claveUsuario, correoUsuario, contraUsuario);
        RegistrarUsuario.listener = new RegistroListener() {
            @Override
            public void activarRegistro(int numero) {
                if (numero==1){

                    SignupActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            onSignupSuccess();

                            progressDialog.dismiss();
                        }
                    });
                }else{
                    SignupActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            onSignupFailed();

                            progressDialog.dismiss();
                        }
                    });
                }
            }
        };

/*
        activaToast=0;

        if(activaToast==1){
           // Toast.makeText(getBaseContext(), "El registro fue exitoso", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onSignupSuccess();

                            progressDialog.dismiss();
                        }
                    }, 3000);
        }else{
          //  Toast.makeText(getBaseContext(), "Error al registrar el usuario", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                             onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }

*/
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "El registro fue exitoso", Toast.LENGTH_LONG).show();
        _nameText.setText("");
        _emailText.setText("");
        _passwordText.setText("");

        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Error de registro", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Ingrese al menos 3 caracteres");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Ingrese un correo valido");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Ingrese entre 4 y 10 caracteres alfanumericos");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
