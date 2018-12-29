package com.goandroytech.www.rahisipay.Connection;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyTextWatcher implements TextWatcher {

    private final WeakReference<EditText> editTextWeakReference;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String s = editable.toString();
        if (s.isEmpty()) return;
        editText.removeTextChangedListener(this);
        String cleanString = s.replaceAll("[$,]", "");
        DecimalFormat format = new DecimalFormat("#,##0");
        BigDecimal bBalance = new BigDecimal(cleanString);
        //BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        //String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        String formattedBalance = format.format(bBalance);
        editText.setText(formattedBalance);
        editText.setSelection(formattedBalance.length());
        editText.addTextChangedListener(this);
    }
}