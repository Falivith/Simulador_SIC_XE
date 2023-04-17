package com.example.simulador_sic_xe;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    private int size;
    private byte[] memoryTable;
    private List<MemoryListener> listeners;

    public Memory(int size) {
        this.size = size;
        memoryTable = new byte[size];
        listeners = new ArrayList<>();
    }

    public byte read(int address) {
        if (address >= 0 && address < size) {
            return memoryTable[address];
        } else {
            throw new IllegalArgumentException("Endereço de memória inválido.");
        }
    }

    public void write(int address, byte value) {
        if (address >= 0 && address < size) {
            memoryTable[address] = value;
            fireMemoryWrite(address, value);
        } else {
            throw new IllegalArgumentException("Endereço de memória inválido.");
        }
    }

    public int getSize(){
        return size;
    }

    public void addListener(MemoryListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MemoryListener listener) {
        listeners.remove(listener);
    }

    private void fireMemoryWrite(int address, byte value) {
        for (MemoryListener listener : listeners) {
            listener.onMemoryWrite(address, value);
        }
    }

    public interface MemoryListener {
        void onMemoryWrite(int address, byte value);
    }
}