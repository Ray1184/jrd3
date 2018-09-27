package org.jrd3.engine.playenv.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Inner data for scene elements.
 *
 * @author Ray1184
 * @version 1.0
 */
public class InnerData implements Serializable {

    private static final long serialVersionUID = -3280289916295696442L;

    private final Map<String, String> stringData;

    private final Map<String, Integer> integerData;

    private final Map<String, Float> floatData;

    private final Map<String, Serializable> genericData;


    /**
     * Default constructor.
     */
    public InnerData() {
        stringData = new HashMap<>();
        integerData = new HashMap<>();
        floatData = new HashMap<>();
        genericData = new HashMap<>();

    }


    public void removeString(String key) {
        stringData.remove(key);
    }

    public void removeInteger(String key) {
        integerData.remove(key);
    }

    public void removeFloat(String key) {
        floatData.remove(key);
    }

    public void removeGeneric(String key) {
        genericData.remove(key);
    }


    public void setString(String key, String value) {
        stringData.put(key, value);
    }

    public void setInteger(String key, Integer value) {
        integerData.put(key, value);
    }

    public void setFloat(String key, Float value) {
        floatData.put(key, value);
    }

    public void setGeneric(String key, Serializable value) {
        genericData.put(key, value);
    }



    public Serializable getGeneric(String key) {
        return genericData.get(key);
    }


    public String getString(String key) {
        return stringData.get(key);
    }

    public Integer getInteger(String key) {
        return integerData.get(key);
    }

    public Float getFloat(String key) {
        return floatData.get(key);
    }


}
