import ImageReader.Dijikstra;
import ImageReader.Graph;
import ImageReader.PPoint;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rodionov12 on 15.11.2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Graph img = new Graph("radik.png");
        Dijikstra d = new Dijikstra(img);
        img.saveToFile();
        img.saveToImage();
    }
}
