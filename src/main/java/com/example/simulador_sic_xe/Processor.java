package com.example.simulador_sic_xe;

import java.util.HashMap;
import java.util.List;

public class Processor {
    private Registers registers;
    private Memory memory;
    private HashMap<String, List<String>> instructionMap;
    private String firstByte, secondByte, thirdByte, fourthByte;
    private byte BfirstByte, BsecondByte, BthirdByte, BfourthByte;
    String format;
    String instruction;
    String addressingMode;
    int operand;
    int r1;
    int r2;
    boolean endflag = false;

    public Processor(Memory memory, Registers registers) {
        this.registers = registers;
        this.memory = memory;
        instructionMap = Util.createInstructionMap("src/main/java/com/example/simulador_sic_xe/samplecodes/instructions.txt", true);
    }

    public void run() {
        while(!endflag){
            step();
        }
    }

    public void step() {
        if(!endflag){
            clearProcessor();

            BfirstByte = memory.read(registers.getPC().getValue());
            firstByte = Util.byteToHex(memory.read(registers.getPC().getValue()));

            decodeInstruction(instructionMap.get(Util.instructionCleaner(firstByte)));
        }
    }

    private void decodeInstruction(List<String> instr) {

        format = instr.get(1);
        instruction = instr.get(0);

        boolean jumpflag = false;

        setActualConfig();

        switch (format) {
            case "2":
                switch (instruction) {
                    case "ADDR":
                        byteToTwoInts(BsecondByte);
                        Instructions.ADDR(getRegisterFromNumber(registers, r1), getRegisterFromNumber(registers, r2));
                        break;
                    case "CLEAR":
                        byteToTwoInts(BsecondByte);
                        Instructions.CLEAR(getRegisterFromNumber(registers, r1));
                        break;
                    case "COMPR":
                        byteToTwoInts(BsecondByte);
                        Instructions.COMPR(getRegisterFromNumber(registers, r1), getRegisterFromNumber(registers, r2), registers.getSW());
                        break;
                    case "DIVR":
                        byteToTwoInts(BsecondByte);
                        Instructions.DIVR(getRegisterFromNumber(registers, r1), getRegisterFromNumber(registers, r2));
                        break;
                    case "MULR":
                        byteToTwoInts(BsecondByte);
                        Instructions.MULR(getRegisterFromNumber(registers, r1), getRegisterFromNumber(registers, r2));
                        break;
                    case "SUBR":
                        byteToTwoInts(BsecondByte);
                        Instructions.SUBR(getRegisterFromNumber(registers, r1), getRegisterFromNumber(registers, r2));
                        break;
                    case "RMO":
                        byteToTwoInts(BsecondByte);
                        Instructions.RMO(getRegisterFromNumber(registers, r1), getRegisterFromNumber(registers, r2));
                        break;
                    case "SHIFTL":
                        byteToTwoInts(BsecondByte);
                        Instructions.SHIFTL(getRegisterFromNumber(registers, r1));
                        break;
                    case "SHIFTR":
                        byteToTwoInts(BsecondByte);
                        Instructions.SHIFTR(getRegisterFromNumber(registers, r1));
                        break;
                    case "TIXR":
                        byteToTwoInts(BsecondByte);
                        Instructions.TIXR(getRegisterFromNumber(registers, r1), registers);
                        break;
                }
                break;
            case "3":
                switch (instruction) {
                    case "LDA":
                        Instructions.LDA(registers, memory, operand, addressingMode);
                        break;
                    case "LDS":
                        Instructions.LDS(registers, memory, operand, addressingMode);
                        break;
                    case "LDT":
                        Instructions.LDT(registers, memory, operand, addressingMode);
                        break;
                    case "LDX":
                        Instructions.LDX(registers, memory, operand, addressingMode);
                        break;
                    case "LDB":
                        Instructions.LDB(registers, memory, operand, addressingMode);
                        break;
                    case "LDL":
                        Instructions.LDL(registers, memory, operand, addressingMode);
                        break;
                    case "LDCH":
                        Instructions.LDCH(registers, memory, operand, addressingMode);
                        break;
                    case "STX":
                        Instructions.STX(registers, memory, operand, addressingMode);
                        break;
                    case "STB":
                        Instructions.STB(registers, memory, operand, addressingMode);
                        break;
                    case "STS":
                        Instructions.STS(registers, memory, operand, addressingMode);
                        break;
                    case "STL":
                        Instructions.STL(registers, memory, operand, addressingMode);
                        break;
                    case "ADD":
                        Instructions.ADD(registers, memory, operand, addressingMode);
                        break;
                    case "J":
                        Instructions.J(registers, memory, operand, addressingMode);
                        jumpflag = true;
                        break;
                    case "JEQ":
                        jumpflag = Instructions.JEQ(registers, memory, operand, addressingMode);
                        break;
                    case "JGT":
                        jumpflag = Instructions.JGT(registers, memory, operand, addressingMode);
                        break;
                    case "JLT":
                        jumpflag = Instructions.JLT(registers, memory, operand, addressingMode);
                        break;
                    case "JSUB":
                        Instructions.JSUB(registers, memory, operand, addressingMode);
                        jumpflag = true;
                        break;
                    case "RSUB":
                        Instructions.RSUB(registers, memory, operand, addressingMode);
                        jumpflag = true;
                        break;
                    case "STA":
                        Instructions.STA(registers, memory, operand, addressingMode);
                        break;
                    case "AND":
                        Instructions.AND(registers, memory, operand, addressingMode);
                        break;
                    case "COMP":
                        Instructions.COMP(registers, memory, operand, addressingMode);
                        break;
                    case "DIV":
                        Instructions.DIV(registers, memory, operand, addressingMode);
                        break;
                    case "TIX":
                        Instructions.TIX(registers, memory, operand, addressingMode);
                        break;
                    case "END":
                        System.out.println("||||||||||||| O Programa terminou! ||||||||||||| ");
                        endflag = true;
                        break;
                }
                break;
        }
        if(!jumpflag)
            registers.getPC().setValue(registers.getPC().getValue() + Integer.parseInt(format));
    }

    private Register getRegisterFromNumber(Registers registers, int n){
        return switch (n) {
            case 1 -> registers.getX();
            case 2 -> registers.getL();
            case 3 -> registers.getB();
            case 4 -> registers.getS();
            case 5 -> registers.getT();
            case 8 -> registers.getPC();
            case 9 -> registers.getSW();
            default -> registers.getA();
        };
    }

    public void byteToTwoInts(byte b) {
        r1 = (b >> 4) & 0xF;
        r2 = b & 0xF;
    }

    public void clearProcessor(){
        firstByte = null;
        secondByte = null;
        thirdByte = null;
        fourthByte = null;

        BfirstByte = 0;
        BsecondByte = 0;
        BthirdByte = 0;
        BfourthByte = 0;

        format = null;
        instruction = null;
        addressingMode = null;

        int r1 = 0;
        int r2 = 0;
    }

    private byte[] instructionReconstructor(){
        switch (format){
            case "2":
                return new byte[]{BfirstByte, BsecondByte};
            case "3":
                return new byte[]{BfirstByte, BsecondByte, BthirdByte};
            case "4":
                return new byte[]{BfirstByte, BsecondByte, BthirdByte, BfourthByte};
        }
        return null;
    }

    public void setActualConfig(){
        BsecondByte = memory.read(registers.getPC().getValue() + 1);
        secondByte = Util.byteToHex(memory.read(registers.getPC().getValue() + 1));

        BthirdByte = memory.read(registers.getPC().getValue() + 2);
        thirdByte = Util.byteToHex(memory.read(registers.getPC().getValue() + 2));

        BfourthByte = memory.read(registers.getPC().getValue() + 3);
        fourthByte = Util.byteToHex(memory.read(registers.getPC().getValue() + 3));

        if(format.equals("3")){
            operand = Util.getOperand(instructionReconstructor());
            addressingMode = Util.checkAddressingMode(BfirstByte);
        }
    }
}

