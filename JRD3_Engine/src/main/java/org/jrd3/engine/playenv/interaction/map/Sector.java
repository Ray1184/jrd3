package org.jrd3.engine.playenv.interaction.map;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jrd3.engine.core.loaders.OBJTriangleLoader;
import org.jrd3.engine.playenv.data.InnerData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Sector of walkmap, with geometric and logical properties.
 *
 * @author Ray1184
 * @version 1.0
 */
public class Sector implements Serializable {

    private static final long serialVersionUID = -7630728170647419659L;

    private final Triangle triangle;

    private String name;

    private List<Side> externalSides;

    private final InnerData sectorData;

    public Sector(Triangle triangle) {
        this.triangle = triangle;
        externalSides = new ArrayList<>();
        sectorData = new InnerData();
    }

    /**
     * Calculates all external sides.
     *
     * @param sector  The current sector.
     * @param sectors All the sectors.
     * @return The external sides of sector.
     */
    public static List<Side> calculateExternalSides(Sector sector, List<Sector> sectors) {

        Triangle t = sector.getTriangle();

        List<Triangle> triangles = new ArrayList<>(sectors.size());

        for (Sector s : sectors) {
            triangles.add(s.getTriangle());
        }

        List<Side> externalSides = new ArrayList<>();

        boolean check12 = false;
        boolean check23 = false;
        boolean check31 = false;
        for (Triangle o : triangles) {
            if (o.equals(t)) {
                continue; // It's the triangle itself.
            }
            check12 = check12 || Sector.testSidesOverlap(t, o, Sides.A);
            check23 = check23 || Sector.testSidesOverlap(t, o, Sides.B);
            check31 = check31 || Sector.testSidesOverlap(t, o, Sides.C);
        }

        // If no check then triangle is alone, should not happens.

        if (!check12) {
            externalSides.add(new Side(t.x1, t.z1, t.x2, t.z2));
        }

        if (!check23) {
            externalSides.add(new Side(t.x2, t.z2, t.x3, t.z3));
        }

        if (!check31) {
            externalSides.add(new Side(t.x3, t.z3, t.x1, t.z1));
        }

        return externalSides;

    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * TestRetro whether two triangles have a common side.
     *
     * @param t    The first triangle.
     * @param o    The second triangle.
     * @param side The side of the first triangle to test.
     * @return <code>true</code> whether the side of the first triangle overlaps one
     * side of the second, <code>false</code> otherwise.
     */
    public static boolean testSidesOverlap(Triangle t, Triangle o, Sides side) {
        switch (side) {
            case A:
                return (t.x1 == o.x2 && t.z1 == o.z2 && t.y1 == o.y2 && t.x2 == o.x1 && t.z2 == o.z1 && t.y2 == o.y1) ||
                        (t.x1 == o.x1 && t.z1 == o.z1 && t.y1 == o.y1 && t.x2 == o.x3 && t.z2 == o.z3 && t.y2 == o.y3) ||
                        (t.x1 == o.x3 && t.z1 == o.z3 && t.y1 == o.y3 && t.x2 == o.x2 && t.z2 == o.z2 && t.y2 == o.y2);
            case B:
                return (t.x2 == o.x2 && t.z2 == o.z2 && t.y2 == o.y2 && t.x3 == o.x1 && t.z3 == o.z1 && t.y3 == o.y1) ||
                        (t.x2 == o.x1 && t.z2 == o.z1 && t.y2 == o.y1 && t.x3 == o.x3 && t.z3 == o.z3 && t.y3 == o.y3) ||
                        (t.x2 == o.x3 && t.z2 == o.z3 && t.y2 == o.y2 && t.x3 == o.x2 && t.z3 == o.z2 && t.y3 == o.y2);
            case C:
                return (t.x3 == o.x2 && t.z3 == o.z2 && t.y3 == o.y2 && t.x1 == o.x1 && t.z1 == o.z1 && t.y1 == o.y1) ||
                        (t.x3 == o.x1 && t.z3 == o.z1 && t.y3 == o.y1 && t.x1 == o.x3 && t.z1 == o.z3 && t.y1 == o.y3) ||
                        (t.x3 == o.x3 && t.z3 == o.z3 && t.y3 == o.y3 && t.x1 == o.x2 && t.z1 == o.z2 && t.y1 == o.y2);
            default:
                return false;
        }
    }

    /**
     * Model to model.
     *
     * @param triangle OBJ triangles.
     * @return Sector triangles.
     */
    public static Triangle objTriangleToSectorTriangle(OBJTriangleLoader.Triangle triangle) {
        return new Triangle(triangle.p1, triangle.p2, triangle.p3);
    }

    /**
     * Getter for property 'sectorData'.
     *
     * @return Value for property 'sectorData'.
     */
    public InnerData getSectorData() {
        return sectorData;
    }

    /**
     * Getter for property 'triangle'.
     *
     * @return Value for property 'triangle'.
     */
    public Triangle getTriangle() {
        return triangle;
    }

    /**
     * Getter for property 'externalSides'.
     *
     * @return Value for property 'externalSides'.
     */
    public List<Side> getExternalSides() {
        return externalSides;
    }


    /**
     * 3 sides of triangle.
     */
    public enum Sides {
        A,
        B,
        C
    }

    /**
     * Setter for property 'externalSides'.
     *
     * @param externalSides Value to set for property 'externalSides'.
     */
    public void setExternalSides(List<Side> externalSides) {
        this.externalSides = externalSides;
    }

    /**
     * Triangle inner class.
     */
    public static class Triangle implements Serializable {

        private static final long serialVersionUID = 6152353280047360892L;

        public float x1;
        public float y1;
        public float z1;
        public float x2;
        public float y2;
        public float z2;
        public float x3;
        public float y3;
        public float z3;

        public Vector3f center3f;
        public Vector2f center2f;

        public Triangle(Vector3f a, Vector3f b, Vector3f c) {
            x1 = a.x;
            y1 = a.y;
            z1 = a.z;
            x2 = b.x;
            y2 = b.y;
            z2 = b.z;
            x3 = c.x;
            y3 = c.y;
            z3 = c.z;
            center3f = new Vector3f((x1 + x2 + x3) / 3, (y1 + y2 + y3) / 3, (z1 + z2 + z3) / 3);
            center2f = new Vector2f((x1 + x2 + x3) / 3, (z1 + z2 + z3) / 3);
        }

        public float calcHeightByCoords(Vector2f pos) {
            float det = (z2 - z3) * (x1 - x3) + (x3 - x2) * (z1 - z3);
            float l1 = ((z2 - z3) * (pos.x - x3) + (x3 - x2) * (pos.y - z3)) / det;
            float l2 = ((z3 - z1) * (pos.x - x3) + (x1 - x3) * (pos.y - z3)) / det;
            float l3 = 1.0f - l1 - l2;
            return l1 * y1 + l2 * y2 + l3 * y3;
        }

        public float calcHeightByCoords(Vector3f pos) {
            float det = (z2 - z3) * (x1 - x3) + (x3 - x2) * (z1 - z3);
            float l1 = ((z2 - z3) * (pos.x - x3) + (x3 - x2) * (pos.z - z3)) / det;
            float l2 = ((z3 - z1) * (pos.x - x3) + (x1 - x3) * (pos.z - z3)) / det;
            float l3 = 1.0f - l1 - l2;
            return l1 * y1 + l2 * y2 + l3 * y3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Triangle triangle = (Triangle) o;

            if (Float.compare(triangle.x1, x1) != 0) return false;
            if (Float.compare(triangle.y1, y1) != 0) return false;
            if (Float.compare(triangle.z1, z1) != 0) return false;
            if (Float.compare(triangle.x2, x2) != 0) return false;
            if (Float.compare(triangle.y2, y2) != 0) return false;
            if (Float.compare(triangle.z2, z2) != 0) return false;
            if (Float.compare(triangle.x3, x3) != 0) return false;
            if (Float.compare(triangle.y3, y3) != 0) return false;
            if (Float.compare(triangle.z3, z3) != 0) return false;
            if (center3f != null ? !center3f.equals(triangle.center3f) : triangle.center3f != null) return false;
            return center2f != null ? center2f.equals(triangle.center2f) : triangle.center2f == null;
        }

        @Override
        public int hashCode() {
            int result = (x1 != +0.0f ? Float.floatToIntBits(x1) : 0);
            result = 31 * result + (y1 != +0.0f ? Float.floatToIntBits(y1) : 0);
            result = 31 * result + (z1 != +0.0f ? Float.floatToIntBits(z1) : 0);
            result = 31 * result + (x2 != +0.0f ? Float.floatToIntBits(x2) : 0);
            result = 31 * result + (y2 != +0.0f ? Float.floatToIntBits(y2) : 0);
            result = 31 * result + (z2 != +0.0f ? Float.floatToIntBits(z2) : 0);
            result = 31 * result + (x3 != +0.0f ? Float.floatToIntBits(x3) : 0);
            result = 31 * result + (y3 != +0.0f ? Float.floatToIntBits(y3) : 0);
            result = 31 * result + (z3 != +0.0f ? Float.floatToIntBits(z3) : 0);
            result = 31 * result + (center3f != null ? center3f.hashCode() : 0);
            result = 31 * result + (center2f != null ? center2f.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Triangle{");
            sb.append("x1=").append(x1);
            sb.append(", y1=").append(y1);
            sb.append(", z1=").append(z1);
            sb.append(", x2=").append(x2);
            sb.append(", y2=").append(y2);
            sb.append(", z2=").append(z2);
            sb.append(", x3=").append(x3);
            sb.append(", y3=").append(y3);
            sb.append(", z3=").append(z3);
            sb.append(", center3f=").append(center3f);
            sb.append(", center2f=").append(center2f);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Side implements Serializable {

        private static final long serialVersionUID = -3799409109522055535L;

        public Vector2f p1;
        public Vector2f p2;

        public Side(float x1, float y1, float x2, float y2) {
            this(new Vector2f(x1, y1), new Vector2f(x2, y2));
        }

        public Side(Vector2f p1, Vector2f p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Side side = (Side) o;

            if (p1 != null ? !p1.equals(side.p1) : side.p1 != null) return false;
            return p2 != null ? p2.equals(side.p2) : side.p2 == null;
        }

        @Override
        public int hashCode() {
            int result = p1 != null ? p1.hashCode() : 0;
            result = 31 * result + (p2 != null ? p2.hashCode() : 0);
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Side{");
            sb.append("p1=").append(p1);
            sb.append(", p2=").append(p2);
            sb.append('}');
            return sb.toString();
        }
    }


}
