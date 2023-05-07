package com.example.simulador_sic_xe;

public class Instruction {

    private int format; // 2, 3, 4
    private String mnemonic;
    private String address;
    private String addressingMode;
    private String op1;
    private String op2;
    private String hexCode;
    private String binaryCode;

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressingMode() {
        return addressingMode;
    }

    public void setAddressingMode(String addressingMode) {
        this.addressingMode = addressingMode;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getBinaryCode() {
        return binaryCode;
    }

    public void setBinaryCode(String binaryCode) {
        this.binaryCode = binaryCode;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
}
