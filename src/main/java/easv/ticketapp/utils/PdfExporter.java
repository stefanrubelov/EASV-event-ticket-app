package easv.ticketapp.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class PdfExporter {

    public static void exportSceneToPDF(Scene scene, Stage stage) {
        if (scene == null || scene.getRoot() == null) {
            System.out.println("Invalid scene or root node!");
            return;
        }

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        if (sceneWidth <= 0 || sceneHeight <= 0) {
            sceneWidth = 850;
            sceneHeight = 600;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        if (file == null) {
            System.out.println("Save operation canceled.");
            return;
        }
        String outputPath = file.getAbsolutePath();
        WritableImage snapshot = new WritableImage((int) sceneWidth, (int) sceneHeight);

        scene.snapshot(result -> {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(SwingFXUtils.fromFXImage(result.getImage(), null), "png", outputStream);

                Document document = new Document();
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
                document.open();

                byte[] imageBytes = outputStream.toByteArray();
                Image pdfImage = Image.getInstance(imageBytes);

                float desiredWidth = 580f;
                float desiredHeight = 370f;
                pdfImage.scaleToFit(desiredWidth, desiredHeight);
                document.add(pdfImage);

                document.close();
                System.out.println("PDF saved successfully: " + outputPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }, snapshot);
    }
}
