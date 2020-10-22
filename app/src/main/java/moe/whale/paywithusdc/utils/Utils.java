package moe.whale.paywithusdc.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.FileUtils;
import android.widget.Toast;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import moe.whale.paywithusdc.R;

public class Utils {
    private static Credentials mCredentials = null;
    private static Web3j mWeb3j = null;

    public static void loadCredentials(Context context, Callback<Credentials> onCredentialsLoadedCallback) {
        new AsyncTask<Void, Void, Credentials>() {
            @Override
            protected Credentials doInBackground(Void... voids) {
                try {
                    if (mCredentials == null) {
                        File walletDir = new File(context.getFilesDir(), "wallet");
                        String[] files = walletDir.list();
                        assert files != null;
                        File wallet = new File(walletDir, files[0]);
                        mCredentials = WalletUtils.loadCredentials("password", wallet);
                        Toast.makeText(context, "Wallet load successful", Toast.LENGTH_SHORT).show();

                        // DEBUG
//                    File extPath = new File("/storage/emulated/0/Download");
//                    File externalFile = new File(extPath, "wallet.json");
//                    System.out.println(externalFile.toPath());
//                    externalFile.createNewFile();
//                    FileUtils.copy(new FileInputStream(wallet), new FileOutputStream(externalFile));
                    }
                } catch (CipherException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Toast.makeText(context, "No wallet found", Toast.LENGTH_SHORT).show();
                } catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(context, "No wallet found", Toast.LENGTH_SHORT).show();
                } finally {
                    return mCredentials;
                }
            }

            @Override
            protected void onPostExecute(Credentials credentials) {
                onCredentialsLoadedCallback.callback(credentials);
            }
        }.execute();
    }

    public static void unloadCredentials() {
        mCredentials = null;
    }

    public static void loadWeb3(Context context, Callback<Web3j> onWeb3jLoadedCallback) {
        if (mWeb3j == null) {
            String url =
                context.getString(R.string.common_infura_mainnet_url) +
                context.getString(R.string.secret_infura_project_id);

            new Web3LoadTask(
                new Callback<Web3j>() {
                    @Override
                    public void callback(Web3j web3j) {
                        mWeb3j = web3j;
                        onWeb3jLoadedCallback.callback(web3j);
                    }
                }
            ).execute(url);
            return;
        }

        onWeb3jLoadedCallback.callback(mWeb3j);
    }

    public static void getEstimatedGasPrice(Context context, Transaction transaction) {
        loadWeb3(context, web3j -> new AsyncTask<Web3j, Void, BigInteger>() {
            @Override
            protected BigInteger doInBackground(Web3j... web3js) {
                try {
                    EthEstimateGas estimate = web3js[0].ethEstimateGas(transaction).send();
                    return estimate.getAmountUsed();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute(web3j));
    }

    public static void doContractFunctionCall(
            RemoteFunctionCall<TransactionReceipt> functionCall,
            Callback<TransactionReceipt> callback) {
        new ContractFunctionTask(callback).execute(functionCall);
    }
}

class ContractFunctionTask
        extends AsyncTask<RemoteFunctionCall<TransactionReceipt>, Void, TransactionReceipt> {
    private Callback<TransactionReceipt> mCallback;

    public ContractFunctionTask(Callback<TransactionReceipt> callback) {
        mCallback = callback;
    }

    @Override
    protected TransactionReceipt doInBackground(RemoteFunctionCall<TransactionReceipt>... remoteFunctionCalls) {
        try {
            return remoteFunctionCalls[0].send();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

class Web3LoadTask extends AsyncTask<String, Void, Web3j> {
    private Callback<Web3j> mCallback;

    public Web3LoadTask(Callback<Web3j> callback) {
        super();
        mCallback = callback;
    }

    @Override
    protected Web3j doInBackground(String... urls) {
        return Web3j.build(new HttpService(urls[0]));
    }

    @Override
    protected void onPostExecute(Web3j web3j) {
        mCallback.callback(web3j);
    }
}

