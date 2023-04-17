package com.example.simulador_sic_xe;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable , Memory.MemoryListener {

    private Processor processor;
    private Memory memory;
    ArrayList<Memory16Row> ListRows = new ArrayList<>();
    ObservableList<Memory16Row> ObservableListRow;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println("Starting...");
        memory = new Memory(256);
        loadMemoryView();
        memory.addListener(this);
        runButton.setOnAction(e -> runButtonClick());
    }

    private void runButtonClick() {
        memory.write(10, (byte) 0b00001110);
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
    private Label LabelRegisterA;

    @FXML
    private Label labelRegisterB;

    @FXML
    private Label labelRegisterL;

    @FXML
    private Label labelRegisterPC;

    @FXML
    private Label labelRegisterS;

    @FXML
    private Label labelRegisterT;

    @FXML
    private Label labelRegisterX;
    @FXML
    private TextArea assemblyTextArea;

    @FXML
    private TextArea objectTextArea;

    @FXML
    private Button runButton;

    @FXML
    private TableColumn<Memory16Row, String> tableColumnBaseAddress;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn0;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn1;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn2;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn3;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn4;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn5;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn6;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn7;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn8;

    @FXML
    private TableColumn<Memory16Row, String> tableColumn9;

    @FXML
    private TableColumn<Memory16Row, String> tableColumnA;

    @FXML
    private TableColumn<Memory16Row, String> tableColumnB;

    @FXML
    private TableColumn<Memory16Row, String> tableColumnC;

    @FXML
    private TableColumn<Memory16Row, String> tableColumnD;

    @FXML
    private TableColumn<Memory16Row, String> tableColumnE;

    @FXML
    private TableView<Memory16Row> tableViewMemory;

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
