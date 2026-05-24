
import javafx.geometry.Point2D;
import javafx.scene.robot.Robot;
public class Test10 {
    public static void main(String[] args) throws Exception {
        new Thread(()->{
            Robot robot = new Robot();
            robot.mouseMove(new Point2D(716,516));
        }).start();

    }
}
