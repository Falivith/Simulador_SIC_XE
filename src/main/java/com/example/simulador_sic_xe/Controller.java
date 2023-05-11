package com.example.simulador_sic_xe;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable , Memory.MemoryListener, Registers.RegistersListener {

    private Processor processor;
    private Memory memory;
    private Registers registers;

    ArrayList<Memory16Row> ListRows = new ArrayList<>();
    ObservableList<Memory16Row> ObservableListRow;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        memory = new Memory(256);
        registers = new Registers();
        memory.addListener(this);
        registers.addListener(this);

        Loaded pInfo = Assembler.assemble("src/main/java/com/example/simulador_sic_xe/samplecodes/fibo.asm");

        fillAssembly(pInfo.getAssembly());
        fillObjCode((pInfo.getObjCode()));
        loadMemoryView();

        runButton.setOnAction(e -> runButtonClick());
        stepButton.setOnAction(e -> stepButtonClick());

        Assembler.loader(memory, pInfo);
        registers.getPC().setValue(pInfo.getStartingAddress());
        processor = new Processor(memory, registers);

            Macro macroprocessador = new Macro("src/main/java/com/example/simulador_sic_xe/samplecodes/macro_2.asm");
            macroprocessador.macroProcessing();
    }

    private void stepButtonClick(){
        processor.step();
    }

    private void runButtonClick() {
        processor.run();
    }

    public void loadMemoryView(){

        // Definição do que será mostrado em cada coluna

        {
            tableColumnBaseAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().baseAddress));
            tableColumn0.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[0]));
            tableColumn1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[1]));
            tableColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[2]));
            tableColumn3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[3]));
            tableColumn4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[4]));
            tableColumn5.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[5]));
            tableColumn6.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[6]));
            tableColumn7.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[7]));
            tableColumn8.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[8]));
            tableColumn9.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[9]));
            tableColumnA.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[10]));
            tableColumnB.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[11]));
            tableColumnC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[12]));
            tableColumnD.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[13]));
            tableColumnE.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[14]));
            tableColumnF.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().hexValues[15]));
        }

        int size = memory.getSize();

        byte[] buffer = new byte[16];
        int count = 0;

        for(int i = 0; i < size/16; i++){
            int current_collection = i * 16;

            for(int j = current_collection; j < current_collection + 16; j++){
                buffer[count] = (memory.read(j));
                count++;
            }

            count = 0;
            ListRows.add(new Memory16Row(i*16, buffer));
        }

        ObservableListRow = FXCollections.observableArrayList(ListRows);
        tableViewMemory.setItems(ObservableListRow);
    }

    @FXML
    private ListView<String> assemblyListView;
    @FXML private Label labelRegisterA, labelRegisterB, labelRegisterL, labelRegisterPC, labelRegisterS, labelRegisterT,
            labelRegisterX, labelRegisterSW;
    @FXML private ListView<String> objCodeListView;
    @FXML private Button runButton;
    @FXML private Button stepButton;
    @FXML private TableColumn<Memory16Row, String> tableColumnBaseAddress, tableColumn0, tableColumn1, tableColumn2,
            tableColumn3, tableColumn4, tableColumn5, tableColumn6, tableColumn7, tableColumn8, tableColumn9,
            tableColumnA, tableColumnB, tableColumnC, tableColumnD, tableColumnE, tableColumnF;
    @FXML private TableView<Memory16Row> tableViewMemory;

    // Métodos para atualizar a tabela conforme a memória for atualizada.
    private void memoryWriteInterface(int address, byte value){
        Memory16Row temporary16Row = ListRows.get(address / 16);
        temporary16Row.hexValues[address % 16] = String.format("%02x", value);
        ListRows.set((address / 16), temporary16Row);
        tableViewMemory.refresh();
    }
    public void onMemoryWrite(int address, byte value) {
        memoryWriteInterface(address, value);
    }

    public void onRegistersChange(Registers registers){
        labelRegisterA.setText(String.format("%06x", registers.getA().getValue()));
        labelRegisterB.setText(String.format("%06x", registers.getB().getValue()));
        labelRegisterL.setText(String.format("%06x", registers.getL().getValue()));
        labelRegisterPC.setText(String.format("%06x", registers.getPC().getValue()));
        labelRegisterSW.setText(String.format("%06x", registers.getSW().getValue()));
        labelRegisterT.setText(String.format("%06x", registers.getT().getValue()));
        labelRegisterX.setText(String.format("%06x", registers.getX().getValue()));
        labelRegisterS.setText(String.format("%06x", registers.getS().getValue()));
    }

    public void fillAssembly(List<String> asm){
        for (String string : asm) {
            assemblyListView.getItems().add(string);
        }
    }

    public void fillObjCode(List<String> obj){
        for (String string : obj) {
            objCodeListView.getItems().add(string);
        }
    }
}

// Classe que representa uma linha da tabela da memória. É uma gambiarra, mas funciona.
class Memory16Row {
    String baseAddress;
    String[] hexValues;

    Memory16Row(int baseAddress, byte[] byteArray){
        this.baseAddress = String.format("%05x", baseAddress);
        hexValues = new String[16];
        for (int i = 0; i < byteArray.length; i++) {
            hexValues[i] = String.format("%02x", byteArray[i]);
        }
    }
}
