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
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import moe.whale.paywithusdc.contracts.IERC20;
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

        Utils.loadCredentials(getApplicationContext(), wallet -> {
            TextView addressView = (TextView) findViewById(R.id.activity_balance_address);
            String address = wallet.getAddress();
            addressView.setText("Your address is " + address);

            // load eth balance
            Utils.loadWeb3(getApplicationContext(), web3j -> {
                Request<?, EthGetBalance> getBalanceRequest =
                        web3j.ethGetBalance(wallet.getAddress(), DefaultBlockParameterName.LATEST);
                try {
                    BigInteger balance = getBalanceRequest.sendAsync().get().getBalance();
                    balance = balance.divide(BigInteger.valueOf((long) Math.pow(10, 14)));
                    BigDecimal bal = BigDecimal.valueOf(balance.longValue());
                    bal = bal.divide(BigDecimal.valueOf(Math.pow(10, 4)));
                    TextView balanceView = (TextView) findViewById(R.id.activity_balance_account_balance);
                    balanceView.setText("Your ETH balance is: " + bal.toPlainString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });

            // load usdc balance
            Utils.loadWeb3(getApplicationContext(), web3j -> {
                IERC20 usdcToken = IERC20.load(SendMoneyActivity.USDC_CONTRACT_GOERLI, web3j, wallet, new DefaultGasProvider());
                try {
                    BigInteger balance = usdcToken.balanceOf(wallet.getAddress()).sendAsync().get();
                    balance = balance.divide(BigInteger.valueOf((long) Math.pow(10, 3)));
                    BigDecimal bal = BigDecimal.valueOf(balance.longValue());
                    bal = bal.divide(BigDecimal.valueOf(Math.pow(10, 3)));
                    TextView balanceView = (TextView) findViewById(R.id.activity_balance_usdc_balance);
                    balanceView.setText("Your USDC balance is: $" + bal.toPlainString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startBalanceActivity());
    }

    private void startBalanceActivity() {
        Intent i = new Intent(this, SendMoneyActivity.class);
        startActivity(i);

    }
}