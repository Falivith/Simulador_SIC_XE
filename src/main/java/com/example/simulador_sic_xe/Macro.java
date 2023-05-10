package com.example.simulador_sic_xe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
}

public class Macro {

    ArrayList<String> namTab = new ArrayList<>();
    HashMap<String, String> substituitions = new HashMap<>();
    HashMap<String, String> defTab = new HashMap<>();
    ArrayList<String> argTab = new ArrayList<>();
    File file;
    Scanner leitor;
    lineComponents actualLineFromFile;
    boolean expanding = false;
    String newAsm = new String();
    int level = 0;
    int linhaNumber = 0;

    public void macroProcessing() {

        System.out.println("-- Iniciando processamento de MACROS SIC/XE -- ");

        getNextLineInterface();
        newAsm += actualLineFromFile.line + "\n";

        while(!actualLineFromFile.opCode.equals("END")){

            getLine();
            processLine();

        }
        System.out.println("Final " + newAsm);

        return;
    }

    public void getLine(){
        if(expanding){
            System.out.println("Expandindo");
            // pega a nova linha da macro na tabela de definição
            // vai substituindos os argumentos
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
            newAsm += actualLineFromFile.line + "\n";
        }
    }

    public void define(){
        lineComponents macroDefLine = actualLineFromFile;

        namTab.add(macroDefLine.label);
        defTab.put(macroDefLine.label, macroDefLine.line);
        level = 1;

        while (level > 0){
            getLine();
            if(!(actualLineFromFile.line.isEmpty() || actualLineFromFile.line.isBlank())){

                // Adiciona a linha na tabela de definição
                String temp = defTab.get(macroDefLine.label);
                temp += "\n" + actualLineFromFile.line + "\n";
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
        String firstLine = defTab.get(actualLineFromFile.opCode);
        argTab = splitArgs(actualLineFromFile.operand);
        newAsm += defTab.get(actualLineFromFile.opCode) + "\n";

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
}
