/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeAllocator implements Allocator {
    public static final Unsafe unsafe;
    public static final BlockCopy blockCopy;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            int byteArrayIndexScale = unsafe.arrayIndexScale(byte[].class);
            if (byteArrayIndexScale != 1) {
                throw new IllegalStateException("Byte array index scale must be 1, but is " + byteArrayIndexScale);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        blockCopy = new BlockCopy(unsafe);
    }

    public static final int ADDRESS_SIZE = unsafe.addressSize();
    public static final int PAGE_SIZE = unsafe.pageSize();

    public final boolean checkBounds;

    public UnsafeAllocator() {
        this(true);
    }
    
    public UnsafeAllocator(boolean checkBounds) {
        this.checkBounds = checkBounds;
    }

    public Allocation allocate(long size) throws IllegalArgumentException, OutOfMemoryError {
        if (size < 0) {
            throw new IllegalArgumentException("Size is negative: " + size);
        }

        // Allocating zero bytes, results in a pointer to null
        if (size == 0) {
            return NULL_POINTER;
        }

        long address = unsafe.allocateMemory(size);
        UnsafeAllocation memory = new UnsafeAllocation(unsafe, blockCopy, address, size, checkBounds);
        return memory;
    }
}