
package connectivity;

import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
    public static class Result {
        public final List<Integer> order;
        public final long operations;

        public Result(List<Integer> order, long operations) {
            this.order = order;
            this.operations = operations;
        }
    }

    public static List<Integer> sort(Graph graph) {
        return sortWithOperations(graph).order;
    }

    public static Result sortWithOperations(Graph graph) {
        int vertices = graph.getVertices();
        int[] indegree = new int[vertices];
        long operations = 0;

        for (int i = 0; i < vertices; i++) {
            for (Graph.Edge edge : graph.getNeighbors(i)) {
                indegree[edge.to]++;
                operations++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < vertices; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            order.add(vertex);

            for (Graph.Edge edge : graph.getNeighbors(vertex)) {
                indegree[edge.to]--;
                operations++;

                if (indegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        if (order.size() != vertices) {
            return new Result(new ArrayList<>(), operations);
        }

        return new Result(order, operations);
    }
}
