package com.example.simulador_sic_xe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

class lineComponents{
    String line;
    String label;
    String opCode;
    String operand;

    public lineComponents(String codeLine) {

        codeLine = codeLine.trim();
        String[] words = codeLine.split("\\s+");
        line = codeLine;

        switch(words.length){
            case 1:
                opCode = words[0];
                break;
            case 2:
                opCode = words[0];
                operand = words[1];
                break;
            case 3:
                label = words[0];
                opCode = words[1];
                operand = words[2];
                break;
        }
    }

    public void setOperand(String newOperand){
        operand = newOperand;

        if (label == null || label.isBlank() || label.isBlank()){
            line = "\t\t" + opCode + "\t\t" + operand;
        }else{
            line = label + "\t" + opCode + "\t\t" + operand;
        }
    }
}

public class Macro {

    ArrayList<String> namTab = new ArrayList<>();
    ArrayList<String> argTab = new ArrayList<>();
    ArrayList<String> argTabMacro = new ArrayList<>();

    HashMap<String, String> substituitions = new HashMap<>();
    HashMap<String, String> defTab = new HashMap<>();
    HashMap<String, String> exchange = new HashMap<>();

    File file;
    Scanner leitor;

    boolean expanding = false;
    String newAsm = new String();
    lineComponents actualLineFromFile;

    String macroAtual;
    int level = 0;
    int linhaNumber = 0;

    public void macroProcessing() {

        System.out.println("-- Iniciando processamento de MACROS SIC/XE -- ");

        getNextLineInterface();
        newAsm += actualLineFromFile.line + "\n";

        while(!actualLineFromFile.opCode.equals("END")){

            getLine(0);
            processLine();

        }
        System.out.println("Final " + newAsm);
        Assembler.writeObjCodeToFile(newAsm, "expanded.asm");
        storeMacroDefinitions(defTab, "definitions.txt");

        return;
    }

    public void getLine(int index){
        if(expanding){

            String[] linhasMacro = separarLinhas(defTab.get(macroAtual));
            //System.out.println("Expandindo " + linhasMacro[index]);

            actualLineFromFile = new lineComponents(linhasMacro[index]);

            // Se a linha tem algo a ser substituído
            if (actualLineFromFile.operand.contains("&")){
                for (Map.Entry<String, String> entrada : exchange.entrySet()) {
                    String chave = entrada.getKey();
                    String valor = entrada.getValue();
                    actualLineFromFile.setOperand(actualLineFromFile.operand.replace(chave, valor));
                }
            }

            //System.out.println("Resultado " + actualLineFromFile.operand);
        }
        else{
            getNextLineInterface();
        }
    }

    public void processLine(){
        if(namTab.contains(actualLineFromFile.opCode)){
            expand();
        } else if (actualLineFromFile.opCode.equals("MACRO")) {
            define();
        } else {
            if(!actualLineFromFile.line.isBlank())
            newAsm += actualLineFromFile.line + "\n";
        }
    }

    public void define(){

        lineComponents macroDefLine = actualLineFromFile;

        namTab.add(macroDefLine.label);
        defTab.put(macroDefLine.label, macroDefLine.line);
        level = 1;

        while (level > 0){
            getLine(0);
            if(!(actualLineFromFile.line.isEmpty() || actualLineFromFile.line.isBlank())){

                // Adiciona a linha na tabela de definição
                String temp = defTab.get(macroDefLine.label);
                temp += "\n" + actualLineFromFile.line;
                defTab.put(macroDefLine.label, temp);

                if (actualLineFromFile.opCode.equals("MACRO")) {
                    level++;
                }else if (actualLineFromFile.opCode.equals("MEND")){
                    level--;
                }
            }
        }
    }

    public void expand(){
        expanding = true;

        macroAtual = actualLineFromFile.opCode;
        String[] linhasMacro = separarLinhas(defTab.get(macroAtual));
        lineComponents prototipoMacro = new lineComponents(linhasMacro[0]);

        // Seta as variáveis internas da macro
        argTabMacro = splitArgs(prototipoMacro.operand);

        // Seta os argumentos para substituir
        argTab = splitArgs(actualLineFromFile.operand);

        if(argTabMacro.size() != argTab.size()){
            throw new RuntimeException("Inconsistência na contagem dos argumentos.");
        }

        // Pareamento de cada variável com cada argumento
        for (int i = 0; i < argTab.size(); i++){
            exchange.put(argTabMacro.get(i), argTab.get(i));
        }

        System.out.println("Macro Invocation");

        for (int i = 1; i < linhasMacro.length - 1; i++){
            getLine(i);
            processLine();
        }

        //argTab.clear(); // Tem que ver isso aqui
        //argTabMacro.clear(); // Tem que ver isso aqui
        expanding = false;
    }

    public lineComponents getNextLineInterface(){
        actualLineFromFile = new lineComponents(leitor.nextLine());
        return actualLineFromFile;
    }

    Macro(String path){
            file = new File(path);
        try {
            leitor = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList <String> splitArgs(String args){
        String[] elementos = args.split(",");
        ArrayList<String> lista = new ArrayList<String>();
        for (String elemento : elementos) {
            lista.add(elemento);
        }
        return lista;
    }

    private static String[] separarLinhas(String string) {
        String[] linhas = string.split("\\r?\\n"); // ou string.split("\\r\\n") dependendo do formato da quebra de linha
        return linhas;
    }

    private static void storeMacroDefinitions(HashMap<String, String> dt, String nomearq){

        File defFile = new File(nomearq);
        PrintWriter printWriterObjeto = null;

        try {
            printWriterObjeto = new PrintWriter(defFile);

            for (Map.Entry<String, String> entrada : dt.entrySet()) {
                String chave = entrada.getKey();
                String valor = entrada.getValue();
                printWriterObjeto.print(valor + "\n\n");
            }

            printWriterObjeto.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
