/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

/**
 * Allocation is a region that can be freed or reallcated.
 * Allocation is NOT thread safe.
 */
public interface Allocation extends Region
{
    void free();

    Allocation reallocate(long size);
    
}
