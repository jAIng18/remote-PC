module com.ui.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.mail;
    requires java.desktop;

    opens com.ui.gui to javafx.fxml;
    exports com.ui.gui;
}