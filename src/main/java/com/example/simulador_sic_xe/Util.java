package com.example.simulador_sic_xe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Util {
    public static String byteToHex(byte b) {
        char[] hexChars = new char[2];
        hexChars[0] = Character.forDigit((b >> 4) & 0xF, 16);
        hexChars[1] = Character.forDigit((b & 0xF), 16);
        return new String(hexChars);
    }

    public static HashMap<String, List<String>> createInstructionMap(String path, boolean decode) {
        File file = new File(path);
        HashMap<String, List<String>> mapa = new HashMap<>();

        try {

            Scanner leitor = new Scanner(file);

            if(!decode){
                // Mapa para codificar
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
                // Mapa para decodificar
            }else{
                while (leitor.hasNextLine()) {
                    String linha = leitor.nextLine();

                    // divide a linha em duas partes: instrução, código e formato
                    String[] partes = linha.split(" ");

                    List<String> x = new ArrayList<>();
                    x.add(partes[0]);
                    x.add(partes[2]);

                    // adiciona a instrução e seu código ao HashMap
                    mapa.put(partes[1], x);
                }
            }
            leitor.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        }
        return mapa;
    }

    public static String changeBitsNI(String hexString, String addressingMode) {
        // Converter a String hexadecimal em um byte
        byte b = (byte) Integer.parseInt(hexString, 16);

        if(addressingMode.equals("Imediato"))
            // Define o bit "i" como 1
            b = (byte) (b | 0x01);

        if(addressingMode.equals("Indireto"))
            // Define o bit "n" como 1
            b = (byte) (b | 0x01);

        String manipulatedHexString = String.format("%02X", b);
        return manipulatedHexString;
    }

    public static String checkAddressingMode(byte b){
        int lastTwoBits = b & 0b00000011;

        if( lastTwoBits == 0b10)
            return "Indireto";
        else if (lastTwoBits == 0b01)
            return "Imediato";
        else if (lastTwoBits == 0b00 || lastTwoBits == 0b11)
            return "Direto";
        else return "Simples";
    }

    public static int getOperand(byte[] bytes) {
        // Ignora o primeiro byte e os XBPE
        int displacement;
        if (bytes.length == 3)
            displacement = ((bytes[1] & 0x0F) << 8) | (bytes[2] & 0xFF);
        else
            displacement = ((bytes[1] & 0x0F) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
        // Retorna os 12 bits finais como um inteiro
        return displacement;
    }

    public static String instructionCleaner(String hexString) {
        byte b = (byte) Integer.parseInt(hexString, 16);

        // Limpa os bits NI antes de passar pelo Hashmap de Insturções
        b = (byte) (b & 0b11111100);

        String manipulatedHexString = String.format("%02X", b);
        return manipulatedHexString;
    }

    public static byte[] convertIntToWord(int valor) {
        byte[] word = new byte[3];
        word[0] = (byte) (valor & 0xFF);
        word[1] = (byte) ((valor >> 8) & 0xFF);
        word[2] = (byte) ((valor >> 16) & 0xFF);
        return word;
    }
}
