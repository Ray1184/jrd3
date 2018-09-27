package org.jrd3.engine.playenv.interaction.map;

import org.joml.Vector2f;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.loaders.OBJTriangleLoader;
import org.jrd3.engine.playenv.data.InnerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic walkmap for base interaction and collision management.
 *
 * @author Ray1184
 * @version 1.0
 */
public class WalkMap implements Serializable {

    private static final long serialVersionUID = 5789943596330127870L;

    protected final List<Sector> sectors;

    protected final InnerData mapData;

    protected Sector current;

    protected final Map<String, Sector> sectorByName;

    /**
     * Default constructor.
     *
     * @param resourcePath The walkmap path.
     * @throws JRD3Exception
     */
    public WalkMap(String resourcePath) throws JRD3Exception {
        sectors = new ArrayList<>();
        mapData = new InnerData();
        sectorByName = new HashMap<>();
        List<OBJTriangleLoader.Triangle> triangles = OBJTriangleLoader.loadTriangleList(resourcePath);
        for (OBJTriangleLoader.Triangle t : triangles) {
            sectors.add(new Sector(Sector.objTriangleToSectorTriangle(t)));
        }
        for (Sector s : sectors) {
            s.setExternalSides(Sector.calculateExternalSides(s, sectors));
        }

    }

    /**
     * Checks whether a point is inside the walkmap.
     *
     * @param p     The point.
     * @param delta Threshold
     * @return The triangle where point is inside, null if is out of interaction.
     */
    public Sector sampleSector(Vector2f p, float delta) {
        return sampleSector(p.x, p.y, delta);
    }

    /**
     * Checks whether a point is inside the walkmap.
     *
     * @param x     The x.
     * @param y     The y.
     * @param delta Threshold
     * @return The triangle where point is inside, null if is out of interaction.
     */
    public Sector sampleSector(float x, float y, float delta) {
        // TODO - Implements threshold.
        // TODO - Implements also height.
        for (Sector sector : sectors) {
            Sector.Triangle triangle = sector.getTriangle();
            float dX = x - triangle.x3;
            float dY = y - triangle.z3;
            float dX21 = triangle.x3 - triangle.x2;
            float dY12 = triangle.z2 - triangle.z3;
            float d = dY12 * (triangle.x1 - triangle.x3) + dX21 * (triangle.z1 - triangle.z3);
            float s = dY12 * dX + dX21 * dY;
            float t = (triangle.z3 - triangle.z1) * dX + (triangle.x1 - triangle.x3) * dY;
            boolean inside;
            if (d < 0) {
                inside = s <= 0 && t <= 0 && s + t >= d;
            } else {
                inside = s >= 0 && t >= 0 && s + t <= d;
            }
            if (inside) {
                return sector;
            }
        }
        return null;
    }


    /**
     * Getter for property 'sectors'.
     *
     * @return Value for property 'sectors'.
     */
    public List<Sector> getSectors() {
        return sectors;
    }

    /**
     * Getter for property 'mapData'.
     *
     * @return Value for property 'mapData'.
     */
    public InnerData getMapData() {
        return mapData;
    }

    /**
     * Getter for property 'sectorByName'.
     *
     * @return Value for property 'sectorByName'.
     */
    public Map<String, Sector> getSectorByName() {
        return sectorByName;
    }
}
