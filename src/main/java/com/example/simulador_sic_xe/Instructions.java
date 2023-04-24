package com.example.simulador_sic_xe;

public class Instructions {

    // Tipo 3
    public static void ADD(Registers registers, Memory memory, int address) {
        registers.setA(memory.read(address));
    }

    public static void AND(Registers registers, Memory memory, int address) {
        byte[] memValue = extractWordFromAddress(memory, address);
        byte[] regValue = extractWordFromInt(registers.getA().getValue());
        byte[] result;

        // Implementar
        registers.setA(memory.read(address));
    }

    public static void COMP(Registers registers, Memory memory, int address){
        if(registers.getA().getValue() == memory.read(address)){
            registers.setSW((int)'=');
            return;
        }
        else if(registers.getA().getValue() < memory.read(address)){
            registers.setSW((int)'<');
        }else{
            registers.setSW((int)'>');
        }
    }

    public static void DIV(Registers registers, Memory memory, int address){
        Register A = registers.getA();
        A.setValue(A.getValue() / memory.read(address));
    }

    public static void J(Registers registers, Memory memory, int address){
        Register PC = registers.getPC();
        PC.setValue(memory.read(address));
    }

    public static void JEQ(Registers registers, Memory memory, int address){
        Register SW = registers.getSW();
        Register PC = registers.getPC();
        if(SW.getValue() == (int) '='){
            PC.setValue(address);
        }
    }

    public static void JGT(Registers registers, Memory memory, int address){
        Register SW = registers.getSW();
        Register PC = registers.getPC();
        if(SW.getValue() == (int) '>'){
            PC.setValue(address);
        }
    }

    public static void JLT(Registers registers, Memory memory, int address){
        Register SW = registers.getSW();
        Register PC = registers.getPC();
        if(SW.getValue() == (int) '<'){
            PC.setValue(address);
        }
    }

    public static void JSUB(Registers registers, Memory memory, int address){
        Register SW = registers.getSW();
        Register PC = registers.getPC();
        if(SW.getValue() == (int) '<'){
            PC.setValue(address);
        }
    }

    // Tipo 2
    public static void ADDR(Register R2, Register R1) {
        R2.setValue(R1.getValue() + R2.getValue());
    }

    public static void CLEAR(Register R1){
        R1.setValue(0);
    }

    public static void COMPR(Register R1, Register R2, Register SW) {
        if(R1.getValue() == R2.getValue()){
            SW.setValue((int)'=');
        }
        else if(R1.getValue() < R2.getValue()){
            SW.setValue((int)'<');
        }else{
            SW.setValue((int)'>');
        }
    }

    public static void DIVR(Register R1, Register R2){
        R1.setValue(R1.getValue() / R2.getValue());
    }


    private static byte[] extractWordFromInt(int num){
        byte[] word = new byte[3];
        word[0] = (byte) (num >> 16 & 0xFF);
        word[1] = (byte) (num >> 8 & 0xFF);
        word[2] = (byte) (num & 0xFF);
        return word;
    }

    private static byte[] extractWordFromAddress(Memory memory, int address){
        byte[] word = new byte[3];
        word[0] = memory.read(address);
        word[1] = memory.read(address + 1);
        word[2] = memory.read(address + 2);
        return word;
    }
}
