package com.example.simulador_sic_xe;

import java.util.List;

public class Processor {
    private Instruction instruction;
    private String addressOperando;
    private Registers registers;
    private Memory memory;
    private Loaded program;
    private List<Instruction> instructionsList;

    public Processor(Memory memory) {
        this.instruction = new Instruction();
        this.registers = new Registers();
        this.memory = memory;
    }

    public void run() {

    }

    private void decodeInstruction(){
    }

    private void executeInstruction() {
    }
}
