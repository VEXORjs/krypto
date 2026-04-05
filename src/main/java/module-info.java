module cryptoDES {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;

    opens cryptoDES to javafx.fxml;
    exports cryptoDES;
}