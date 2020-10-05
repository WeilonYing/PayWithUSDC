package moe.whale.paywithusdc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.web3j.crypto.WalletUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        // WalletUtils.generateNewWalletFile()
    }

    private void initButtons() {
        Button newWallet = findViewById(R.id.activity_main_button_new);
        newWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.common_new), Toast.LENGTH_SHORT).show();
                System.out.println("asfasdfdsfdsfsdddfdfdf;sdka");
            }
        });

        Button loadWallet = findViewById(R.id.activity_main_button_load);
        loadWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.common_load), Toast.LENGTH_SHORT).show();
                System.out.println("salfjasfjasdjfl;sdka");
            }
        });
    }
}