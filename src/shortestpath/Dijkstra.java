
package shortestpath;

import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    public static class Result {
        public final long[] distances;
        public final int[] previous;

        public Result(long[] distances, int[] previous) {
            this.distances = distances;
            this.previous = previous;
        }

        public List<Integer> getPath(int target) {
            List<Integer> path = new ArrayList<>();

            if (distances[target] == Long.MAX_VALUE) {
                return path;
            }

            for (int vertex = target; vertex != -1; vertex = previous[vertex]) {
                path.add(vertex);
            }

            Collections.reverse(path);
            return path;
        }
    }

    private static class Node {
        int vertex;
        long distance;

        Node(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public static Result findShortestPaths(Graph graph, int source) {
        int vertices = graph.getVertices();

        if (source < 0 || source >= vertices) {
            throw new IllegalArgumentException("Invalid source vertex");
        }

        long[] distances = new long[vertices];
        int[] previous = new int[vertices];

        Arrays.fill(distances, Long.MAX_VALUE);
        Arrays.fill(previous, -1);

        PriorityQueue<Node> queue = new PriorityQueue<>(
                (a, b) -> Long.compare(a.distance, b.distance)
        );

        distances[source] = 0;
        queue.add(new Node(source, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.distance != distances[current.vertex]) {
                continue;
            }

            for (Graph.Edge edge : graph.getNeighbors(current.vertex)) {
                if (edge.weight < 0) {
                    throw new IllegalArgumentException("Negative weights are not supported");
                }

                long newDistance = current.distance + edge.weight;

                if (newDistance < distances[edge.to]) {
                    distances[edge.to] = newDistance;
                    previous[edge.to] = current.vertex;
                    queue.add(new Node(edge.to, newDistance));
                }
            }
        }

        return new Result(distances, previous);
    }
}
