
import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Experiment {
    private static final int[] SIZES = {10, 100, 1000};
    private static final int REPEATS = 5;
    private static final long SEED = 42;

    public static void main(String[] args) throws IOException {
        new File("results").mkdirs();

        try (FileWriter writer = new FileWriter("results/results.csv")) {
            writer.write("algorithm,vertices,edges,averageTimeNs,operations\n");

            for (int vertices : SIZES) {
                int edges = vertices == 10 ? 30 : vertices == 100 ? 500 : 5000;

                Graph undirected = createUndirectedGraph(vertices, edges, SEED + vertices);
                Graph directed = createDirectedGraph(vertices, edges, SEED + vertices);
                Graph dag = createDAG(vertices, edges, SEED + vertices);

                measureMST(writer, undirected, "Kruskal");
                measureMST(writer, undirected, "Prim");
                measureDijkstra(writer, directed);
                measureSCC(writer, directed);
                measureTopologicalSort(writer, dag, "TopologicalSortDAG");
                measureTopologicalSort(writer, directed, "TopologicalSortCyclic");
            }
        }

        System.out.println("Experiments completed. Results saved to results/results.csv");
    }

    private static Graph createUndirectedGraph(int vertices, int edges, long seed) {
        Graph graph = new Graph(vertices);
        Random random = new Random(seed);
        Set<String> used = new HashSet<>();

        for (int i = 1; i < vertices; i++) {
            addUndirectedEdge(graph, used, i - 1, i, random.nextInt(100) + 1);
        }

        while (used.size() < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);

            if (from != to) {
                addUndirectedEdge(graph, used, from, to, random.nextInt(100) + 1);
            }
        }

        return graph;
    }

    private static void addUndirectedEdge(Graph graph, Set<String> used,
                                          int from, int to, int weight) {
        int a = Math.min(from, to);
        int b = Math.max(from, to);
        String key = a + "-" + b;

        if (used.add(key)) {
            graph.addUndirectedEdge(from, to, weight);
        }
    }

    private static Graph createDirectedGraph(int vertices, int edges, long seed) {
        Graph graph = new Graph(vertices);
        Random random = new Random(seed);
        Set<String> used = new HashSet<>();

        while (used.size() < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);

            if (from != to && used.add(from + "-" + to)) {
                graph.addDirectedEdge(from, to, random.nextInt(100) + 1);
            }
        }

        return graph;
    }

    private static Graph createDAG(int vertices, int edges, long seed) {
        Graph graph = new Graph(vertices);
        Random random = new Random(seed);
        Set<String> used = new HashSet<>();

        for (int i = 1; i < vertices; i++) {
            graph.addDirectedEdge(i - 1, i, 1);
            used.add((i - 1) + "-" + i);
        }

        while (used.size() < edges) {
            int from = random.nextInt(vertices);
            int to = random.nextInt(vertices);

            if (from < to && used.add(from + "-" + to)) {
                graph.addDirectedEdge(from, to, random.nextInt(100) + 1);
            }
        }

        return graph;
    }

    private static void measureMST(FileWriter writer, Graph graph,
                                   String algorithm) throws IOException {
        long totalTime = 0;

        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();

            if (algorithm.equals("Kruskal")) {
                Kruskal.findMST(graph);
            } else {
                Prim.findMST(graph);
            }

            totalTime += System.nanoTime() - start;
        }

        writeResult(writer, algorithm, graph.getVertices(),
                graph.getEdges().size() / 2, totalTime, graph.getEdges().size());
    }

    private static void measureDijkstra(FileWriter writer, Graph graph) throws IOException {
        long totalTime = 0;

        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();
            Dijkstra.findShortestPaths(graph, 0);
            totalTime += System.nanoTime() - start;
        }

        writeResult(writer, "Dijkstra", graph.getVertices(),
                graph.getEdges().size(), totalTime, graph.getEdges().size());
    }

    private static void measureSCC(FileWriter writer, Graph graph) throws IOException {
        long totalTime = 0;

        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();
            SCC.find(graph);
            totalTime += System.nanoTime() - start;
        }

        writeResult(writer, "SCC", graph.getVertices(),
                graph.getEdges().size(), totalTime, graph.getEdges().size());
    }

    private static void measureTopologicalSort(FileWriter writer, Graph graph,
                                               String algorithm) throws IOException {
        long totalTime = 0;

        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();
            TopologicalSort.sort(graph);
            totalTime += System.nanoTime() - start;
        }

        writeResult(writer, algorithm, graph.getVertices(),
                graph.getEdges().size(), totalTime, graph.getEdges().size());
    }

    private static void writeResult(FileWriter writer, String algorithm,
                                    int vertices, int edges,
                                    long totalTime, long operations) throws IOException {
        writer.write(algorithm + "," + vertices + "," + edges + ","
                + totalTime / REPEATS + "," + operations + "\n");
    }
}
