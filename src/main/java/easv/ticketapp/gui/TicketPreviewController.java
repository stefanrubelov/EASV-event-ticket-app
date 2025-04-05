package easv.ticketapp.gui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.bll.BarcodeGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class TicketPreviewController {
    @FXML
    private Label eventNameLbl1;
    @FXML
    private Label eventNameLbl2;
    @FXML
    private Label eventDateLbl1;
    @FXML
    private Label eventDateLbl2;
    @FXML
    private Label eventLocationLbl1;
    @FXML
    private Label eventLocationLbl2;
    @FXML
    private Label ticketPriceLbl;
    @FXML
    private Label ticketDescriptionLbl;
    @FXML
    private Label eventDescriptionLbl;
    @FXML
    private Label customerNameLbl;
    @FXML
    private ImageView barcodeContainer1;
    @FXML
    private ImageView barcodeContainer2;
    @FXML
    private Label ticketCodeLbl;
    @FXML
    private Pane rootPane;
    @FXML
    private ImageView ticketTemplate;

    public void setBarcode(String code) {
        BarcodeGenerator code93Generator;
        Integer width = 180;
        Integer height = 50;
        try {
            code93Generator = new BarcodeGenerator(
                    code,
                    BarcodeFormat.CODE_93,
                    width, height,
                    BufferedImage.TYPE_INT_ARGB
            );

        } catch (WriterException e) {
            e.printStackTrace();
            return;
        }

        BufferedImage code93ImageBuffered = code93Generator.qrImage();
        Image image = SwingFXUtils.toFXImage(code93ImageBuffered, null);

        barcodeContainer1.setImage(image);
        barcodeContainer1.setY(30);
        barcodeContainer1.setX(3);
        barcodeContainer1.setFitWidth(width);
        barcodeContainer1.setFitHeight(height);

        barcodeContainer2.setImage(image);
        barcodeContainer2.setX(-7);
        barcodeContainer2.setY(30);
        barcodeContainer2.setFitWidth(width);
        barcodeContainer2.setFitHeight(height);
        ticketCodeLbl.setText(code);
    }

    public void setCustomerName(String customerName) {
        customerNameLbl.setText(customerName);
    }

    public void updatePreview(Ticket ticket) {
        eventNameLbl1.setText(ticket.getEvent().getName());
        eventNameLbl2.setText(ticket.getEvent().getName());
        eventDateLbl1.setText(ticket.getEvent().getDateFormatted());
        eventDateLbl2.setText(ticket.getEvent().getDateFormatted());
        eventLocationLbl1.setText(ticket.getEvent().getLocation());
        eventLocationLbl2.setText(ticket.getEvent().getLocation());

        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println(df.format(ticket.getPrice()));
        ticketPriceLbl.setText(df.format(ticket.getPrice()));

        ticketDescriptionLbl.setText(ticket.getDescription());
        eventDescriptionLbl.setText(ticket.getEvent().getDescription());
        String type = ticket.getTicketType().getName().toLowerCase();
        String imagePath = "/images/ticket-templates/";

        if ("vip".equals(type)) {
            imagePath += "vip.jpg";
        } else if ("drinks".equals(type)) {
            imagePath += "drinks.jpg";
        } else {
            imagePath += "regular.jpg";
        }

        ticketTemplate.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }

    public void setBackgroundImage(String imagePath) {
        rootPane.setStyle("-fx-background-image: url(");
    }

    public Pane getRootPane() {
        return rootPane;
    }


}
