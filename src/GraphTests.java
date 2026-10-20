
import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Graph;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

public class GraphTests {
    public static void main(String[] args) {
        testMST();
        testDisconnectedGraph();
        testTopologicalCycle();
        testSCC();
        testDijkstra();
        testSingleVertex();
        System.out.println("All tests passed.");
    }

    private static void testMST() {
        Graph graph = new Graph(4);
        graph.addUndirectedEdge(0, 1, 1);
        graph.addUndirectedEdge(1, 2, 2);
        graph.addUndirectedEdge(2, 3, 3);
        graph.addUndirectedEdge(0, 3, 10);
        graph.addUndirectedEdge(0, 1, 1);

        Kruskal.Result kruskal = Kruskal.findMST(graph);
        Kruskal.Result prim = Prim.findMST(graph);

        check(kruskal.connected, "MST should be connected");
        check(kruskal.totalWeight == 6, "Kruskal MST weight should be 6");
        check(prim.totalWeight == kruskal.totalWeight, "Prim and Kruskal should match");
    }

    private static void testDisconnectedGraph() {
        Graph graph = new Graph(3);
        graph.addUndirectedEdge(0, 1, 5);

        check(!Kruskal.findMST(graph).connected, "Disconnected graph should be detected");
        check(!Prim.findMST(graph).connected, "Prim should detect disconnected graph");
    }

    private static void testTopologicalCycle() {
        Graph graph = new Graph(3);
        graph.addDirectedEdge(0, 1, 1);
        graph.addDirectedEdge(1, 2, 1);
        graph.addDirectedEdge(2, 0, 1);

        check(TopologicalSort.sort(graph).isEmpty(), "Cycle should prevent topological ordering");
    }

    private static void testSCC() {
        Graph graph = new Graph(4);
        graph.addDirectedEdge(0, 1, 1);
        graph.addDirectedEdge(1, 0, 1);
        graph.addDirectedEdge(1, 2, 1);
        graph.addDirectedEdge(2, 3, 1);
        graph.addDirectedEdge(3, 2, 1);

        check(SCC.find(graph).size() == 2, "Graph should contain two SCCs");
    }

    private static void testDijkstra() {
        Graph graph = new Graph(4);
        graph.addDirectedEdge(0, 1, 4);
        graph.addDirectedEdge(0, 2, 1);
        graph.addDirectedEdge(2, 1, 2);
        graph.addDirectedEdge(1, 3, 1);

        Dijkstra.Result result = Dijkstra.findShortestPaths(graph, 0);

        check(result.distances[1] == 3, "Distance to vertex 1 should be 3");
        check(result.distances[3] == 4, "Distance to vertex 3 should be 4");
    }

    private static void testSingleVertex() {
        Graph graph = new Graph(1);

        check(Kruskal.findMST(graph).connected, "Single-vertex MST should be connected");
        check(Prim.findMST(graph).connected, "Single-vertex Prim should be connected");

        Dijkstra.Result result = Dijkstra.findShortestPaths(graph, 0);
        check(result.distances[0] == 0, "Distance to source should be zero");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
