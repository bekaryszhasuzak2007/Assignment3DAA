
package connectivity;

import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCC {
    public static List<List<Integer>> find(Graph graph) {
        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        List<Integer> order = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfsOrder(graph, i, visited, order);
            }
        }

        Graph reversed = new Graph(vertices);

        for (int i = 0; i < vertices; i++) {
            for (Graph.Edge edge : graph.getNeighbors(i)) {
                reversed.addDirectedEdge(edge.to, edge.from, edge.weight);
            }
        }

        Collections.fill(new ArrayList<Boolean>(), false);
        visited = new boolean[vertices];

        List<List<Integer>> components = new ArrayList<>();
        Collections.reverse(order);

        for (int vertex : order) {
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                dfsComponent(reversed, vertex, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfsOrder(Graph graph, int vertex, boolean[] visited, List<Integer> order) {
        visited[vertex] = true;

        for (Graph.Edge edge : graph.getNeighbors(vertex)) {
            if (!visited[edge.to]) {
                dfsOrder(graph, edge.to, visited, order);
            }
        }

        order.add(vertex);
    }

    private static void dfsComponent(Graph graph, int vertex, boolean[] visited, List<Integer> component) {
        visited[vertex] = true;
        component.add(vertex);

        for (Graph.Edge edge : graph.getNeighbors(vertex)) {
            if (!visited[edge.to]) {
                dfsComponent(graph, edge.to, visited, component);
            }
        }
    }
}
