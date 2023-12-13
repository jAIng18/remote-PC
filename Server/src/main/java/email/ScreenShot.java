package email;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenShot {
    Robot robot;
    private static ScreenShot instance = new ScreenShot();
    private Rectangle rectangle;

    private ScreenShot() {
        try {
            robot = new Robot();
            rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        } catch (AWTException e) {
            System.out.println("Error! Cannot create robot");
        }
    }

    public static ScreenShot getInstance() {
        return instance;
    }

    public String takeScreenShot() {
        try {
            String filename = "snapshot" + ZonedDateTime.now().format(DateTimeFormatter
                    .ofPattern("dd-MM-yyyy HH-mm")) + ".png";
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            ImageIO.write(bufferedImage, "png", new File(filename));
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public static void main(String[] args) {
        ScreenShot.getInstance().takeScreenShot();
    }
}