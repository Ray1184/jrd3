package org.jrd3.engine.core.loaders.binary;

import org.jrd3.engine.core.exceptions.JRD3Exception;
import org.jrd3.engine.core.utils.CommonUtils;
import org.jrd3.engine.playenv.interaction.map.PathsMap;
import org.jrd3.engine.playenv.interaction.map.ViewMap;
import org.jrd3.engine.playenv.interaction.map.WalkMap;

import java.io.*;

/**
 * Fast loader for binary files.
 *
 * @author Ray1184
 * @version 1.0
 */
public class BinaryFileLoader {

    /**
     * Loads a JRD3M walk map.
     *
     * @param path The source path.
     * @return The walk map.
     * @throws JRD3Exception
     */
    public static final WalkMap loadWalkMap(String path) throws JRD3Exception {
        try {
            InputStream is = CommonUtils.class.getClass().getResourceAsStream(path);
            if (is == null) {
                is = new FileInputStream(path);
            }
            InputStream buffer = new BufferedInputStream(is);
            ObjectInput input = new ObjectInputStream(buffer);
            return (WalkMap) input.readObject();
        } catch (IOException e) {
            throw new JRD3Exception(e);
        } catch (ClassNotFoundException e2) {
            throw new JRD3Exception(e2);
        }

    }

    /**
     * Loads a JRD3M view map.
     *
     * @param path The source path.
     * @return The view map.
     * @throws JRD3Exception
     */
    public static final ViewMap loadViewMap(String path) throws JRD3Exception {
        try {
            InputStream is = CommonUtils.class.getClass().getResourceAsStream(path);
            if (is == null) {
                is = new FileInputStream(path);
            }
            InputStream buffer = new BufferedInputStream(is);
            ObjectInput input = new ObjectInputStream(buffer);
            ViewMap vm = (ViewMap) input.readObject();
            vm.bindImages();
            return vm;
        } catch (IOException e) {
            throw new JRD3Exception(e);
        } catch (ClassNotFoundException e2) {
            throw new JRD3Exception(e2);
        }
    }

    /**
     * Loads a JRD3M path map.
     *
     * @param path The source path.
     * @return The path map.
     * @throws JRD3Exception
     */
    public static final PathsMap loadPathsMap(String path) throws JRD3Exception {
        try {
            InputStream is = CommonUtils.class.getClass().getResourceAsStream(path);
            if (is == null) {
                is = new FileInputStream(path);
            }
            InputStream buffer = new BufferedInputStream(is);
            ObjectInput input = new ObjectInputStream(buffer);
            PathsMap pm = (PathsMap) input.readObject();
            pm.initWalkPath();
            return pm;
        } catch (IOException e) {
            throw new JRD3Exception(e);
        } catch (ClassNotFoundException e2) {
            throw new JRD3Exception(e2);
        }
    }
}
