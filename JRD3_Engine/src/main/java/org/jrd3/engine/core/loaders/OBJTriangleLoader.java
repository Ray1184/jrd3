package org.jrd3.engine.core.loaders;

import org.joml.Vector3f;
import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class OBJTriangleLoader {


    public static List<Triangle> loadTriangleList(String fileName) throws JRD3Exception {
        List<String> lines = CommonUtils.readAllLines(fileName);

        List<Triangle> triangles = new ArrayList<>();
        List<Vector3f> vertices = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    // Geometric vertex
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;
                default:
                    // Ignore other lines
                    break;
            }
        }

        for (Face face : faces) {
            Vector3f p1 = new Vector3f(vertices.get(face.indexA).x, vertices.get(face.indexA).y, vertices.get(face.indexA).z);
            Vector3f p2 = new Vector3f(vertices.get(face.indexB).x, vertices.get(face.indexB).y, vertices.get(face.indexB).z);
            Vector3f p3 = new Vector3f(vertices.get(face.indexC).x, vertices.get(face.indexC).y, vertices.get(face.indexC).z);
            Triangle t = new Triangle(p1, p2, p3);
            triangles.add(t);
        }
        return triangles;
    }

    public static class Triangle {
        public final Vector3f p1;

        public final Vector3f p2;

        public final Vector3f p3;

        public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }
    }


    private static class Face {

        private final int indexA;

        private final int indexB;

        private final int indexC;

        public Face(String tokenX, String tokenY, String tokenZ) throws JRD3Exception {
            indexA = processIndex(tokenX) - 1;
            indexB = processIndex(tokenY) - 1;
            indexC = processIndex(tokenZ) - 1;
        }

        int processIndex(String token) throws JRD3Exception {
            if (token != null) {
                int sIndex = token.indexOf("/");
                if (sIndex < 0) {
                    return Integer.parseInt(token);
                } else {
                    return Integer.parseInt(token.substring(0, sIndex));
                }
            }
            return 0;
        }

        /**
         * Getter for property 'indexA'.
         *
         * @return Value for property 'indexA'.
         */
        public int getIndexA() {
            return indexA;
        }


        /**
         * Getter for property 'indexB'.
         *
         * @return Value for property 'indexB'.
         */
        public int getIndexB() {
            return indexB;
        }


        /**
         * Getter for property 'indexC'.
         *
         * @return Value for property 'indexC'.
         */
        public int getIndexC() {
            return indexC;
        }
    }
}