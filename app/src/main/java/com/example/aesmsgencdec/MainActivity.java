package com.example.aesmsgencdec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText inputText, inputPassword;
    TextView outputText;
    Button encBtn, decBtn;
    String outputString;
    String AES="AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText=(EditText) findViewById(R.id.inputText);
        inputPassword= (EditText) findViewById(R.id.password);
        outputText= (TextView) findViewById(R.id.outputText);
        encBtn=(Button) findViewById(R.id.Encrypt);
        decBtn=(Button) findViewById(R.id.Decrypt);

        encBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    outputString = encrypt(inputText.getText().toString(), inputPassword.getText().toString());
                    outputText.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            private String encrypt(String Data, String Password) throws Exception{
                SecretKeySpec key = generateKey(Password);
                Cipher c= Cipher.getInstance(AES);
                c.init(Cipher.ENCRYPT_MODE, key);
                byte[] encVal= c.doFinal(Data.getBytes("UTF-8"));
                String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
                return encryptedValue;

            }

            private SecretKeySpec generateKey(String password) throws Exception{
                final MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] bytes = password.getBytes("UTF-8");
                digest.update(bytes, 0,bytes.length);
                byte[] key = digest.digest();
                SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
                return secretKeySpec;
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    outputString = decrypt(outputString, inputPassword.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                outputText.setText(outputString);
            }

            private String decrypt(String outputString, String password) throws Exception {
                SecretKeySpec key = generateKey(password);
                Cipher c= Cipher.getInstance(AES);
                c.init(Cipher.DECRYPT_MODE, key);
                byte[] decodedValue= Base64.decode(outputString, Base64.DEFAULT);
                byte[] decVal= c.doFinal(decodedValue);
                String decryptedValue = new String(decVal);
                return decryptedValue;
            }

            private SecretKeySpec generateKey(String password) throws Exception{
                final MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] bytes = password.getBytes("UTF-8");
                digest.update(bytes, 0,bytes.length);
                byte[] key = digest.digest();
                SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
                return secretKeySpec;
            }
        });


    }
}