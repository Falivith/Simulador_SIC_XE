package com.example.simulador_sic_xe;

public class Instructions {

    // Tipo 3
    public static void ADD(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setA(registers.getA().getValue() + op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            byte[] word = extractWordFromAddress(memory, op);
            int value = convertWordToInt(word);

            registers.setA(registers.getA().getValue() + value);
        }
    }

    public static void AND(Registers registers, Memory memory, int op, String addressingMode) {
        byte[] memValue = extractWordFromAddress(memory, op);
        //System.out.println(memValue[0] + " " + memValue[1] + " " + memValue[2]);

        byte[] regValue = extractWordFromInt(registers.getA().getValue());
        //System.out.println(regValue[0] + " " + regValue[1] + " " + regValue[2]);

        byte[] result = new byte[3];

        for (int i = 0; i < 3; i++) {
            result[i] = (byte) (memValue[i] & regValue[i]);
        }
        registers.getA().setValue(convertWordToInt(result));
    }

    public static void COMP(Registers registers, Memory memory, int op, String addressingMode){
        int memValue = convertWordToInt(extractWordFromAddress(memory, op));

        if(registers.getA().getValue() == memValue){
            registers.setSW((int)'=');
            return;
        }
        else if(registers.getA().getValue() < memValue){
            registers.setSW((int)'<');
        }else{
            registers.setSW((int)'>');
        }
    }

    public static void DIV(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setA(registers.getA().getValue() + op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            byte[] word = extractWordFromAddress(memory, op);
            int value = convertWordToInt(word);

            registers.setA(registers.getA().getValue() / value);
        }
    }

    public static void TIX(Registers registers, Memory memory, int op, String addressingMode){

        Register X = registers.getX();
        Register SW = registers.getSW();

        X.setValue(X.getValue() + 1);

        int memValue = convertWordToInt(extractWordFromAddress(memory, op));

        //System.out.println("Memória: " + memValue);
        //System.out.println("Regis X: " + X.getValue());

        if(X.getValue() == memValue){
            SW.setValue((int)'=');
            return;
        }
        else if(X.getValue() < memValue){
            SW.setValue((int)'<');
        }else{
            SW.setValue((int)'>');
        }
    }

    public static void J(Registers registers, Memory memory, int op, String addressingMode){
        Register PC = registers.getPC();

        if(addressingMode == "Imediato"){
            //System.out.println("Imediato " + op);
            PC.setValue(op);
        }
        else if(addressingMode == "Indireto"){
            byte[] word = extractWordFromAddress(memory, op);
            int value = convertWordToInt(word);
            PC.setValue(value);
        }
        else{
            //System.out.println("Direto " + op);
            PC.setValue(op);
        }
    }

    public static boolean JEQ(Registers registers, Memory memory, int op, String addressingMode){
        Register PC = registers.getPC();
        Register SW = registers.getSW();

        if(SW.getValue() == '='){
            if(addressingMode == "Imediato"){
                PC.setValue(op);
            }
            else if(addressingMode == "Indireto"){
                byte[] word = extractWordFromAddress(memory, op);
                int value = convertWordToInt(word);
                PC.setValue(value);
            }
            else{
                PC.setValue(op);
            }
            return true;
        }
        return false;
    }

    public static boolean JGT(Registers registers, Memory memory, int op, String addressingMode){
        Register PC = registers.getPC();
        Register SW = registers.getSW();

        if(SW.getValue() == '>'){
            if(addressingMode == "Imediato"){
                System.out.println("Imediato " + op);
                PC.setValue(op);
            }
            else if(addressingMode == "Indireto"){
                byte[] word = extractWordFromAddress(memory, op);
                int value = convertWordToInt(word);
                PC.setValue(value);
            }
            else{
                System.out.println("Direto " + op);
                PC.setValue(op);
            }
            return true;
        }
        return false;
    }

    public static boolean JLT(Registers registers, Memory memory, int op, String addressingMode){
        Register PC = registers.getPC();
        Register SW = registers.getSW();

        if(SW.getValue() == '<'){
            if(addressingMode == "Imediato"){
                //System.out.println("Imediato " + op);
                PC.setValue(op);
            }
            else if(addressingMode == "Indireto"){
                byte[] word = extractWordFromAddress(memory, op);
                int value = convertWordToInt(word);
                PC.setValue(value);
            }
            else{
                //System.out.println("Direto " + op);
                PC.setValue(op);
            }
            return true;
        }
        return false;
    }

    public static void JSUB(Registers registers, Memory memory, int op, String addressingMode){
        Register PC = registers.getPC();
        Register L = registers.getL();

        L.setValue(PC.getValue());

        if(addressingMode == "Imediato"){
            //System.out.println("Imediato " + op);
            PC.setValue(op);
        }
        else if(addressingMode == "Indireto"){
            byte[] word = extractWordFromAddress(memory, op);
            int value = convertWordToInt(word);
            PC.setValue(value);
        }
        else{
            //System.out.println("Direto " + op);
            PC.setValue(op);
        }
    }

    public static void RSUB(Registers registers, Memory memory, int op, String addressingMode) {
        registers.setPC(registers.getL().getValue());
    }

    public static void LDA(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setA(op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            registers.setA(convertWordToInt(extractWordFromAddress(memory, op)));
        }
    }

    public static void LDB(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setB(op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            registers.setB(convertWordToInt(extractWordFromAddress(memory, op)));
        }
    }

    public static void LDCH(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setA(op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            registers.setA(memory.read(op));
        }
    }

    public static void STCH(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            // Implementar
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            generalStore(Util.convertIntToWord(registers.getA().getValue()), op, memory);
        }
    }

    public static void LDL(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setL(op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            registers.setL(convertWordToInt(extractWordFromAddress(memory, op)));
        }
    }

    public static void LDT(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setT(op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            registers.setT(convertWordToInt(extractWordFromAddress(memory, op)));
        }
    }

    public static void LDX(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            registers.setX(op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            registers.setX(convertWordToInt(extractWordFromAddress(memory, op)));
        }
    }

    public static void STA(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            generalStore(Util.convertIntToWord(registers.getA().getValue()), op, memory);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            generalStore(Util.convertIntToWord(registers.getA().getValue()), op, memory);
        }
    }

    public static void STB(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            generalStore(Util.convertIntToWord(registers.getB().getValue()), op, memory);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            generalStore(Util.convertIntToWord(registers.getB().getValue()), op, memory);
        }
    }

    public static void STL(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            generalStore(Util.convertIntToWord(registers.getL().getValue()), op, memory);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            generalStore(Util.convertIntToWord(registers.getL().getValue()), op, memory);
        }
    }

    public static void STS(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            generalStore(Util.convertIntToWord(registers.getS().getValue()), op, memory);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            generalStore(Util.convertIntToWord(registers.getS().getValue()), op, memory);
        }
    }

    public static void STT(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            generalStore(Util.convertIntToWord(registers.getT().getValue()), op, memory);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            generalStore(Util.convertIntToWord(registers.getT().getValue()), op, memory);
        }
    }

    public static void STX(Registers registers, Memory memory, int op, String addressingMode){
        if(addressingMode == "Imediato"){
            generalStore(Util.convertIntToWord(registers.getX().getValue()), op, memory);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            generalStore(Util.convertIntToWord(registers.getX().getValue()), op, memory);
        }
    }

    public static void LDS(Registers registers, Memory memory, int op, String addressingMode){
        Register S = registers.getS();
        if(addressingMode == "Imediato"){
            S.setValue(op);
        }
        else if(addressingMode == "Indireto"){
            // Implementar
        }
        else{
            S.setValue(memory.read(op));
        }
    }

    // Tipo 2
    public static void ADDR(Register R1, Register R2) {
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
        R2.setValue(R2.getValue() / R1.getValue());
    }

    public static void MULR(Register R1, Register R2){
        R2.setValue(R2.getValue() * R1.getValue());
    }

    public static void SUBR(Register R1, Register R2){
        R2.setValue(R2.getValue() - R1.getValue());
    }

    public static void RMO(Register R1, Register R2){
        R2.setValue(R1.getValue());
    }

    public static void SHIFTL(Register R1){
        R1.setValue(R1.getValue() << 2);
    }

    public static void SHIFTR(Register R1){
        R1.setValue(R1.getValue() >> 2);
    }

    public static void TIXR(Register R1, Registers registers){
        Register X = registers.getX();
        Register SW = registers.getSW();

        X.setValue(X.getValue() + 1);

        if(R1.getValue() == X.getValue()){
            SW.setValue((int)'=');
        }
        else if(R1.getValue() < X.getValue()){
            SW.setValue((int)'<');
        }else {
            SW.setValue((int) '>');
        }
    }

    public static void generalStore(byte[] word, int address, Memory memory){
        memory.write(address + 0, word[2]);
        memory.write(address + 1, word[1]);
        memory.write(address + 2, word[0]);
    }

    public static int convertWordToInt(byte[] word) {
        int valor = 0;
        valor |= (word[0] & 0xFF) << 16;
        valor |= (word[1] & 0xFF) << 8;
        valor |= (word[2] & 0xFF);
        return valor;
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
