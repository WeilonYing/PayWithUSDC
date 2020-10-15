package moe.whale.paywithusdc;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import moe.whale.paywithusdc.utils.Callback;
import moe.whale.paywithusdc.utils.Utils;

public class BalanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        Credentials wallet = Utils.loadCredentials(getApplicationContext());
        TextView addressView = (TextView) findViewById(R.id.activity_balance_address);
        String address = wallet.getAddress();
        System.out.println(wallet.getEcKeyPair().getPrivateKey());
        addressView.setText(address);
        System.out.println(address);
        Utils.loadWeb3(getApplicationContext(), web3j -> {
            Request<?, EthGetBalance> getBalanceRequest =
                web3j.ethGetBalance(wallet.getAddress(), DefaultBlockParameterName.LATEST);
            try {
                BigInteger balance = getBalanceRequest.sendAsync().get().getBalance();
                Toast.makeText(getApplicationContext(), balance.toString(), Toast.LENGTH_LONG).show();
                TextView balanceView = (TextView) findViewById(R.id.activity_balance_account_balance);
                balanceView.setText(balance.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBalanceActivity();
            }
        });

    }

    private void startBalanceActivity() {
        Intent i = new Intent(this, SendMoneyActivity.class);
        startActivity(i);

    }
}