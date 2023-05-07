package com.example.simulador_sic_xe;
/* Essa classe armazena as características do programa
 * atual carregado na máquina virtual */

import java.util.List;

class Loaded {
    private int startingAddress;
    private int instructions_number;
    private List<LineDecode> instructions;
    private List<String> objCode;
    private List<String> assembly;
    private String program_name;

    Loaded (int startAddr, String program_name, int instrNumber, List<LineDecode> instructions, List<String> objCode, List<String> asm) {
        this.startingAddress = startAddr;
        this.instructions_number = instrNumber;
        this.instructions = instructions;
        this.program_name = program_name;
        this.objCode = objCode;
        this.assembly = asm;
    }

    Loaded (){

    }

    public List<String> getAssembly() {
        return assembly;
    }

    public void setAssembly(List<String> assembly) {
        this.assembly = assembly;
    }

    public List<String> getObjCode() {
        return objCode;
    }

    public void setObjCode(List<String> objCode) {
        this.objCode = objCode;
    }

    public int getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(int startingAddress) {
        this.startingAddress = startingAddress;
    }
    public int getInstructions_number() {
        return instructions_number;
    }

    public void setInstructions_number(int instructions_number) {
        this.instructions_number = instructions_number;
    }

    public List<LineDecode> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<LineDecode> instructions) {
        this.instructions = instructions;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }
}
