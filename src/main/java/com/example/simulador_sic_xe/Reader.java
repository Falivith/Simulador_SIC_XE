package com.example.simulador_sic_xe;

import java.io.*;
import java.util.*;

class lineDecode{
    int LOCCTR;
    String Label;
    String OpCode;
    String Operand;
}
class Reader {

    ////////////////////////////////////////////////////////////////// ObjCodeReader
    public static Loaded readObjectCode(String filePath) {

        String programName = null;
        String initialAddress = null;
        String programSize = null;
        int instr_number = 0;

        List<Instruction> instructionList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                String buffer; // Responsável por guardar o hex atual

                if (line.length() > 0) {
                    if (line.charAt(0) == 'H') {
                        programName = line.substring(2, 8);
                        initialAddress = line.substring(10, 15);
                        programSize = line.substring(16);

                    } else if (line.charAt(0) == 'T') {

                        String baseAddress = line.substring(2, 8);
                        int startIndex = 12, endIndex = 18, lastFormat = 0;

                        // Iteração pelo número de instruções da linha
                        for (int i = 0; i < (Integer.parseInt(line.substring(9, 11), 16)) / 3; i++) {
                            Instruction actual = new Instruction();
                            instr_number++;

                            buffer = line.substring(startIndex, endIndex);
                            actual.setHexCode(buffer);
                            actual.setMnemonic(evaluateOpcode(buffer.substring(0, 2)));
                            actual.setFormat(calcInstructionFormat(actual.getMnemonic()));
                            actual.setAddress(Integer.toHexString((Integer.parseInt(baseAddress, 16)) + lastFormat));

                            if (actual.getFormat() == 2) {
                                actual.setBinaryCode(String.format("%16s", Integer.toBinaryString(Integer.parseInt(buffer, 16))).replace(' ', '0'));
                                actual.setOp1(actual.getBinaryCode().substring(8, 12));
                                actual.setOp2(actual.getBinaryCode().substring(12, 16));
                            }
                            else{
                                actual.setBinaryCode(String.format("%24s", Integer.toBinaryString(Integer.parseInt(buffer, 16))).replace(' ', '0'));
                            }

                            actual.setAddressingMode(addressingModeCalculator(actual.getBinaryCode()));

                            if ( actual.getFormat() == 3 ){
                                actual.setOp1(actual.getBinaryCode().substring(12));
                                if ( actual.getAddressingMode().equals("direct") ){
                                    actual.setOp1(actual.getBinaryCode().substring(9));
                                }
                            }

                            instructionList.add(actual);

                            lastFormat += actual.getFormat();
                            startIndex += 7;
                            endIndex += 7;
                        }
                    } else if (line.charAt(0) == 'E') {
                        System.out.println("Cheguei ao End.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo: " + e.getMessage());
        }

        return new Loaded(initialAddress, programSize, programName, instr_number, instructionList);
    }

    static int calcInstructionFormat(String instruction){
        Set<String> type_2 = new HashSet<>(Arrays.asList(
                "ADDR", "CLEAR", "COMPR",
                "DIVR", "MULR", "RMO",
                "SHIFTL", "SUBR", "TIXR"));
        Set<String> type_3 = new HashSet<>(Arrays.asList(
                "ADD", "AND", "COMP", "DIV",
                "J", "JEQ", "JGT", "JLT",
                "JSUB", "LDA", "LDB", "LDCH",
                "LDL", "LDS", "LDT", "LDX",
                "MUL", "OR", "RSUB", "STA",
                "STB", "STCH", "STL", "STS",
                "STT", "STX", "SUB", "TIX"));
        if (type_2.contains(instruction)) {
            return 2;
        }
        if (type_3.contains(instruction)) {
            return 3;
        }
        return 0;
    }

    static String evaluateOpcode(String opCode){

        Map<String, String> opCodeList = new HashMap<>();
        opCodeList.put("00", "LDA");
        opCodeList.put("18", "ADD");
        opCodeList.put("90", "ADDR");
        opCodeList.put("40", "AND");
        opCodeList.put("01", "LDA");
        opCodeList.put("0C", "STA");
        opCodeList.put("B4", "CLEAR");
        opCodeList.put("28", "COMP");
        opCodeList.put("A0", "COMPR");
        opCodeList.put("24", "DIV");
        opCodeList.put("9C", "DIVR");
        opCodeList.put("3C", "J");
        opCodeList.put("30", "JEQ");
        opCodeList.put("34", "JGT");
        opCodeList.put("38", "JLT");
        opCodeList.put("48", "JSUB");
        opCodeList.put("68", "LDB");
        opCodeList.put("50", "LDCH");
        opCodeList.put("08", "LDL");
        opCodeList.put("6C", "LDS");
        opCodeList.put("74", "LDT");
        opCodeList.put("04", "LDX");
        opCodeList.put("20", "MUL");
        opCodeList.put("98", "MULR");
        opCodeList.put("44", "OR");
        opCodeList.put("AC", "RMO");
        opCodeList.put("4C", "RSUB");
        opCodeList.put("A4", "SHIFTL");
        opCodeList.put("A8", "SHIFTR");
        opCodeList.put("78", "STB");
        opCodeList.put("54", "STCH");
        opCodeList.put("14", "STL");
        opCodeList.put("7C", "STS");
        opCodeList.put("84", "STT");
        opCodeList.put("10", "STX");
        opCodeList.put("1C", "SUB");
        opCodeList.put("94", "SUBR");
        opCodeList.put("2C", "TIX");
        opCodeList.put("B8", "TIXR");

        return opCodeList.getOrDefault(opCode, null);
    }

    static String addressingModeCalculator(String instruction){
        int n = 6, i = 7, x = 8;

        if (instruction.charAt(x) == '1'){
            return "indexed";
        }
        if (instruction.charAt(n) == '0' && instruction.charAt(i) == '1'){
            return "immediate";
        }
        else if (instruction.charAt(n) == '1' && instruction.charAt(i) == '0'){
            return "indirect";
        }
        else{
            return "direct";
        }
    }

    ////////////////////////////////////////////////////////////////// Assembler
    public static HashMap<String, String> createInstructionMap(String path) {
        File file = new File(path);
        HashMap<String, String> mapa = new HashMap<>();

        try {

            Scanner leitor = new Scanner(file);

            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();

                // divide a linha em duas partes: instrução e código
                String[] partes = linha.split(" ");

                // adiciona a instrução e seu código ao HashMap
                mapa.put(partes[0], partes[1]);
            }

            leitor.close();

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        }

        return mapa;
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
        // for (int i = 0; i < asm.size(); i++) System.out.println(asm.get(i));

        return asm;
    }

    public static lineDecode wordSplit(String codeLine) {
        codeLine = codeLine.trim();
        String[] words = codeLine.split("\\s+"); // separa as palavras pelo espaço em branco
        lineDecode line = new lineDecode();

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

    // Escreve OBJCode
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
}