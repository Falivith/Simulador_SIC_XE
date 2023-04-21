package com.example.simulador_sic_xe;

import java.util.List;

public class Processor {
    private Registers registers;
    private Memory memory;
    private Instruction instruction;
    private String addressOperando;

    private Loaded program;
    private List<Instruction> instructionsList;

    public Processor(Memory memory, Registers registers) {
        this.instruction = new Instruction();
        this.registers = registers;
        this.memory = memory;
    }

    public void run() {

    }

    private void decodeInstruction(){
    }

    private void executeInstruction() {
    }
}
