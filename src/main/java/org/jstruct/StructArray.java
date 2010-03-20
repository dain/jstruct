/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

import java.util.AbstractList;
import java.util.RandomAccess;

public class StructArray<E extends Struct> extends AbstractList<E> implements RandomAccess {
    private final Region region;
    private final long offset;
    private final StructInfo<E> typeInfo;
    private final int size;
    private final int typeSize;

    public static <E extends Struct> StructArray<E> newStructArray(Allocator allocator, StructInfo<E> typeInfo, int size) {
        return new StructArray<E>(allocator.allocate(typeInfo.size() * size), 0, typeInfo, size);
    }

    public static <E extends Struct> StructArray<E> newStructArray(Region region, long offset, StructInfo<E> typeInfo, int size) {
        return new StructArray<E>(region, offset, typeInfo, size);

    }

    private StructArray(Region region, long offset, StructInfo<E> typeInfo, int size) {
        this.region = region;
        this.offset = offset;
        this.typeInfo = typeInfo;
        this.size = size;
        typeSize = this.typeInfo.size();
    }

    @Override
    public E get(int index) {
        checkBounds(index);

        E e = typeInfo.newInstance(region, getOffset(index));
        return e;
    }

    public void swap(int a, int b) {
        checkBounds(a);
        checkBounds(b);

        byte[] temp = new byte[typeSize];
        // a -> temp
        region.getBytes(getOffset(a), temp);
        // b -> a
        region.copyMemory(getOffset(b), region, getOffset(a), typeSize);
        // tmp -> b
        region.putBytes(getOffset(b), temp);
    }

    @Override
    public E set(int index, E element) {
        checkBounds(index);

        if (element == null) {
            // clear the value
            region.setMemory(getOffset(index), typeSize, (byte) 0);
        } else {
            // set the value
            Region elementRegion = typeInfo.getPointer(element);
            long elementOffset = typeInfo.getOffset(element);
            elementRegion.copyMemory(elementOffset, region, getOffset(index), typeSize);
        }

        // this will be the new value, but there is really no way to give the old value
        return get(index);
    }

    public void fill(int from, int to, E element) {
        checkBounds(from);
        checkBounds(to);

        if (element == null) {
            // clear the values
            region.setMemory(getOffset(from), getOffset(to) - getOffset(from), (byte) 0);
        } else {
            // to the values
            Region elementRegion = typeInfo.getPointer(element);
            long elementOffset = typeInfo.getOffset(element);
            for (int index = from; index < to; index++) {
                elementRegion.copyMemory(elementOffset, region, getOffset(index), typeSize);
            }
        }
    }

    public void move(int srcPos, int destPos, int length) {
        region.copyMemory(getOffset(srcPos), region, getOffset(destPos), length * typeSize);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        if (!typeInfo.getType().isInstance(o)) {
            return -1;
        }

        E target = (E) o;
        Region targetRegion = typeInfo.getPointer(target);
        long targetOffset = typeInfo.getOffset(target);
        for (int i = 0; i < size; i++) {
            if (region.compareMemory(getOffset(i), targetRegion, targetOffset, typeSize) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (!typeInfo.getType().isInstance(o)) {
            return -1;
        }

        E target = (E) o;
        Region targetRegion = typeInfo.getPointer(target);
        long targetOffset = typeInfo.getOffset(target);
        for (int i = size -1; i >= 0; i--) {
            if (region.compareMemory(getOffset(i), targetRegion, targetOffset, typeSize) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public StructArray<E> clone()  {
        return new StructArray<E>(region, offset, typeInfo, size);
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    private long getOffset(int i) {
        return offset + i * typeSize;
    }

    private void checkBounds(int index) {
        if (index >= size || index < 0){
            throw new IndexOutOfBoundsException("size=" + size + ",index = " + index);
        }
    }
}
