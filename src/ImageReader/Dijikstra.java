package ImageReader;

import java.security.interfaces.DSAPublicKey;
import java.util.*;

/**
 * Created by rodionov12 on 15.11.2016.
 */
class PriorityPoint
{
    PPoint point;
    double priority;

    public PPoint getPoint() {
        return point;
    }

    public double getPriority() {
        return priority;
    }

    public PriorityPoint(PPoint point, double priority) {
        this.point = point;
        this.priority = priority;
    }
}

public class Dijikstra {
    private Graph g;
    public Dijikstra(Graph g) {
        this.g = g;

    }
    public ArrayList<PPoint> Find() {
        HashMap<PPoint, Double> dist = new HashMap<>();
        ArrayList<PPoint> unvisited = new ArrayList<>();
        HashMap<PPoint, PPoint> prev = new HashMap<>();

        for (PPoint p : g.getPoints().keySet()) {
            if(p != g.getStart())
                dist.put(p, Double.POSITIVE_INFINITY);
            unvisited.add(p);
            prev.put(p, null);
        }

        dist.put(g.getStart(), 0.0);

        while (!unvisited.isEmpty()) {
            PPoint min = getMin(dist);
            unvisited.remove(min);

            for (Map.Entry<PPoint, Double> kv:g.getPoints().get(min).entrySet()) {
                double h = dist.get(kv.getKey()) + g.getPoints().get(min).get(kv.getKey());
                if (h  < dist.get(kv.getKey())) {
                    dist.put(kv.getKey(), h);
                    prev.put(min, kv.getKey());
                }
            }
        }

        ArrayList<PPoint> path = new ArrayList<>();
        PPoint target = g.getEnd();
        while (prev.get(target) != null) {
            path.add(target);
            target = prev.get(target);
        }
        path.add(target);

        return path;
    }

    /***
     * O(1) compleximeasdasd
     * Chuck Sejdijsktra
     */
    public PPoint getMin(HashMap<PPoint, Double> map)
    {
        return map.entrySet().parallelStream()
                .sorted((o1, o2) -> Double.compare(o1.getValue(), o2.getValue()))
                .findFirst()
                .map(x -> x.getKey()).get();
    }
}
