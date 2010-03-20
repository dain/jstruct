/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

public abstract class StructInfo<T extends Struct> {
    private final Class<T> type;
    private final int size;

    public StructInfo(Class<T> type, int size) {
        this.type = type;
        this.size = size;
    }

    public Class<T> getType() {
        return type;
    }

    public int size() {
        return size;
    }

    public abstract Region getPointer(T instance);
    public abstract long getOffset(T instance);

    public abstract T newInstance(Region region, long offset);
}