
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
        public final long operations;

        public Result(List<Graph.Edge> edges, long totalWeight,
                      boolean connected, long operations) {
            this.edges = edges;
            this.totalWeight = totalWeight;
            this.connected = connected;
            this.operations = operations;
        }
    }

    public static Result findMST(Graph graph) {
        int vertices = graph.getVertices();
        List<Graph.Edge> edges = new ArrayList<>();

        for (Graph.Edge edge : graph.getEdges()) {
            if (edge.from < edge.to) {
                edges.add(edge);
            }
        }

        edges.sort(Comparator.comparingInt(edge -> edge.weight));

        DSU dsu = new DSU(vertices);
        List<Graph.Edge> mst = new ArrayList<>();
        long totalWeight = 0;
        long operations = edges.size();

        for (Graph.Edge edge : edges) {
            operations++;

            if (dsu.union(edge.from, edge.to)) {
                mst.add(edge);
                totalWeight += edge.weight;

                if (mst.size() == vertices - 1) {
                    break;
                }
            }
        }

        boolean connected = vertices == 0 || mst.size() == vertices - 1;
        operations += dsu.getOperations();

        return new Result(mst, totalWeight, connected, operations);
    }
}
