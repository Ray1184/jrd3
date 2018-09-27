package org.jrd3.engine.playenv.serialization;

import java.io.Serializable;

/**
 * RGBA data struct.
 *
 * @author Ray1184
 * @version 1.0
 */

public class RGBA implements Serializable {

    private static final long serialVersionUID = -3280709911235693442L;

    public int r;

    public int g;

    public int b;

    public int a;


    public RGBA(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }


    public RGBA(RGBA c) {
        r = c.r;
        g = c.g;
        b = c.b;
        a = c.a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RGBA rgba = (RGBA) o;

        if (r != rgba.r) return false;
        if (g != rgba.g) return false;
        if (b != rgba.b) return false;
        return a == rgba.a;
    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + g;
        result = 31 * result + b;
        result = 31 * result + a;
        return result;
    }
}
