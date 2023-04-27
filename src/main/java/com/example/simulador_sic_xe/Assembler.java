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

        HashMap<String, List<String>> instructionMap = createInstructionMap("src/main/java/com/example/simulador_sic_xe/samplecodes/instructions.txt");
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
            System.out.println("-- Montando o cabeçalho --");

            LineDecode Buffer = wordSplit(assembly.get(0));
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
        LineDecode actual = wordSplit(assembly.get(iterator));

        // Passo 1 da montagem

        while( !actual.OpCode.equals("END") ){

            //System.out.println("LOCCTR: " + String.format("%06X", LOCCTR) + " " + actual.Label + " " + actual.OpCode + " " + actual.Operand);

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
                actual.OpCode.substring(1); // Retira o "+" da instrução pra não haver confusão na tabela de instruções abaixo.
                LOCCTR += 4;
                actual.Format = 4;
            }else if(instructionMap.get(actual.OpCode).get(1).contains("2")){
                LOCCTR += 2;
                actual.Format = 2;
            }
            else{
                LOCCTR += 3;
                actual.Format = 3;
            }

            if(instructionMap.containsKey(actual.OpCode)){

                actual.AddressingMode = "Direct";

                if(actual.Operand.startsWith("#")){
                    System.out.println(actual.OpCode + " Endereçamento Imediato ");
                    actual.AddressingMode = "Immediate";
                }

                if(actual.Operand.endsWith(",X")){
                    System.out.println(actual.OpCode + " Endereçamento Indexado ");
                    actual.AddressingMode = "Indexed";
                }

                if (actual.Operand.startsWith("@")) {
                    System.out.println(actual.OpCode + " Endereçamento Indireto ");
                    actual.AddressingMode = "Indirect";
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

            actual = wordSplit(assembly.get(++iterator));
            actual.LOCCTR = LOCCTR;
            program.add(actual);
        }

        // Fim do programa (Gambiarra) =)
        LineDecode end = new LineDecode();
        end.OpCode = "FF";
        program.add(end);

        // Passo 2 da montagem

        iterator = 1;
        actual = wordSplit(assembly.get(iterator));
        String objCode = "";

        for (LineDecode line : program) {
            String hexCode = "";
        }

        /*
        while (!actual.OpCode.equals("END")) {

            String hexCode = null;

            // Se temos um OPCODE ou uma Diretiva de montador
            if (instructionMap.containsKey(actual.OpCode)) {

                String hexOpCode = instructionMap.get(actual.OpCode).get(0);
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
        */
        
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

    public static HashMap<String, List<String>> createInstructionMap(String path) {
        File file = new File(path);
        HashMap<String, List<String>> mapa = new HashMap<>();

        try {

            Scanner leitor = new Scanner(file);

            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();

                // divide a linha em duas partes: instrução, código e formato
                String[] partes = linha.split(" ");

                List<String> x = new ArrayList<>();
                x.add(partes[1]);
                x.add(partes[2]);

                // adiciona a instrução e seu código ao HashMap
                mapa.put(partes[0], x);
            }

            leitor.close();

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        }

        return mapa;
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

}
