package com.example.simulador_sic_xe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    HashMap<String, String> defTab = new HashMap<>();
    File file;
    Scanner leitor;
    lineComponents actualLineFromFile;
    boolean expanding = false;
    String newAsm = new String();

    public void macroProcessing() {

        System.out.println("-- Iniciando processamento de MACROS SIC/XE -- ");

        /*ArrayList<lineComponents> assembly_ = new ArrayList<>();

        for (String line: assembly) {
            assembly_.add(wordSplit(line));
        }*/

        // begin (macro processor)
        getNextLine();

        while(!actualLineFromFile.opCode.equals("END")){

            getLine();
            processLine();

        }
        return;
    }

    public void getLine(){
        if(expanding){
            //get next line of macro def...
            // substitute args from argtab...
        }
        else{
            getNextLine();
        }
    }

    public void processLine(){
        if(namTab.contains(actualLineFromFile.opCode)){
            expand();
        } else if (actualLineFromFile.opCode.equals("MACRO")) {
            define();
        } else {
            System.out.println("Só replicando pro arquivo final");
            newAsm += "\n" + actualLineFromFile;
        }
    }

    public void define(){

    }

    public void expand(){
        expanding = true;
    }

    public lineComponents getNextLine(){
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
}
