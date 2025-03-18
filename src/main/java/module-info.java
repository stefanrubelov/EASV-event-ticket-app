module easv.ticketapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    opens easv.ticketapp to javafx.fxml;
    opens easv.ticketapp.be to javafx.base;
    exports easv.ticketapp;
}