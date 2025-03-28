package easv.ticketapp.bll;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.util.Builder;

import java.awt.image.BufferedImage;

public class BarcodeGenerator {

    private final BitMatrix bitMatrix;

    private BufferedImage qrImage;

    public BarcodeGenerator(String data, BarcodeFormat format, int width, int height, int imageType) throws WriterException {

        BitMatrixBuilder builder = new BitMatrixBuilder(data, format, width, height);
        this.bitMatrix = builder.build();

        this.qrImageCreate(width, height, imageType);

    }

    private void qrImageCreate(int width, int height, int imageType) {

        // Convert BitMatrix to BufferedImage
        this.qrImage = new BufferedImage(width, height, imageType);

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                qrImage.setRGB(x, y, this.bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);

            }

        }

    }

    public BufferedImage qrImage() {
        return this.qrImage;
    }

    private static class BitMatrixBuilder implements Builder<BitMatrix> {

        private final BitMatrix bitMatrix;

        public BitMatrixBuilder(String data, BarcodeFormat format, int width, int height) throws WriterException {
            this.bitMatrix = new com.google.zxing.MultiFormatWriter().encode(data, format, width, height);
        }

        @Override
        public BitMatrix build() {
            return this.bitMatrix;
        }
    }

}


//EXAMPLE OF USAGE
//
//    private static final int QR_CODE_SIZE = 300;
//    private static final String CONTENT_TO_ENCODE = "wikipedia";
//
//    private final StackPane parent = new StackPane();
//
//    @Override
//    public void init() throws Exception {
//        super.init();
//        this.buildUI();
//    }
//
//    private void buildUI() {
//
//        BarcodeGenerator qrGenerator;
//
//        try {
//            qrGenerator = new BarcodeGenerator(
//                    CONTENT_TO_ENCODE,
//                    BarcodeFormat.QR_CODE,
//                    QR_CODE_SIZE, QR_CODE_SIZE,
//                    BufferedImage.TYPE_INT_ARGB
//            );
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        BufferedImage qrImageBuffered = qrGenerator.qrImage();
//        Image qrImage = SwingFXUtils.toFXImage(qrImageBuffered, null);
//        ImageView imageViewQr = new ImageView(qrImage);
//
//
//        BarcodeGenerator code93Generator;
//
//        try {
//            code93Generator = new BarcodeGenerator(
//                    CONTENT_TO_ENCODE,
//                    BarcodeFormat.CODE_93,
//                    QR_CODE_SIZE, 100,
//                    BufferedImage.TYPE_INT_ARGB
//            );
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        BufferedImage code93ImageBuffered = code93Generator.qrImage();
//        Image image = SwingFXUtils.toFXImage(code93ImageBuffered, null);
//        ImageView imageViewCode93 = new ImageView(image);
//
//
//
////        parent.getChildren().add(imageViewQr);
//        parent.getChildren().add(imageViewCode93);
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        this.setupStage(stage);
//    }
//
//    private void setupStage(Stage stage) {
//
//        Scene scene = new Scene(this.parent, 640.0, 480.0);
//
//        // Sets the stage title
//        stage.setTitle("JavaFX QR Code Generator");
//
//        // Sets the stage scene
//        stage.setScene(scene);
//
//        // Centers stage on screen
//        stage.centerOnScreen();
//
//        // Show stage on screen
//        stage.show();
//
//    }