package moe.whale.paywithusdc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import moe.whale.paywithusdc.contracts.IERC20;
import moe.whale.paywithusdc.contracts.SendMoneyDirect;
import moe.whale.paywithusdc.utils.Callback;
import moe.whale.paywithusdc.utils.Utils;

public class SendMoneyActivity extends AppCompatActivity {
    public static final String USDC_CONTRACT_ROPSTEN = "0x07865c6E87B9F70255377e024ace6630C1Eaa37F";
    public static final String USDC_CONTRACT_GOERLI = "0x2f3A40A3db8a7e3D09B0adfEfbCe4f6F81927557";

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

        Utils.loadCredentials(getApplicationContext(), wallet -> {
            Utils.loadWeb3(getApplicationContext(), web3j -> {
                IERC20 usdcToken = IERC20.load(USDC_CONTRACT_GOERLI, web3j, wallet, new DefaultGasProvider());
                try {
                    System.out.println("STARTING SEND");
                    BigInteger gasLimit = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                            .sendAsync()
                            .get()
                            .getBlock()
                            .getGasLimit();
                    System.out.println("GAS LIMIT IS " + gasLimit);
                    BigInteger balance = usdcToken.balanceOf(wallet.getAddress()).sendAsync().get();
                    Toast.makeText(getApplicationContext(), balance.toString(), Toast.LENGTH_LONG).show();

                    TransactionReceipt receipt = usdcToken
                            .transferFrom(
                                    wallet.getAddress(),
                                    "0x33acA7B16E4Cc9927B67E97A529dA035AE62FF16",
                                    BigInteger.valueOf(100000))
                            .send();
                    System.out.println(receipt);
                    String result = "";
                    for (Log l : receipt.getLogs()) {
                        result += l.getData() + "\n";
                    }
                    Toast.makeText(getApplicationContext(), "UWU", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

    }

    //        Utils.loadWeb3(getApplicationContext(), web3j -> {
    //
    //            SendMoneyDirect sendMoneyDirect = SendMoneyDirect.load(
    //                ,
    //                web3j,
    //                wallet,
    //                new DefaultGasProvider());
    //            sendMoneyDirect.setGasProvider(new DefaultGasProvider() {
    //                @Override
    //                public BigInteger getGasLimit(String contractFunc) {
    //                    return BigInteger.valueOf(10000000L);
    //                }
    //            });
    //            Utils.doContractFunctionCall(
    //                    sendMoneyDirect.sendCoin(
    //                            USDC_CONTRACT_ROPSTEN,
    //                            "0x33acA7B16E4Cc9927B67E97A529dA035AE62FF16",
    //                            BigInteger.valueOf(100000)),
    //                    new Callback<TransactionReceipt>() {
    //                        @Override
    //                        public void callback(TransactionReceipt receipt) {
    //                            if (receipt == null) {
    //                                Toast.makeText(getApplicationContext(), "Transaction failed", Toast.LENGTH_LONG).show();
    //                            } else {
    //                                Toast.makeText(getApplicationContext(), "Transaction appeared to work", Toast.LENGTH_LONG).show();
    //                                System.out.println(receipt);
    //                            }
    //                        }
    //                    }
    //            );
    //        });
}