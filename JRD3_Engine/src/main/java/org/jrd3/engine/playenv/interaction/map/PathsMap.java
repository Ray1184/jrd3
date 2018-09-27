package org.jrd3.engine.playenv.interaction.map;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import org.joml.Vector2f;
import org.jrd3.engine.core.exceptions.JRD3Exception;

import java.util.List;

import static org.jrd3.engine.playenv.interaction.map.Sector.testSidesOverlap;

/**
 * Walkmap extension for pathfinding. Sectors are managed in an weighted undirected graph.
 * Querying the paths map (with origin point and target point) the maps replies with the
 * next point 2D where the seeker entity must to go.
 * The paths map should overlap the collision map.
 *
 * @author Ray1184
 * @version 1.0
 */
public class PathsMap extends WalkMap {

    private static final long serialVersionUID = -1224045636889786659L;

    private transient HipsterGraph<Sector, Float> walkPath;



    /**
     * Default constructor.
     *
     * @param resourcePath The walkmap path.
     * @throws JRD3Exception
     */
    public PathsMap(String resourcePath) throws JRD3Exception {
        super(resourcePath);

    }

    /**
     * Initializes walkpath.
     *
     * @param sectors All sectors.
     * @return Undirected graph with walkable path.
     */
    private static HipsterGraph<Sector, Float> createWalkablePath(List<Sector> sectors) {
        GraphBuilder<Sector, Float> graphBuilder = GraphBuilder.create();

        // Take each sector.
        for (Sector sector : sectors) {
            Sector.Triangle t = sector.getTriangle();
            // Foreach sector test whether has a common side with others.
            for (Sector sectorTest : sectors) {
                Sector.Triangle o = sectorTest.getTriangle();
                if (testSidesOverlap(t, o, Sector.Sides.A) || testSidesOverlap(t, o, Sector.Sides.B)
                        || testSidesOverlap(t, o, Sector.Sides.C)) {
                    // Calculate the distance and use as weight.
                    float dist = Math.abs(t.center2f.distance(o.center2f));
                    graphBuilder.connect(sector).to(sectorTest).withEdge(dist);
                }
            }
        }
        return graphBuilder.createUndirectedGraph();
    }

    /**
     * Initializes walk path.
     */
    public void initWalkPath() {
        walkPath = createWalkablePath(sectors);
    }

    /**
     * Creates the shortest path between start sector and target sector.
     *
     * @param start  The starting sector.
     * @param target The target sector.
     * @param path   Where the whole path is stored.
     */
    @SuppressWarnings("unchecked")
    public void calculateShortestPath(Sector start, Sector target, List<Sector> path) {

        if (start == null || target == null || path == null) {
            return;
        }

        path.clear();

        // Try to cast a ray from start and target sector. If the ray
        // doesn't touch any external side, so the path is made only by the target
        // sector.

        if (!MapGeomUtils.intersectAnyExternalSide(start.getTriangle().center2f,
                target.getTriangle().center2f, this)) {
            path.add(target);
            return;
        }


        // Otherwise the complete path is calculated using Dijkstra algorithm.

        SearchProblem p = GraphSearchProblem
                .startingFrom(start)
                .in(walkPath)
                .takeCostsFromEdges()
                .build();
        Algorithm.SearchResult res = Hipster.createDijkstra(p).search(target);

        List<WeightedNode<Float, Sector, Double>> nonOptimizedPath = res.getGoalNode().path();

        // The path could be optimized. Starting from the start sector all next sectors,
        // are visited and a ray will be casted from start to next. If ray from start to next
        // doesn't touch any external side, let's try to cast a ray from start and next+1, until
        // a collision will not be found at next+n. In this case next + (n - 1) will be added
        // as first good next sector. The process is repeated from this sector to target.

        if (!nonOptimizedPath.isEmpty()) {
            optimizePath(nonOptimizedPath, 0, path);

        }

    }

    /**
     * Recursive path optimization.
     *
     * @param nonOptimizedPath The previous calculated path.
     * @param startingIndex    The starting index.
     * @param path             Where the optimized path is stored.
     */
    private void optimizePath(List<WeightedNode<Float, Sector, Double>> nonOptimizedPath,
                              int startingIndex, List<Sector> path) {


        // TODO - Redesign method in safe way (avoid Stack Overflow)
        Sector current = nonOptimizedPath.get(startingIndex).state();

        for (int i = startingIndex; i < nonOptimizedPath.size(); i++) {

            if (MapGeomUtils.intersectAnyExternalSide(current.getTriangle().center2f,
                    nonOptimizedPath.get(i).state().getTriangle().center2f, this)) {
                path.add(nonOptimizedPath.get(i).state());
                optimizePath(nonOptimizedPath, i, path);
            }
        }


    }


}
