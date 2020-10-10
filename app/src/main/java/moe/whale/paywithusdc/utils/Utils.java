package moe.whale.paywithusdc.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;

import moe.whale.paywithusdc.BalanceActivity;
import moe.whale.paywithusdc.R;

public class Utils {
    private static Credentials mCredentials = null;
    private static Web3j mWeb3j = null;

    public static Credentials loadCredentials(Context context) {
        try {
            if (mCredentials == null) {
                File walletDir = new File(context.getFilesDir(), "wallet");
                String[] files = walletDir.list();
                    assert files != null;
                    File wallet = new File(walletDir, files[0]);
                    mCredentials = WalletUtils.loadCredentials("password", wallet);
                    Toast.makeText(context, "Wallet load successful", Toast.LENGTH_SHORT).show();
            }
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(context, "No wallet found", Toast.LENGTH_SHORT).show();
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(context, "No wallet found", Toast.LENGTH_SHORT).show();
        }

        return mCredentials;
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

