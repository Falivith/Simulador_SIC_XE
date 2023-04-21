package com.example.simulador_sic_xe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
class lineDecode{
    int LOCCTR;
    String Label;
    String OpCode;
    String Operand;
}
public class Assembler {
    public static Loaded assemble(String asmPath) {
        System.out.println("-- Abrindo arquivo Assembly SIC/XE -- ");

        Loaded decodedProgram = null;

        HashMap<String, String> instructionMap = createInstructionMap("src/main/java/com/example/simulador_sic_xe/samplecodes/instructions.txt");
        HashMap<String, Integer> symbolTable = new HashMap<>();
        ArrayList<String> assembly = asmReader(asmPath);
        ArrayList<lineDecode> intermediate = new ArrayList<>();

        int START = 0;
        int PSIZE;
        int LOCCTR;
        String PNAME = null;

        // Define o cabeçalho do programa

        if(assembly.get(0).contains("START")){
            System.out.println("-- Montando o cabeçalho --");

            lineDecode Buffer = wordSplit(assembly.get(0));
            START = Integer.parseInt(Buffer.Operand, 16);
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
        lineDecode actual = wordSplit(assembly.get(iterator));
        intermediate.add(actual);

        // Passo 1 da montagem

        while( !actual.OpCode.equals("END") ){

            System.out.println("LOCCTR: " + String.format("%06X", LOCCTR) + " " + actual.Label + " " + actual.OpCode + " " + actual.Operand);

            // Tabela de símbolos

            if(actual.Label != null && !actual.Label.isEmpty()){
                if(symbolTable.containsKey(actual.Label)){
                    System.out.println("O Símbolo < " + actual.Label + " > já existe na tabela de símbolos.");
                    return null;
                }
                symbolTable.put(actual.Label, LOCCTR);
            }

            // Tabela de Instruções

            if(instructionMap.containsKey(actual.OpCode)){
                LOCCTR += 3;
            }
            else if(actual.OpCode.equals("WORD")){
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

            actual = wordSplit(assembly.get(++iterator));
            actual.LOCCTR = LOCCTR;
            intermediate.add(actual);
        }

        // Passo 2 da montagem

        iterator = 1;
        actual = wordSplit(assembly.get(iterator));
        String objCode = "";

        while (!actual.OpCode.equals("END")) {

            String hexCode = null;

            // Se temos um OPCODE ou uma Diretiva de montador
            if (instructionMap.containsKey(actual.OpCode)) {

                String hexOpCode = instructionMap.get(actual.OpCode);
                int tempOperand = 0;

                // Se temos um operando
                if (actual.Operand != null) {
                    if (symbolTable.containsKey(actual.Operand))
                        tempOperand = symbolTable.get(actual.Operand);
                }
                hexCode = hexOpCode + String.format("%04X", tempOperand);
            } else if (actual.OpCode.equals("BYTE") || actual.OpCode.equals("WORD")) {
                hexCode = String.format("%06X", Integer.parseInt(actual.Operand));
            }

            objCode += hexCode + "\n";

            actual = wordSplit(assembly.get(++iterator));
            LOCCTR += 3;
        }

        System.out.println(objCode);
        writeObjCodeToFile(objCode);

        return decodedProgram = new Loaded(START, 0, PNAME, (objCode.split("\n").length - 1), null);
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
}
