package com.example.centerprimesampleqkcsdk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.quarkchainsdk.QKCManager;
import com.example.centerprimesampleqkcsdk.databinding.ActivityCreateWalletBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletActivity extends AppCompatActivity {
    ActivityCreateWalletBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);

        /**
         * Using this createWallet function user can create a wallet.
         *
         * @params password, Context
         *
         * @return walletAddress
         */

        QKCManager qkcManager = new QKCManager();
        qkcManager.init("http://jrpc.mainnet.quarkchain.io:38391");

        binding.createWallet.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(binding.password.getText().toString()) && !TextUtils.isEmpty(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().trim().length() >= 6
                    && binding.confirmPassword.getText().toString().trim().length() >= 6) {

                qkcManager.createWallet(binding.password.getText().toString(), this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(wallet -> {

                            String walletAddress = wallet.getAddress();
                            String qckWalletAddress = qkcManager.
                                    getQCKAddress(walletAddress,this);

                     //       binding.address.setText("0x" + wallet.getAddress());
                            binding.qckAddress.setText(qckWalletAddress);
                            binding.ethAddress.setText(walletAddress);

                            binding.copyEth.setVisibility(View.VISIBLE);
                            binding.copyQCK.setVisibility(View.VISIBLE);

                        }, error -> {
                            System.out.println(error);
                        });
            } else {
                Toast.makeText(this, "Please insert password correctly.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.copyQCK.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.qckAddress.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });

        binding.copyEth.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.ethAddress.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });

    }
}
