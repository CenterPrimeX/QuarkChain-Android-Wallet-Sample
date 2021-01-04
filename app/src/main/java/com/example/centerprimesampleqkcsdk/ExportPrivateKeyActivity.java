package com.example.centerprimesampleqkcsdk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.quarkchainsdk.QKCManager;
import com.example.centerprimesampleqkcsdk.databinding.ActivityExportPrivateKeyBinding;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExportPrivateKeyActivity extends AppCompatActivity {
    ActivityExportPrivateKeyBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_export_private_key);

        QKCManager qkcManager = QKCManager.getInstance();
        /**
         * @param infura - Initialize infura
         */
        qkcManager.init("http://jrpc.mainnet.quarkchain.io:38391");

        binding.button.setOnClickListener(v -> {

            String walletAddress = binding.address.getText().toString();
            if(walletAddress.startsWith("0x")){
                walletAddress = walletAddress.substring(2);
            }
            /**
             * Using this exportPrivateKey function user can export walletAddresses privateKey.
             *
             * @params walletAddress, password, Context
             *
             * @return privateKey
             */
            String password = binding.password.getText().toString();
            qkcManager.getPrivateKeyForQW(walletAddress, password,this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(privateKey -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        Pair<BigInteger, BigInteger> privateKeyT = privateKey;
                        binding.privateKey.setText(privateKeyT.first.toString(16));
                        binding.copy.setVisibility(View.VISIBLE);

                    }, error -> {
                        /**
                         * if function fails error can be caught in this block
                         */
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.privateKey.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });

    }
}
