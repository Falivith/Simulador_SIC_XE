package com.example.simulador_sic_xe;

public class Processor {
    Memory memory;
    public Processor() {
        memory = new Memory(1024);
    }
    public Memory getMemory() {
        return memory;
    }
}
