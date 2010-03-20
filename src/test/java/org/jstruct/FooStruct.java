/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

public interface FooStruct extends Struct, Comparable<FooStruct>
{
    int getIntValue();

    void setIntValue(int value);

    long getLongValue();

    void setLongValue(long value);
}
