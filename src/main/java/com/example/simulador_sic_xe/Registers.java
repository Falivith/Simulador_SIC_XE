package com.example.simulador_sic_xe;

import java.util.ArrayList;
import java.util.List;

public class Registers {
    private int A; // Acumulador
    private int X; // Registrador de índice
    private int L; // Registrador de ligação
    private int B; // Registrador Base
    private int S; // Registrador de uso geral
    private int T; // Registrador de uso geral
    private int PC; // Contador de Instruções (Program Counter)
    private int SW; // Palavra de status

    private List<RegistersListener> listeners;

    public Registers() {
        // inicializa os registradores com valor 0
        this.A = 0;
        this.X = 0;
        this.L = 0;
        this.B = 0;
        this.S = 0;
        this.T = 0;
        this.PC = 0;
        this.SW = 0;
        listeners = new ArrayList<>();
    }

    public int getA() {
        return A;
    }

    public int getX() {
        return X;
    }

    public int getL() {
        return L;
    }

    public int getB() {
        return B;
    }

    public int getS() {
        return S;
    }

    public int getT() {
        return T;
    }

    public int getPC() {
        return PC;
    }

    public int getSW() {
        return SW;
    }

    public void setA(int a) {
        this.A = a;
        notifyListeners();
    }

    public void setX(int x) {
        this.X = x;
        notifyListeners();
    }

    public void setL(int l) {
        this.L = l;
        notifyListeners();
    }

    public void setB(int b) {
        this.B = b;
        notifyListeners();
    }

    public void setS(int s) {
        this.S = s;
        notifyListeners();
    }

    public void setT(int t) {
        this.T = t;
        notifyListeners();
    }

    public void setPC(int pc) {
        this.PC = pc;
        notifyListeners();
    }

    public void setSW(int sw) {
        this.SW = sw;
        notifyListeners();
    }

    public void reset(){
        this.A = 0;
        this.X = 0;
        this.L = 0;
        this.B = 0;
        this.S = 0;
        this.T = 0;
        this.PC = 0;
        this.SW = 0;
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