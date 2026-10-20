
package mst;

import graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    public static Kruskal.Result findMST(Graph graph) {
        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        List<Graph.Edge> mst = new ArrayList<>();
        PriorityQueue<Graph.Edge> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(a.weight, b.weight)
        );

        long totalWeight = 0;
        int start = 0;

        if (vertices == 0) {
            return new Kruskal.Result(mst, 0, true);
        }

        visited[start] = true;
        queue.addAll(graph.getNeighbors(start));

        while (!queue.isEmpty() && mst.size() < vertices - 1) {
            Graph.Edge edge = queue.poll();

            if (visited[edge.to]) {
                continue;
            }

            visited[edge.to] = true;
            mst.add(edge);
            totalWeight += edge.weight;

            for (Graph.Edge next : graph.getNeighbors(edge.to)) {
                if (!visited[next.to]) {
                    queue.add(next);
                }
            }
        }

        boolean connected = mst.size() == vertices - 1;

        return new Kruskal.Result(mst, totalWeight, connected);
    }
}
