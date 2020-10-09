package moe.whale.paywithusdc.utils;

import android.content.Context;
import android.content.Intent;
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

    public static Web3j loadWeb3(Context context) {
        if (mWeb3j == null) {
            mWeb3j = Web3j.build(
                new HttpService(
                context.getString(R.string.common_infura_mainnet_url) +
                    context.getString(R.string.secret_infura_project_id)));
        }

        return mWeb3j;
    }
}
