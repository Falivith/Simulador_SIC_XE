package com.example.simulador_sic_xe;

import java.util.ArrayList;
import java.util.List;

class Register {
    int value;

    Register(){
        value = 0;
    }

    void setValue(int v){
        value = v;
    }

    int getValue(){
        return value;
    }
}

public class Registers {
    private Register A; // Acumulador
    private Register X; // Registrador de índice
    private Register L; // Registrador de ligação
    private Register B; // Registrador Base
    private Register S; // Registrador de uso geral
    private Register T; // Registrador de uso geral
    private Register PC; // Contador de Instruções (Program Counter)
    private Register SW; // Palavra de status

    private List<RegistersListener> listeners;

    public Registers() {
        // inicializa os registradores com valor 0
        this.A = new Register();
        this.X = new Register();
        this.L = new Register();
        this.B = new Register();
        this.S = new Register();
        this.T = new Register();
        this.PC = new Register();
        this.SW = new Register();
        listeners = new ArrayList<>();
    }

    public Register getA() {
        return A;
    }

    public Register getX() {
        return X;
    }

    public Register getL() {
        return L;
    }

    public Register getB() {
        return B;
    }

    public Register getS() {
        return S;
    }

    public Register getT() {
        return T;
    }

    public Register getPC() {
        return PC;
    }

    public Register getSW() {
        return SW;
    }

    public void setA(int a) {
        this.A.setValue(a);
        notifyListeners();
    }

    public void setX(int x) {
        this.X.setValue(x);
        notifyListeners();
    }

    public void setL(int l) {
        this.L.setValue(l);
        notifyListeners();
    }

    public void setB(int b) {
        this.B.setValue(b);
        notifyListeners();
    }

    public void setS(int s) {
        this.S.setValue(s);
        notifyListeners();
    }

    public void setT(int t) {
        this.T.setValue(t);
        notifyListeners();
    }

    public void setPC(int pc) {
        this.PC.setValue(pc);
        notifyListeners();
    }

    public void setSW(int sw) {
        this.SW.setValue(sw);
        notifyListeners();
    }

    public void reset(){
        this.A.setValue(0);
        this.X.setValue(0);
        this.L.setValue(0);
        this.B.setValue(0);
        this.S.setValue(0);
        this.T.setValue(0);
        this.PC.setValue(0);
        this.SW.setValue(0);
    }

    public interface RegistersListener {
        void onRegistersChange(Registers registers);
    }

    public void addListener(RegistersListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        if (listeners != null) {
            for (RegistersListener listener : listeners) {
                listener.onRegistersChange(this);
            }
        }
    }
}