package moe.whale.paywithusdc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

import moe.whale.paywithusdc.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBouncyCastle();
        setContentView(R.layout.activity_main);
        initButtons();
        verifyAppPermissions();
    }

    private void initButtons() {
        Button newWallet = findViewById(R.id.activity_main_button_new);
        newWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewWallet();
            }
        });

        final Button loadWallet = findViewById(R.id.activity_main_button_load);
        loadWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWallet();
            }
        });
    }

    private void createNewWallet() {
        DEBUG_createNewWalletFile();
        String[] files = new File(getApplicationContext().getFilesDir(), "wallet").list();
        for (String s : files) {
            System.out.println("Files: " + s);
        }
    }

    private void loadWallet() {
        Credentials wallet = Utils.loadCredentials(getApplicationContext());
        if (wallet != null) {
            Intent balanceIntent = new Intent(this, BalanceActivity.class);
            startActivity(balanceIntent);
        }
    }

    private void startLoadWalletFilePicker() {
        DEBUG_createNewWalletFile();
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");

        startActivityForResult(i, 2);
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    private void DEBUG_createNewWalletFile() {
        try {
            File walletDirectory = new File(getApplicationContext().getFilesDir(), "/wallet");

            // Create a new wallet directory if not exists, and throw an IO exception if that fails.
            if (!walletDirectory.exists() && !walletDirectory.mkdir()) {
                throw new IOException("Unable to create wallet directory");
            }

            String[] children = walletDirectory.list();
            if (children != null) {
                for (String c : children) {
                    new File(walletDirectory, c).delete();
                }
            }

            WalletUtils.generateNewWalletFile("password", walletDirectory);
            children = walletDirectory.list();
            if (children != null && children.length > 0) {
                File renamedFile = new File(walletDirectory, "wallet.json");
                new File(walletDirectory, children[0]).renameTo(renamedFile);
            }
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyAppPermissions() {
        // Check if we have write permission
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (checkSelfPermission(Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.INTERNET);
        }

        if (permissionsToRequest.size() > 0) {
            String[] permissions = new String[permissionsToRequest.size()];
            permissionsToRequest.toArray(permissions);
            requestPermissions(permissions, 1);
        }
    }
}