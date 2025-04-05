package easv.ticketapp.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PdfExporter {

    public static CompletableFuture<Boolean> exportScenesToMultiplePDFsWithDialog(List<Scene> scenes, Stage stage, String ticketName, int copiesPerScene) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        String ticketNameParsed = ticketName.replaceAll("\\s+", "_").toLowerCase();

        if (scenes == null || scenes.isEmpty()) {
            future.complete(false);
            return future;
        }

        // Take single snapshot of each scene
        List<CompletableFuture<byte[]>> snapshotFutures = new ArrayList<>();

        for (Scene scene : scenes) {
            double width = scene.getWidth() > 0 ? scene.getWidth() : 850;
            double height = scene.getHeight() > 0 ? scene.getHeight() : 600;

            WritableImage snapshot = new WritableImage((int) width, (int) height);
            CompletableFuture<byte[]> snapshotFuture = new CompletableFuture<>();

            scene.snapshot(result -> {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(SwingFXUtils.fromFXImage(result.getImage(), null), "png", outputStream);
                    snapshotFuture.complete(outputStream.toByteArray());
                } catch (Exception e) {
                    snapshotFuture.completeExceptionally(e);
                }
                return null;
            }, snapshot);

            snapshotFutures.add(snapshotFuture);
        }

        CompletableFuture.allOf(snapshotFutures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    Platform.runLater(() -> {
                        try {
                            DirectoryChooser directoryChooser = new DirectoryChooser();
                            directoryChooser.setTitle("Save PDF Files");
                            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

                            File selectedDir = directoryChooser.showDialog(stage);

                            if (selectedDir == null) {
                                future.complete(false);
                                return;
                            }

                            boolean allSuccess = true;

                            // For each scene, generate the requested number of copies
                            for (int i = 0; i < scenes.size(); i++) {
                                byte[] imageBytes = snapshotFutures.get(i).join();

                                // Generate the copies for this scene
                                for (int copy = 1; copy <= copiesPerScene; copy++) {
                                    String fileName = String.format(
                                            "%s_%d_%d.pdf",
                                            ticketNameParsed,
                                            i + 1,  // Scene number
                                            copy    // Copy number
                                    );

                                    File pdfFile = new File(selectedDir, fileName);

                                    Document doc = null;
                                    PdfWriter writer = null;
                                    try {
                                        doc = new Document();
                                        writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
                                        doc.open();

                                        Image pdfImage = Image.getInstance(imageBytes);
                                        pdfImage.scaleToFit(580f, 370f);
                                        doc.add(pdfImage);

                                    } catch (Exception e) {
                                        System.err.println("Failed to save PDF: " + fileName);
                                        e.printStackTrace();
                                        allSuccess = false;
                                    } finally {
                                        if (doc != null) doc.close();
                                        if (writer != null) writer.close();
                                    }
                                }
                            }

                            future.complete(allSuccess);
                        } catch (Exception e) {
                            future.completeExceptionally(e);
                        }
                    });
                })
                .exceptionally(ex -> {
                    future.complete(false);
                    return null;
                });

        return future;
    }

    public static CompletableFuture<List<String>> exportScenesToMultiplePDFs(List<Scene> scenes, Stage stage, String ticketName, int copiesPerScene) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        List<String> savedPaths = new ArrayList<>();

        if (scenes == null || scenes.isEmpty()) {
            future.complete(savedPaths);
            return future;
        }

        String timestampDir = Paths.get("storage", String.valueOf(System.currentTimeMillis())).toString();
        try {
            Files.createDirectories(Paths.get(timestampDir));
        } catch (IOException e) {
            future.completeExceptionally(e);
            return future;
        }

        List<CompletableFuture<byte[]>> snapshotFutures = new ArrayList<>();

        for (Scene scene : scenes) {
            double width = scene.getWidth() > 0 ? scene.getWidth() : 850;
            double height = scene.getHeight() > 0 ? scene.getHeight() : 600;

            WritableImage snapshot = new WritableImage((int) width, (int) height);
            CompletableFuture<byte[]> snapshotFuture = new CompletableFuture<>();

            scene.snapshot(result -> {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(SwingFXUtils.fromFXImage(result.getImage(), null), "png", outputStream);
                    snapshotFuture.complete(outputStream.toByteArray());
                } catch (Exception e) {
                    snapshotFuture.completeExceptionally(e);
                }
                return null;
            }, snapshot);

            snapshotFutures.add(snapshotFuture);
        }

        CompletableFuture.allOf(snapshotFutures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    try {
                        String ticketNameParsed = ticketName.replaceAll("\\s+", "_").toLowerCase();

                        for (int i = 0; i < scenes.size(); i++) {
                            byte[] imageBytes = snapshotFutures.get(i).join();

                            for (int copy = 1; copy <= copiesPerScene; copy++) {
                                String fileName = String.format(
                                        "%s_%d_%d.pdf",
                                        ticketNameParsed,
                                        i + 1,
                                        copy
                                );

                                File pdfFile = new File(timestampDir, fileName);

                                Document doc = null;
                                PdfWriter writer = null;
                                try {
                                    doc = new Document();
                                    writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
                                    doc.open();

                                    Image pdfImage = Image.getInstance(imageBytes);
                                    pdfImage.scaleToFit(580f, 370f);
                                    doc.add(pdfImage);

                                    savedPaths.add(pdfFile.getAbsolutePath());
                                } catch (Exception e) {
                                    System.err.println("Failed to save PDF: " + fileName);
                                    e.printStackTrace();
                                } finally {
                                    if (doc != null) doc.close();
                                    if (writer != null) writer.close();
                                }
                            }
                        }

                        future.complete(savedPaths);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                })
                .exceptionally(ex -> {
                    future.completeExceptionally(ex);
                    return null;
                });

        return future;
    }
}