/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

public interface Allocator {
    Allocation NULL_POINTER = new NullRegion();
    int BYTE_SIZE = 1;
    int CHAR_SIZE = 2;
    int SHORT_SIZE = 2;
    int INT_SIZE = 4;
    int LONG_SIZE = 8;
    int FLOAT_SIZE = 4;
    int DOUBLE_SIZE = 8;

    Allocation allocate(long size) throws OutOfMemoryError;

    public static final class NullRegion implements Allocation
    {
        private NullRegion() {
        }

        public long getAddress() {
            return 0;
        }

        public long size() {
            return 0;
        }

        public boolean isInBounds(long offset, long length)
        {
            return AllocatorUtil.isInBounds(0, offset, length);
        }

        public void checkBounds(long offset, long length) {
            AllocatorUtil.checkBounds(0, offset, length);
        }

        public void free() {
            throw new UnsupportedOperationException();
        }

        public Allocation reallocate(long size) {
            throw new UnsupportedOperationException();
        }

        public byte getByte(long offset) {
            throw new UnsupportedOperationException();
        }

        public void putByte(long offset, byte value) {
            throw new UnsupportedOperationException();
        }

        public byte[] getBytes(long srcOffset, int length) {
            throw new UnsupportedOperationException();
        }

        public void getBytes(long srcOffset, byte[] target) {
            throw new UnsupportedOperationException();
        }

        public void getBytes(long srcOffset, byte[] target, int targetOffset, int length) {
            throw new UnsupportedOperationException();
        }

        public Region getRegion(long offset)
        {
            throw new UnsupportedOperationException();
        }

        public Region getRegion(long offset, long length)
        {
            throw new UnsupportedOperationException();
        }

        public void putBytes(long targetOffset, byte[] src) {
            throw new UnsupportedOperationException();
        }

        public void putBytes(long targetOffset, byte[] src, int srcOffset, int length) {
            throw new UnsupportedOperationException();
        }

        public short getShort(long offset) {
            throw new UnsupportedOperationException();
        }

        public void putShort(long offset, short value) {
            throw new UnsupportedOperationException();
        }

        public char getChar(long offset) {
            throw new UnsupportedOperationException();
        }

        public void putChar(long offset, char value) {
            throw new UnsupportedOperationException();
        }

        public int getInt(long offset) {
            throw new UnsupportedOperationException();
        }

        public void putInt(long offset, int value) {
            throw new UnsupportedOperationException();
        }

        public long getLong(long offset) {
            throw new UnsupportedOperationException();
        }

        public void putLong(long offset, long value) {
            throw new UnsupportedOperationException();
        }

        public float getFloat(long offset) {
            throw new UnsupportedOperationException();
        }

        public void putFloat(long offset, float value) {
            throw new UnsupportedOperationException();
        }

        public double getDouble(long offset) {
            throw new UnsupportedOperationException();
        }

        public void putDouble(long offset, double value) {
            throw new UnsupportedOperationException();
        }

        public void setMemory(byte value) {
            throw new UnsupportedOperationException();
        }

        public void setMemory(long offset, long size, byte value) {
            throw new UnsupportedOperationException();
        }

        public void copyMemory(long srcOffset, Region target) {
            throw new UnsupportedOperationException();
        }

        public void copyMemory(long srcOffset, Region target, long targetOffset, long size) {
            throw new UnsupportedOperationException();
        }

        public int compareMemory(long srcOffset, Region target, long targetOffset, long size) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "NullPointer";
        }
    }
}
