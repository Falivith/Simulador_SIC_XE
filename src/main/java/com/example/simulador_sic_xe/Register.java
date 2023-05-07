package com.example.simulador_sic_xe;

import java.util.ArrayList;
import java.util.List;

public class Register {
    private int value;
    private List<RegisterListener> listeners;

    public Register() {
        value = 0;
        listeners = new ArrayList<>();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        notifyListeners();
    }

    public void addListener(RegisterListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (RegisterListener listener : listeners) {
            listener.onRegisterChange(this);
        }
    }

    public interface RegisterListener {
        void onRegisterChange(Register register);
    }
}
