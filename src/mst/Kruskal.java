
package mst;

import dsu.DSU;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {
    public static class Result {
        public final List<Graph.Edge> edges;
        public final long totalWeight;
        public final boolean connected;

        public Result(List<Graph.Edge> edges, long totalWeight, boolean connected) {
            this.edges = edges;
            this.totalWeight = totalWeight;
            this.connected = connected;
        }
    }

    public static Result findMST(Graph graph) {
        int vertices = graph.getVertices();
        List<Graph.Edge> edges = new ArrayList<>(graph.getEdges());

        edges.sort(Comparator.comparingInt(edge -> edge.weight));

        DSU dsu = new DSU(vertices);
        List<Graph.Edge> mst = new ArrayList<>();
        long totalWeight = 0;

        for (Graph.Edge edge : edges) {
            if (dsu.union(edge.from, edge.to)) {
                mst.add(edge);
                totalWeight += edge.weight;

                if (mst.size() == vertices - 1) {
                    break;
                }
            }
        }

        boolean connected = vertices == 0 || mst.size() == vertices - 1;

        return new Result(mst, totalWeight, connected);
    }
}
