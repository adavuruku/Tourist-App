package com.example.tourist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;


public class register extends Fragment implements Validator.ValidationListener{

    @NotEmpty
    @Password
    @Length(min = 6)
    EditText passwordE;

    @NotEmpty
    @ConfirmPassword
    EditText confirmPasswordE;

    @NotEmpty
    EditText fullnameE;

    @NotEmpty
    @Email
    EditText emailE;

    private Validator validator;
    dbHelper dbHelper;
    Button submit;
    AlertDialog.Builder builder;
    String fullname, email, password;
    private SharedPreferences LoginUserEmail;

    public register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LoginUserEmail = getActivity().getSharedPreferences("LoginUserEmail", getActivity().MODE_PRIVATE);

        validator = new Validator(this);
        validator.setValidationListener(this);

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        passwordE = rootView.findViewById(R.id.password);
        confirmPasswordE = rootView.findViewById(R.id.confirm);
        emailE = rootView.findViewById(R.id.email);
        fullnameE = rootView.findViewById(R.id.user);

        dbHelper = new dbHelper(getActivity());

        submit = rootView.findViewById(R.id.register);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = fullnameE.getText().toString();
                email = emailE.getText().toString();
                password = passwordE.getText().toString();

                validator.validate();
            }
        });

        return rootView;
    }

    public void displayMessage(String msg) {

        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }
    @Override
    public void onValidationSucceeded() {
        dbHelper.openDataBase();
        if(dbHelper.SaveUserInformation(fullname, email, password)){
            SharedPreferences.Editor editor;
            editor = LoginUserEmail.edit();
            editor.putString("LoginUserEmail",email);
            editor.apply();

            Toast.makeText(getActivity(),"Welcome "+ fullname + " To Tourist Guide - MOBILE APP",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(),OgunHome.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            getActivity().finish();
        }else{
            displayMessage("Fail To Register Account \nEmail Already Exist !");
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                displayMessage(message);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
