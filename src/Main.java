
import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

public class Main {
    public static void main(String[] args) {
        testMST();
        testTopologicalSort();
        testSCC();
        testDijkstra();
    }

    private static void testMST() {
        Graph graph = new Graph(4);
        graph.addUndirectedEdge(0, 1, 10);
        graph.addUndirectedEdge(0, 2, 6);
        graph.addUndirectedEdge(0, 3, 5);
        graph.addUndirectedEdge(1, 3, 15);
        graph.addUndirectedEdge(2, 3, 4);

        Kruskal.Result kruskal = Kruskal.findMST(graph);
        Kruskal.Result prim = Prim.findMST(graph);

        System.out.println("Kruskal MST weight: " + kruskal.totalWeight);
        System.out.println("Prim MST weight: " + prim.totalWeight);
        System.out.println("MST connected: " + kruskal.connected);
        System.out.println();
    }

    private static void testTopologicalSort() {
        Graph graph = new Graph(6);
        graph.addDirectedEdge(5, 2, 1);
        graph.addDirectedEdge(5, 0, 1);
        graph.addDirectedEdge(4, 0, 1);
        graph.addDirectedEdge(4, 1, 1);
        graph.addDirectedEdge(2, 3, 1);
        graph.addDirectedEdge(3, 1, 1);

        System.out.println("Topological order: " + TopologicalSort.sort(graph));
        System.out.println();
    }

    private static void testSCC() {
        Graph graph = new Graph(5);
        graph.addDirectedEdge(0, 2, 1);
        graph.addDirectedEdge(2, 1, 1);
        graph.addDirectedEdge(1, 0, 1);
        graph.addDirectedEdge(0, 3, 1);
        graph.addDirectedEdge(3, 4, 1);

        System.out.println("Strongly connected components: " + SCC.find(graph));
        System.out.println();
    }

    private static void testDijkstra() {
        Graph graph = new Graph(5);
        graph.addDirectedEdge(0, 1, 4);
        graph.addDirectedEdge(0, 2, 1);
        graph.addDirectedEdge(2, 1, 2);
        graph.addDirectedEdge(1, 3, 1);
        graph.addDirectedEdge(2, 3, 5);

        Dijkstra.Result result = Dijkstra.findShortestPaths(graph, 0);


        for (int i = 0; i < result.distances.length; i++) {
            if (result.distances[i] == Long.MAX_VALUE) {
                System.out.println("Distance to " + i + ": UNREACHABLE");
                System.out.println("Path: []");
            } else {
                System.out.println("Distance to " + i + ": " + result.distances[i]);
                System.out.println("Path: " + result.getPath(i));
            }
        }

    }
}
