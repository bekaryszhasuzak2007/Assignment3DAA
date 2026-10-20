
package connectivity;

import graph.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCC {
    public static class Result {
        public final List<List<Integer>> components;
        public final long operations;

        public Result(List<List<Integer>> components, long operations) {
            this.components = components;
            this.operations = operations;
        }
    }

    public static List<List<Integer>> find(Graph graph) {
        return findWithOperations(graph).components;
    }

    public static Result findWithOperations(Graph graph) {
        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        List<Integer> order = new ArrayList<>();
        long[] operations = {0};

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfsOrder(graph, i, visited, order, operations);
            }
        }

        Graph reversed = new Graph(vertices);

        for (int i = 0; i < vertices; i++) {
            for (Graph.Edge edge : graph.getNeighbors(i)) {
                reversed.addDirectedEdge(edge.to, edge.from, edge.weight);
            }
        }

        visited = new boolean[vertices];
        List<List<Integer>> components = new ArrayList<>();
        Collections.reverse(order);

        for (int vertex : order) {
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                dfsComponent(reversed, vertex, visited, component, operations);
                components.add(component);
            }
        }

        return new Result(components, operations[0]);
    }

    private static void dfsOrder(
            Graph graph, int vertex, boolean[] visited,
            List<Integer> order, long[] operations) {
        visited[vertex] = true;

        for (Graph.Edge edge : graph.getNeighbors(vertex)) {
            operations[0]++;
            if (!visited[edge.to]) {
                dfsOrder(graph, edge.to, visited, order, operations);
            }
        }

        order.add(vertex);
    }

    private static void dfsComponent(
            Graph graph, int vertex, boolean[] visited,
            List<Integer> component, long[] operations) {
        visited[vertex] = true;
        component.add(vertex);

        for (Graph.Edge edge : graph.getNeighbors(vertex)) {
            operations[0]++;
            if (!visited[edge.to]) {
                dfsComponent(graph, edge.to, visited, component, operations);
            }
        }
    }
}
