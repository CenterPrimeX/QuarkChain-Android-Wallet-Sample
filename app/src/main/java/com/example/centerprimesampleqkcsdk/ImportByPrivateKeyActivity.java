package com.example.centerprimesampleqkcsdk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.quarkchainsdk.QKCManager;
import com.example.centerprimesampleqkcsdk.databinding.ActivityImportPrivateKeyBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportByPrivateKeyActivity extends AppCompatActivity {
    ActivityImportPrivateKeyBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_import_private_key);

        QKCManager qkcManager = QKCManager.getInstance();
        /**
         * @param infura - Initialize infura
         */
        qkcManager.init("http://jrpc.mainnet.quarkchain.io:38391");

        binding.checkBtn.setOnClickListener(v -> {
            /**
             * Using this importFromPrivateKey function user can import his wallet from its private key.
             *
             * @params privateKey, Context
             *
             * @return walletAddress
             */
            String privateKey = binding.privateKey.getText().toString();
            qkcManager.importFromPrivateKey(privateKey, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(walletAddress -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        binding.address.setText("0x" + walletAddress);
                        binding.copyBtn.setVisibility(View.VISIBLE);

                    }, error -> {
                        /**
                         * if function fails error can be caught in this block
                         */
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.copyBtn.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.address.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });
    }
}
