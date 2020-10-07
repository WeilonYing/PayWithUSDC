package moe.whale.paywithusdc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SendMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_send_money);
        setContentView(R.layout.activity_send_money);
    }
}