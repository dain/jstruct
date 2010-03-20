/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

/**
 * Region is NOT thread safe.
 */
public interface Region
{
    long getAddress();
    
    long size();

    byte getByte(long offset);

    void putByte(long offset, byte value);

    byte[] getBytes(long srcOffset, int length);

    void getBytes(long srcOffset, byte[] target);

    void getBytes(long srcOffset, byte[] target, int targetOffset, int length);

    void putBytes(long targetOffset, byte[] src);

    void putBytes(long targetOffset, byte[] src, int srcOffset, int length);

    Region getRegion(long offset);
    Region getRegion(long offset, long length);

    short getShort(long offset);

    void putShort(long offset, short value);

    char getChar(long offset);

    void putChar(long offset, char value);

    int getInt(long offset);

    void putInt(long offset, int value);

    long getLong(long offset);

    void putLong(long offset, long value);

    float getFloat(long offset);

    void putFloat(long offset, float value);

    double getDouble(long offset);

    void putDouble(long offset, double value);

    void setMemory(byte value);

    void setMemory(long offset, long size, byte value);

    void copyMemory(long srcOffset, Region target);

    void copyMemory(long srcOffset, Region target, long targetOffset, long size);

    int compareMemory(long srcOffset, Region target, long targetOffset, long size);

    boolean isInBounds(long offset, long length);

    void checkBounds(long offset, long length);

}
