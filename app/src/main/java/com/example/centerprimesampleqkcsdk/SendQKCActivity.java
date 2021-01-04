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

        QKCManager qkcManager = QKCManager.getInstance();
        /**
         * @param infura - Initialize infura
         */
        qkcManager.init("http://jrpc.mainnet.quarkchain.io:38391");

        binding.sendQKC.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.qkcAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.ethAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.qkcAmount.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.gasLimit.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.receiverAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {

                /**
                 * Using this sendQCKToken function you can send ethereum from walletAddress to another walletAddress.
                 *
                 * @param senderWalletAddress - must be provided sender's wallet address
                 * @param password - User must enter password of wallet address
                 * @param gasPrice - gas price: 30000000000
                 * @param gasLimit - gas limit atleast 21000 or more
                 * @param qkcAmount - amount of QKC which user want to send
                 * @param receiverWalletAddress - wallet address which is user want to send QKC
                 * @param Context - activity context
                 *
                 * @return if sending completes successfully the function returns transactionHash or returns error name
                 */
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
                            /**
                             * if function successfully completes result can be caught in this block
                             */
                            Toast.makeText(this, "TX : " + tx, Toast.LENGTH_SHORT).show();
                            binding.result.setText("TX: " + tx);

                        }, error -> {
                            /**
                             * if function fails error can be caught in this block
                             */
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        });

            }

        });
    }
}
