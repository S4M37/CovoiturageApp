package tn.iac.mobiledevelopment.covoiturageapp;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    protected TextInputLayout firstName = null ;
    protected TextInputLayout lastName = null ;
    protected TextInputLayout email = null ;
    protected TextInputLayout emailConfirmed = null ;
    protected TextInputLayout password = null ;
    protected TextInputLayout passwordConfirmed = null ;
    protected ImageView inscrptionButton= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        firstName = (TextInputLayout) findViewById(R.id.layoutFirstName);
        lastName = (TextInputLayout) findViewById(R.id.layoutLastName);
        email = (TextInputLayout) findViewById(R.id.layoutEmail);
        emailConfirmed = (TextInputLayout) findViewById(R.id.layoutEmailConfirmed);
        password = (TextInputLayout) findViewById(R.id.layoutPassword);
        passwordConfirmed = (TextInputLayout) findViewById(R.id.layoutPasswordConfirmed);
        inscrptionButton = (ImageView)findViewById(R.id.inscrptionButton);

        inscrptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameValue = firstName.getEditText().getText().toString();
                String lastNameValue = lastName.getEditText().getText().toString();
                String emailValue = email.getEditText().getText().toString();
                String emailConfirmedValue = emailConfirmed.getEditText().getText().toString();
                String passwordValue = password.getEditText().getText().toString();
                String passwordConfirmedValue = passwordConfirmed.getEditText().getText().toString();

                firstName.setErrorEnabled(false);
                firstName.requestFocus();
                lastName.setErrorEnabled(false);
                lastName.requestFocus();
                email.setErrorEnabled(false);
                email.requestFocus();
                emailConfirmed.setErrorEnabled(false);
                emailConfirmed.requestFocus();
                password.setErrorEnabled(false);
                password.requestFocus();
                passwordConfirmed.setErrorEnabled(false);
                passwordConfirmed.requestFocus();

                if (!validateInput(firstNameValue)) {
                    firstName.setError("nom non valide !");
                    firstName.requestFocus();

                } else if (!validateInput(lastNameValue)) {
                    lastName.setError("prénom non valide !");
                    lastName.requestFocus();
                } else if (!validateEmail(emailValue)) {
                    email.setError("Email non valide !");
                    email.requestFocus();
                } else if (!validateEmail(emailConfirmedValue)) {
                    emailConfirmed.setError("Email non valide !");
                    emailConfirmed.requestFocus();
                } else if (!(emailValue.equals(emailConfirmedValue))) {
                    email.setError("Les deux e-mails ne sont pas identiques");
                    emailConfirmed.setError("Les deux e-mails ne sont pas identiques");
                    emailConfirmed.requestFocus();
                } else if (!validatePassword(passwordValue)) {
                    password.setError("Mot de passe non valide !");
                    password.requestFocus();
                } else if (!validateInput(passwordConfirmedValue)) {
                    passwordConfirmed.setError("Mot de passe non valide !");
                    passwordConfirmed.requestFocus();
                } else if (!(passwordValue.equals(passwordConfirmedValue))) {
                    password.setError("Les deux mots de passe ne sont pas identiques");
                    passwordConfirmed.setError("Les deux mots de passe ne sont pas identiques");
                    passwordConfirmed.requestFocus();
                } else {
                    hideKeyboard();
                    doInscri();
                }
            }
        });
    }


    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean validatePassword(String password) {
        return password.length()>6;
    }

    public boolean validateInput(String input) {
        return input.length()>3;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    private void doInscri(){
        Toast.makeText(getBaseContext(), "hi bb", Toast.LENGTH_LONG).show();
    }

}
