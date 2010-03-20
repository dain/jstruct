/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class DirectBufferAllocator implements Allocator {
    private final AtomicInteger idGenerator = new AtomicInteger();

    public Allocation allocate(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size is negative: " + size);
        }

        if (size > Integer.MAX_VALUE) {
            throw new IllegalAccessError("Size is greater than 31 bits: " + size);
        }

        // Allocating zero bytes, results in a pointer to null
        if (size == 0) {
            return NULL_POINTER;
        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int) size);
        return new ByteBufferAllocation(this, byteBuffer, idGenerator.incrementAndGet());
    }
}
