package com.example.simulador_sic_xe;
/* Essa classe armazena as características do programa
 * atual carregado na máquina virtual */

import java.util.List;

class Loaded {
    private String startingAddress;
    private String programSize;
    private int instructions_number;
    private List<Instruction> instructions;
    private String program_name;

    Loaded (String startAddr, String progSize, String program_name, int instrNumber, List<Instruction> instructions) {
        this.startingAddress = startAddr;
        this.programSize = progSize;
        this.instructions_number = instrNumber;
        this.instructions = instructions;
        this.program_name = program_name;
    }

    public String getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }

    public String getProgramSize() {
        return programSize;
    }

    public void setProgramSize(String programSize) {
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
