package ImageReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rodionov12 on 13.11.2016.
 */
public class Graph {
    private HashMap<PPoint, HashMap<PPoint, Double>> points = new HashMap<>();
    private HashMap<PPoint, Double> radik = new HashMap<>();
    private BufferedImage image;
    private PPoint start, end;

    public PPoint getStart() {
        return start;
    }

    public PPoint getEnd() {
        return end;
    }

    public HashMap<PPoint, HashMap<PPoint, Double>> getPoints() {
        return points;
    }

    public HashMap<PPoint, Double> getRadik() {
        return radik;
    }

    public Graph() {

    }
    public Graph(String filename) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(filename));
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    PPoint p = pointColor(i, j);
                    if (p == null)
                        continue;
                    addNeighbours(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double radiationCount(int x, int y) {
        Color point;
        double radiation = 0.0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                point = new Color(image.getRGB(i, j), true);
                if (point.getRed() == 255 && point.getGreen() == 127 && point.getBlue() == 39) {
                    double euc = Math.sqrt(Math.pow(x-i, 2) + Math.pow(y-j, 2));
                    radiation += Math.pow(10,6) * Math.exp(-euc/2);
                }
            }
        }
        return Math.round(radiation);
    }

    private PPoint pointColor(int x, int y)
    {
        Color point = new Color(image.getRGB(x, y), true);
        if (point.getRed() == 255 && point.getGreen() == 0 && point.getBlue() == 0) {
            PPoint p = new PPoint(x, y);
            radik.put(p, radiationCount(x, y));
            points.put(p, new HashMap<>());
            start = p;
            System.out.println("START FOUND " + x + " " + y);
            return p;
        }
        if (point.getRed() == 0 && point.getGreen() == 255 && point.getBlue() == 0) {
            PPoint p = new PPoint(x, y);
            radik.put(p, radiationCount(x, y));
            points.put(p, new HashMap<>());
            end = p;
            System.out.println("END FOUND " + x + " " + y);
            return p;
        }
        if (point.getRed() == 255 && point.getGreen() == 255 && point.getBlue() == 255) {
            PPoint p = new PPoint(x, y);
            radik.put(p, radiationCount(x, y));
            points.put(p, new HashMap<>());
            return p;
        }
        return null;
    }
    private boolean isWhite(int x, int y) {
        Color point = new Color(image.getRGB(x, y), true);
        return point.getRed() == 255 && point.getGreen() == 255 && point.getBlue() == 255;
    }
    private void addNeighbours(PPoint point)
    {
        HashMap<PPoint, Double> subMap = new HashMap<PPoint, Double>();
        int x = point.x;
        int y = point.y;

        boolean l = false, r = false, u = false, d = false;
        if(y - 1 >= 0)
            u = true;
        if(x - 1 >= 0)
            l = true;
        if(y + 1 < image.getHeight())
            d = true;
        if(x + 1 < image.getWidth())
            r = true;

        if(u && isWhite(x, y-1))
            subMap.put(new PPoint(x, y-1), 1.0);
        if(u && r && isWhite(x+1, y-1))
            subMap.put(new PPoint(x+1, y-1), 1.4);
        if(r && isWhite(x+1, y))
            subMap.put(new PPoint(x+1, y), 1.0);
        if(r && d && isWhite(x+1, y+1))
            subMap.put(new PPoint(x+1, y+1), 1.4);
        if(d && isWhite(x, y+1))
            subMap.put(new PPoint(x, y+1), 1.0);
        if(l && d && isWhite(x-1, y+1))
            subMap.put(new PPoint(x-1, y+1), 1.4);
        if(l && isWhite(x-1, y))
            subMap.put(new PPoint(x-1, y), 1.0);
        if(l && u && isWhite(x-1, y-1))
            subMap.put(new PPoint(x-1, y-1), 1.4);

        points.put(point, subMap);
    }


    public void saveToImage() throws IOException {
        BufferedImage raditaed = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        double max = Collections.max(radik.values());
        for(Map.Entry<PPoint, Double> i: radik.entrySet())
        {
            int R = (int)(i.getValue() / max * 255);
            Color c = new Color(R, 0, 0, 1-R/255);
            raditaed.setRGB(i.getKey().x, i.getKey().y, c.getRGB());
        }
        File outputfile = new File("image.png");
        ImageIO.write(raditaed, "png", outputfile);
    }

    public void saveToFile() throws IOException {
        File xyr = new File("radik.pulya");
        PrintWriter ryx = new PrintWriter(xyr);
        double max = Collections.max(radik.values());
        for(Map.Entry<PPoint, Double> i: radik.entrySet())
        {
            ryx.append(i.getKey().x + " " + i.getKey().y + " " + i.getValue() + "\n" );
        }
        ryx.flush();
        ryx.close();
    }
    public static Graph fromRadik(String filename) {
        File xyr = new File("radik.pulya");
        Input ryx = new PrintWriter(xyr);
        try {
            Graph g = new Graph();
            g.image = ImageIO.read(g.getClass().getResourceAsStream(filename));
            for (int i = 0; i < g.image.getHeight(); i++) {
                for (int j = 0; j < g.image.getWidth(); j++) {
                    PPoint p = pointColor(i, j);
                    if (p == null)
                        continue;
                    addNeighbours(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

