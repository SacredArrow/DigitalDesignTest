import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePainter {
    public static BufferedImage paintImage(BufferedImage image) {
        for (int i = 0; i< image.getHeight();i++) {
            for (int j = 0; j< image.getWidth();j++) { image.setRGB(j, i, Color.WHITE.getRGB());
            }
        }
        for (int i = 5; i< image.getHeight()-20;i++) {

            image.setRGB(5, i, Color.BLACK.getRGB());

        }
        for (int j = 5; j< image.getWidth()-20;j++) {

            image.setRGB(j, 5, Color.YELLOW.getRGB());

        }
        for (int j = 5; j< image.getWidth()-20;j++) {

            image.setRGB(j, 400, Color.GREEN.getRGB());

        }
        return image;
    }
}
