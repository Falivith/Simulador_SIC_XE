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


        registers.setA(memory.read(address));
    }

    // Tipo 2
    public static void ADDR(Register R2, Register R1) {
        R2.setValue(R1.getValue() + R2.getValue());
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
