module com.example.simulador_sic_xe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.simulador_sic_xe to javafx.fxml;
    exports com.example.simulador_sic_xe;
}