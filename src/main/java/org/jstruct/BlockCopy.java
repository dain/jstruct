/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

import java.nio.ByteBuffer;

public class BlockCopy {
    private final Unsafe unsafe;
    private final Class<? extends ByteBuffer> directByteBufferClass;
    private final long addressFieldOffset;
    private final long capacityFieldOffset;
    private final long limitFieldOffset;

    public BlockCopy(Unsafe unsafe)  {
        this.unsafe = unsafe;
        try {
            directByteBufferClass = getClass().getClassLoader().loadClass("java.nio.DirectByteBuffer").asSubclass(ByteBuffer.class);
            addressFieldOffset = getFieldOffset(unsafe, directByteBufferClass, "address");
            capacityFieldOffset = getFieldOffset(unsafe, directByteBufferClass, "capacity");
            limitFieldOffset = getFieldOffset(unsafe, directByteBufferClass, "limit");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static long getFieldOffset(Unsafe unsafe, Class<?> clazz, String name) throws NoSuchFieldException {
        while(clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(name);
                long offset = unsafe.objectFieldOffset(field);
                return offset;
            } catch (Exception ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        throw new RuntimeException("Class " + clazz.getName() + " does not contain a field named " + name);
    }

    public void getBytes(long address, byte[] target, int targetOffset, int length) {
        try {
            ByteBuffer byteBuffer = (ByteBuffer) unsafe.allocateInstance(directByteBufferClass);
            unsafe.putLong(byteBuffer, addressFieldOffset, address);
            unsafe.putInt(byteBuffer, capacityFieldOffset, Integer.MAX_VALUE);
            unsafe.putInt(byteBuffer, limitFieldOffset, Integer.MAX_VALUE);

            byteBuffer.get(target, targetOffset, length);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public void putBytes(long address, byte[] src, int srcOffset, int length) {
        try {
            ByteBuffer byteBuffer = (ByteBuffer) unsafe.allocateInstance(directByteBufferClass);
            unsafe.putLong(byteBuffer, addressFieldOffset, address);
            unsafe.putInt(byteBuffer, capacityFieldOffset, Integer.MAX_VALUE);
            unsafe.putInt(byteBuffer, limitFieldOffset, Integer.MAX_VALUE);

            byteBuffer.put(src, srcOffset, length);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}