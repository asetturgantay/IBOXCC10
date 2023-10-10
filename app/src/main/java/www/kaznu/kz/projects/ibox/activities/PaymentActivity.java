package www.kaznu.kz.projects.ibox.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

import www.kaznu.kz.projects.ibox.R;

public class PaymentActivity extends AppCompatActivity {

    CardForm cardForm;
    Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .saveCardCheckBoxChecked(true)
                .saveCardCheckBoxVisible(true)
//                .postalCodeRequired(true)
//                .mobileNumberRequired(true)
//                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(PaymentActivity.this);
        cardForm.getCardholderNameEditText().setAllCaps(true);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {

                }else {
                    Toast.makeText(PaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
