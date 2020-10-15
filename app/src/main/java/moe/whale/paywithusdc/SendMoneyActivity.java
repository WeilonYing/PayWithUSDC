package moe.whale.paywithusdc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import moe.whale.paywithusdc.utils.Utils;

public class SendMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_send_money);
        setContentView(R.layout.activity_send_money);

        Button b = (Button) findViewById(R.id.activity_send_money_button_send);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSend();
            }
        });
    }

    private void runSend() {
        EditText amountText = (EditText) findViewById(R.id.activity_send_money_edittext_amount);
        EditText addressText = (EditText) findViewById(R.id.activity_send_money_edittext_address);

        if (amountText.getText().length() == 0 || addressText.getText().length() == 0) {
            Snackbar.make(
                    findViewById(R.id.activity_send_money_layout),
                    R.string.send_money_input_validation_snackbar,
                    Snackbar.LENGTH_LONG)
                .show();
            return;
        }

        double amount = Double.parseDouble(amountText.getText().toString());
        String address = addressText.getText().toString();
        Snackbar.make(
                findViewById(R.id.activity_send_money_layout),
                "Amount: " + amount + ", Address: " + address,
                Snackbar.LENGTH_LONG)
            .show();

        Credentials wallet = Utils.loadCredentials(getApplicationContext());
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

    }
}