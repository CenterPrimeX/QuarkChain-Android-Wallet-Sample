package com.example.centerprimesampleqkcsdk;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.quarkchainsdk.QKCManager;
import com.example.centerprimesampleqkcsdk.databinding.ActivitySendQkcBinding;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendQKCActivity extends AppCompatActivity {

    ActivitySendQkcBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_qkc);
    /**
     * Using this sendQCKToken function you can send QKC from walletAddress to another walletAddress.
     *
     * @params senderWalletAddress, password, gasPrice, gasLimit, qkcAmount, receiverWalletAddress, Context
     *
     * @return transactionHash
     */


        QKCManager qkcManager = QKCManager.getInstance();
        qkcManager.init("http://jrpc.mainnet.quarkchain.io:38391");


        binding.sendQKC.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.qkcAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.ethAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.qkcAmount.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.gasLimit.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.receiverAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {

                String walletAddress = binding.qkcAddress.getText().toString();
                String ethAddress = binding.ethAddress.getText().toString();
                String password = binding.password.getText().toString();
                //    BigInteger gasPrice = new BigInteger(String.valueOf(ethManager.getGasPrice()));
                BigInteger gasPrice = new BigInteger("30000000000");
                BigInteger gasLimit = new BigInteger(binding.gasLimit.getText().toString());
                BigDecimal qkcAmount = new BigDecimal(binding.qkcAmount.getText().toString().trim());
                String receiverAddress = binding.receiverAddress.getText().toString().trim();


                qkcManager.sendQCKToken(ethAddress, walletAddress, password, walletAddress, receiverAddress,gasPrice, gasLimit, qkcAmount.toString(), this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tx -> {

                            Toast.makeText(this, "TX : " + tx, Toast.LENGTH_SHORT).show();

                        }, error -> {
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        });

            }

        });
    }
}
