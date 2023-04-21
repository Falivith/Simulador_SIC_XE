package com.example.simulador_sic_xe;
/* Essa classe armazena as características do programa
 * atual carregado na máquina virtual */

import java.util.List;

class Loaded {
    private int startingAddress;
    private int programSize;
    private int instructions_number;
    private List<Instruction> instructions;
    private String program_name;

    Loaded (int startAddr, int progSize, String program_name, int instrNumber, List<Instruction> instructions) {
        this.startingAddress = startAddr;
        this.programSize = progSize;
        this.instructions_number = instrNumber;
        this.instructions = instructions;
        this.program_name = program_name;
    }

    public int getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(int startingAddress) {
        this.startingAddress = startingAddress;
    }

    public int getProgramSize() {
        return programSize;
    }

    public void setProgramSize(int programSize) {
        this.programSize = programSize;
    }

    public int getInstructions_number() {
        return instructions_number;
    }

    public void setInstructions_number(int instructions_number) {
        this.instructions_number = instructions_number;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }
}
