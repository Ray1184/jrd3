package org.jrd3.engine.playenv.interaction.map;

import org.joml.Vector2f;

import java.util.List;

/**
 * Some walkmap utilities.
 *
 * @author Ray1184
 * @version 1.0
 */
public class MapGeomUtils {


    /**
     * Cast a ray from starting point to target, checking whether the ray goes out of map bounds.
     *
     * @param startingPoint The starting point.
     * @param target        The target point.
     * @param map           The current map.
     * @return <code>true</code> if the vector traced has a collision with any external side,
     * <code>false</code> otherwise.
     */
    public static boolean intersectAnyExternalSide(Vector2f startingPoint, Vector2f target,
                                                   WalkMap map) {
        for (Sector sector : map.getSectors()) {
            List<Sector.Side> sides = sector.getExternalSides();
            for (Sector.Side side : sides) {
                if (intersectSegmentToSegment(startingPoint, target, side.p1, side.p2)) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * Determines whether two segments have one intersection point.
     *
     * @param s1 Starting segment 1.
     * @param e1 Ending segment 1.
     * @param s2 Starting segment 2
     * @param e2 Ending segment 2.
     * @return <code>true</code> if there's an intersection,
     * <code>false</code> otherwise.
     */
    public static boolean intersectSegmentToSegment(Vector2f s1, Vector2f e1, Vector2f s2, Vector2f e2) {
        return MapGeomUtils.linesIntersect(s1.x, s1.y, e1.x, e1.y, s2.x, s2.y, e2.x, e2.y);

    }

    private static boolean linesIntersect(float x1, float y1,
                                          float x2, float y2,
                                          float x3, float y3,
                                          float x4, float y4) {
        return ((MapGeomUtils.relativeCCW(x1, y1, x2, y2, x3, y3) *
                MapGeomUtils.relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
                && (MapGeomUtils.relativeCCW(x3, y3, x4, y4, x1, y1) *
                MapGeomUtils.relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
    }

    private static int relativeCCW(float x1, float y1,
                                   float x2, float y2,
                                   float px, float py) {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        float ccw = px * y2 - py * x2;
        if (ccw == 0.0f) {
            // The point is colinear, classify based on which side of
            // the segment the point falls on.  We can calculate a
            // relative value using the projection of px,py onto the
            // segment - a negative value indicates the point projects
            // outside of the segment in the direction of the particular
            // endpoint used as the origin for the projection.
            ccw = px * x2 + py * y2;
            if (ccw > 0.0f) {
                // Reverse the projection to be relative to the original x2,y2
                // x2 and y2 are simply negated.
                // px and py need to have (x2 - x1) or (y2 - y1) subtracted
                //    from them (based on the original values)
                // Since we really want to get a positive answer when the
                //    point is "beyond (x2,y2)", then we want to calculate
                //    the inverse anyway - thus we leave x2 & y2 negated.
                px -= x2;
                py -= y2;
                ccw = px * x2 + py * y2;
                if (ccw < 0.0f) {
                    ccw = 0.0f;
                }
            }
        }
        return Float.compare(ccw, 0.0f);
    }


}

