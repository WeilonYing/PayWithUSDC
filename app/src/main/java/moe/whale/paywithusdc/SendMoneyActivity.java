package moe.whale.paywithusdc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

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

    }
}