package com.example.centerprimesampleqkcsdk;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.quarkchainsdk.QKCManager;
import com.centerprime.quarkchainsdk.quarck.Numeric;
import com.centerprime.quarkchainsdk.util.BalanceUtils;
import com.example.centerprimesampleqkcsdk.databinding.ActivityCheckBalanceBinding;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CheckBalanceActivity extends AppCompatActivity {
    ActivityCheckBalanceBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_balance);

        /**
         * Using this getQCKBalance function you can check balance of provided walletAddress.
         *
         * @params walletAddress
         *
         * @return balance
         */


        QKCManager qkcManager = QKCManager.getInstance();
        qkcManager.init("http://jrpc.mainnet.quarkchain.io:38391");
        binding.checkBtn.setOnClickListener(v -> {
            String address = binding.address.getText().toString();
            if (!address.startsWith("0x")) {
                address = "0x" + address;
            }


            qkcManager.getQCKBalance(address)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(accountData -> {


                        BigInteger balance = BigInteger.ZERO;
                        if (!accountData.getPrimary().getBalances().isEmpty()) {
                            balance = Numeric.toBigInt(accountData.getPrimary().getBalances().get(0)
                                    .getBalance());
                        }
                        BigDecimal ethBalance = BalanceUtils.weiToEth(balance);
                        String pattern = "###,###.########";
                        DecimalFormat decimalFormat = new DecimalFormat(pattern);
                        System.out.println("**** **** "+decimalFormat.format(ethBalance));
                        binding.balanceTxt.setText("QKC balance: " + decimalFormat.format(ethBalance));
                     //   binding.balanceTxt.setText("QKC balance: " + accountData.getPrimary().getBalances().get(1));

                    }, error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    });
        });
    }
}
