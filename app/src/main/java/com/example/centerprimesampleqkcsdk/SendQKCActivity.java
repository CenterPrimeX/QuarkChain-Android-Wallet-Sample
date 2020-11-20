package com.example.centerprimesampleqkcsdk;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.quarkchainsdk.QKCManager;
import com.example.centerprimesampleqkcsdk.databinding.ActivitySendQkcBinding;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SendQKCActivity extends AppCompatActivity {

    ActivitySendQkcBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_qkc);
    /**
     * Using this sendEther function you can send ethereum from walletAddress to another walletAddress.
     *
     * @params senderWalletAddress, password, gasPrice, gasLimit, etherAmount, receiverWalletAddress, Context
     *
     * @return transactionHash
     */


        QKCManager qkcManager = QKCManager.getInstance();
        qkcManager.init("http://jrpc.mainnet.quarkchain.io:38391");


        binding.sendBNB.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.address.getText().toString().trim()) && !TextUtils.isEmpty(binding.ethAmount.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.gasLimit.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.receiverAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {

                String walletAddress = binding.address.getText().toString();
                String password = binding.password.getText().toString();
                //    BigInteger gasPrice = new BigInteger(String.valueOf(ethManager.getGasPrice()));
                BigInteger gasPrice = new BigInteger("30000000000");
                BigInteger gasLimit = new BigInteger(binding.gasLimit.getText().toString());
                BigDecimal etherAmount = new BigDecimal(binding.ethAmount.getText().toString().trim());
                String receiverAddress = binding.receiverAddress.getText().toString().trim();
/*
                binanceManager.sendQCKToken(walletAddress, password, gasPrice, gasLimit, etherAmount, receiverAddress, this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tx -> {

                            Toast.makeText(this, "TX : " + tx, Toast.LENGTH_SHORT).show();

                        }, error -> {

                            binding.result.setText(error.getMessage() + ". Please check balance of provided walletaddress!");

                            error.printStackTrace();
                            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        });*/
            }

        });
    }
}
