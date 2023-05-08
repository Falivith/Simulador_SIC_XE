package com.example.simulador_sic_xe;

import javafx.scene.shape.Line;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
class LineDecode {
    int LOCCTR;
    int Format;
    String Label;
    String OpCode;
    String Operand;
    String AddressingMode;
    String ObjectCode;
}

public class Assembler {
    public static Loaded assemble(String asmPath) {
        System.out.println("-- Abrindo arquivo Assembly SIC/XE -- ");

        HashMap<String, List<String>> instructionMap = Util.createInstructionMap("src/main/java/com/example/simulador_sic_xe/samplecodes/instructions.txt", false);
        HashMap<String, Integer> symbolTable = new HashMap<>();
        ArrayList<String> assembly = asmReader(asmPath);

        int START = 0;
        int PSIZE;
        int LOCCTR;
        String PNAME = null;

        /* Estruturas que vão user usadas pela VM pra code highlighting
        *  Essas estruturas estão presentes no montador porque a interface
        *  Precisa ter acesso ao código assembly fonte. */

        Loaded decodedProgram = new Loaded();
        ArrayList<LineDecode> program = new ArrayList<>();

        /*
        * Estava utilizando o formato utilizando header, text e end
        * mas mudei devido a complicações no desenvolvimento.
        * */

        if(assembly.get(0).contains("START")){
            //System.out.println("-- Montando o cabeçalho --");
            LineDecode Buffer = wordSplit(assembly.get(0));
            START = Integer.parseInt(Buffer.Operand);
            LOCCTR = START;
            symbolTable.put(Buffer.Label, LOCCTR);
            PNAME = Buffer.Label;
            //System.out.println("LOCCTR: " + String.format("%06X", LOCCTR));
        }
        else {
            LOCCTR = 0;
        }

        // Definição dos buffers e iteradores para cada linha do Assembly

        int iterator = 1;
        LineDecode actual = wordSplit(assembly.get(iterator));

        // Passo 1 da montagem

        while( !actual.OpCode.equals("END") ){

            // Tabela de símbolos
            if(actual.Label != null && !actual.Label.isEmpty()){
                if(symbolTable.containsKey(actual.Label)){
                    System.out.println("O Símbolo < " + actual.Label + " > já existe na tabela de símbolos.");
                    return null;
                }
                symbolTable.put(actual.Label, LOCCTR);
            }

            // Tabela de Instruções

            boolean extendedFlag = actual.OpCode.startsWith("+");
            if (extendedFlag) {
                actual.OpCode = actual.OpCode.substring(1); // Retira o "+" da instrução pra não haver confusão na tabela de instruções abaixo.
            }

            if(instructionMap.containsKey(actual.OpCode)){

                actual.AddressingMode = "Direct";

                if(actual.Operand.startsWith("#")){
                    actual.AddressingMode = "Immediate";
                }

                if(actual.Operand.endsWith(",X")){
                    actual.AddressingMode = "Indexed";
                }

                if (actual.Operand.startsWith("@")) {
                    actual.AddressingMode = "Indirect";
                }

                if(extendedFlag) {
                    LOCCTR += 4;
                    actual.Format = 4;
                }
                else if(instructionMap.get(actual.OpCode).get(1).contains("2")){
                    LOCCTR += 2;
                    actual.Format = 2;
                }
                else{
                    LOCCTR += 3;
                    actual.Format = 3;
                }
            }
            else if(actual.OpCode.equals("WORD")){
                actual.ObjectCode = String.format("%06X", Integer.parseInt(actual.Operand));
                LOCCTR += 3;
            }
            else if(actual.OpCode.equals("RESW")){
                LOCCTR = LOCCTR + 3 * Integer.parseInt(actual.Operand);
            }
            else if(actual.OpCode.equals("RESB")){
                LOCCTR = LOCCTR + Integer.parseInt(actual.Operand);
            }
            else if(actual.OpCode.equals("BYTE")){
                LOCCTR = LOCCTR + 1;
            }
            else{
                System.out.println("A operação < " + actual.OpCode + " > é inválida." + actual.Operand);
                return null;
            }

            program.add(actual);
            actual = wordSplit(assembly.get(++iterator));
            actual.LOCCTR = LOCCTR;
        }

        // Fim do programa (Gambiarra para incluir o END e o FINISH) =)

        if(actual.Label != null && !actual.Label.isEmpty()){
            if(symbolTable.containsKey(actual.Label)){
                System.out.println("O Símbolo < " + actual.Label + " > já existe na tabela de símbolos.");
                return null;
            }
            symbolTable.put(actual.Label, LOCCTR);
        }

        program.add(actual);
        printProgram(program);

        // Passo 2 da montagem (Código OBJETO)

        ArrayList<String> objCodeList = new ArrayList<>();
        String objCode = "";

        for (LineDecode line : program) {

            if(line.OpCode.equals("END")){
                objCodeList.add("FC");
                objCode += "FC" + "\n";
                continue;
            }

            String lineObjCode = null;

            switch (line.Format){
                case 2:
                    lineObjCode = assembleFormat2(line, instructionMap);
                    if (lineObjCode == null) {
                        System.out.println("Erro na montagem");
                    }
                    break;
                case 3:
                    lineObjCode = assembleFormat3(line, instructionMap, symbolTable);
                    if (lineObjCode == null) {
                        System.out.println("Erro na montagem");
                    }
                    break;
                case 4:
                    break;
                default:
                    break;
            }

            if(line.OpCode.equals("WORD")){
                lineObjCode = String.format("%06X", Integer.parseInt(line.Operand));
            }

            if(line.OpCode.equals("BYTE")){
                lineObjCode = String.format("%02X", Integer.parseInt(line.Operand));
            }

            objCodeList.add(lineObjCode);
            objCode += lineObjCode + "\n";
        }

        System.out.println(" -- Código Objeto -- \n" + objCode + "\n");
        printSymbolTable(symbolTable);
        System.out.println();

        writeObjCodeToFile(objCode);

        return decodedProgram = new Loaded(START, PNAME, (objCode.split("\n").length - 1), program, objCodeList, assembly);
    }

    public static ArrayList<String> asmReader(String path){

        File file = new File(path);
        ArrayList<String> asm = new ArrayList<>();

        try {
            Scanner leitor = new Scanner(file);

            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                asm.add(linha);
            }
            leitor.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        }

        // Descomentar para printar o Assembly
        for (int i = 0; i < asm.size(); i++) System.out.println(asm.get(i));

        return asm;
    }
    public static void writeObjCodeToFile(String objCode) {
        File objFile = new File("out.obj");
        PrintWriter printWriterObjeto = null;

        try {
            printWriterObjeto = new PrintWriter(objFile);
            printWriterObjeto.print(objCode);
            printWriterObjeto.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void printStringArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
    public static void printSymbolTable(HashMap<String, Integer> SymTab){
        System.out.println("-- Tabela de Símbolos --");
        for (String instrucao : SymTab.keySet())
            System.out.println(instrucao + " = " + String.format("%06X", SymTab.get(instrucao)));
    }
    public static LineDecode wordSplit(String codeLine) {
        codeLine = codeLine.trim();
        String[] words = codeLine.split("\\s+"); // separa as palavras pelo espaço em branco
        LineDecode line = new LineDecode();

        switch(words.length){
            case 1:
                line.OpCode = words[0];
                break;
            case 2:
                line.OpCode = words[0];
                line.Operand = words[1];
                break;
            case 3:
                line.Label = words[0];
                line.OpCode = words[1];
                line.Operand = words[2];
                break;
        }

        return line;
    }
    public static void printProgram(List<LineDecode> program){
        for (LineDecode line: program) {
            System.out.println(
                    "LOCCTR: " + String.format("%06X", (line.LOCCTR))
          + " | " + "OPCODE: " + String.format("%-5s", line.OpCode)
          + " | " + "OPERAN: " + String.format("%-5s", line.Operand));
        }
    }
    public static String assembleFormat2(LineDecode line, HashMap<String, List<String>> instructionMap){
        String hexCode = null;
        char addressMode;
        int r1;
        int r2;

        if(instructionMap.get(line.OpCode) != null) {
            hexCode = instructionMap.get(line.OpCode).get(0);
        }
        else {
            System.out.println(line.OpCode + " Não existe essa instrução.");
            return null;
        }

        if(line.Operand != null){

            // Não precisa resolver o endereço pela tabela de símbolos pois é tipo 2

            // Caso sejam 2 registradores passados
            if(line.Operand.length() == 3){
                r1 = chooseRegister(line.Operand.charAt(0));
                r2 = chooseRegister(line.Operand.charAt(2));
                hexCode += Integer.toString(r1);
                hexCode += Integer.toString(r2);
            }
            else{
                r1 = chooseRegister(line.Operand.charAt(0));
                hexCode += Integer.toString(r1);
                hexCode += '0';
            }
        }
        else {
            System.out.println(line.OpCode + " Não existe Operando.");
            return null;
        }

        return hexCode;
    }
    public static String assembleFormat3(LineDecode line, HashMap<String, List<String>> instructionMap, HashMap<String, Integer> symbolTable){
        String hexCode = null;
        char addressMode;

        if(instructionMap.get(line.OpCode) != null) {
            hexCode = instructionMap.get(line.OpCode).get(0);
        }

        // Endereçamento Imediato (#)
        if(line.Operand.charAt(0) == '#'){
            hexCode = Util.changeBitsNI(hexCode, "Imediato");
            hexCode += String.format("%04X", Integer.parseInt(line.Operand.substring(1)));
            addressMode = '#';
            // Endereçamento Indireto (@)
        } else if(line.Operand.charAt(0) == '@') {
            hexCode = Util.changeBitsNI(hexCode, "Indireto");
            hexCode += String.format("%04X", (line.Operand.substring(1)));
            addressMode = '@';
        }
        // Endereçamento Direto (Padrão)
        else {
            hexCode += String.format("%04X", symbolTable.get(line.Operand));
            addressMode = 'D';
        }

        return hexCode;
    }
    public static String assembleFormat4(LineDecode line, HashMap<String, List<String>> instructionMap, HashMap<String, Integer> symbolTable){
        String hexCode = null;
            if(instructionMap.get(line.OpCode) != null) {
                hexCode = instructionMap.get(line.OpCode).get(0);
            }
        return hexCode;
    }
    public static int chooseRegister(char r){
        switch (r){
            case 'A': return 0;
            case 'S': return 4;
            case 'T': return 5;
        }
        return -1;
    }
    public static void loader(Memory memory, Loaded pInfo){

        int baseAddress = pInfo.getStartingAddress();
        List<String> objC = pInfo.getObjCode();

        for (String line: objC) {
            switch (line.length()){
                case 2: // Serve apenas pra END e BYTE
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(0, 2), 16));
                    baseAddress += 1;
                    break;
                case 4:
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(0, 2), 16));
                    baseAddress += 1;
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(2, 4), 16));
                    baseAddress += 1;
                    break;
                case 6:
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(0, 2), 16));
                    baseAddress += 1;
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(2, 4), 16));
                    baseAddress += 1;
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(4, 6), 16));
                    baseAddress += 1;
                    break;
                case 8:
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(0, 2), 16));
                    baseAddress += 1;
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(2, 4), 16));
                    baseAddress += 1;
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(4, 6), 16));
                    baseAddress += 1;
                    memory.write(baseAddress, (byte) Integer.parseInt(line.substring(6, 8), 16));
                    baseAddress += 1;
                    break;
            }
        }
    }
}
