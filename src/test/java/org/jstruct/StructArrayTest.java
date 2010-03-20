/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

import junit.framework.Assert;
import static org.jstruct.Allocator.INT_SIZE;
import static org.jstruct.Allocator.LONG_SIZE;
import org.testng.annotations.Test;

public class StructArrayTest {
    @Test
    public void test() {
        long start;

        start = System.nanoTime();        
        Allocator allocator = new UnsafeAllocator();
        StructArray<FooStruct> array = StructArray.newStructArray(allocator, new FooStructInfo(), 1024 * 1024);
        for (int i = 0; i < array.size(); i++) {
            FooStruct foo = array.get(i);
            foo.setIntValue(i * 10);
            foo.setLongValue(Long.MAX_VALUE - foo.getIntValue());
        }
        System.out.printf("Aloc: %3.2f sec\n", (1.0 * System.nanoTime() - start) / 1000000000);

        // verify
        for (int i = 0; i < array.size(); i++) {
            FooStruct foo = array.get(i);
            Assert.assertEquals(i * 10, foo.getIntValue());
            Assert.assertEquals(Long.MAX_VALUE - foo.getIntValue(), foo.getLongValue());
        }
//       for (int loop =0; loop < 10000; loop++) {
        start = System.nanoTime();
        Util.shuffle(array);
        System.out.printf("Shuf: %3.2f sec\n", (1.0 * System.nanoTime() - start) / 1000000000);
        for (FooStruct foo : array) {
            int i = foo.getIntValue() / 10;
            Assert.assertEquals(i * 10, foo.getIntValue());
            Assert.assertEquals(Long.MAX_VALUE - foo.getIntValue(), foo.getLongValue());            
        }


        start = System.nanoTime();
        Util.quickSort(array);
        System.out.printf("Sort: %3.2f sec\n", (1.0 * System.nanoTime() - start) / 1000000000);
        for (int i = 0; i < array.size(); i++) {
            FooStruct foo = array.get(i);
            Assert.assertEquals(i * 10, foo.getIntValue());
            Assert.assertEquals(Long.MAX_VALUE - foo.getIntValue(), foo.getLongValue());
        }
//       }
    }

    public static class FooStructInfo extends StructInfo<FooStruct> {
        public FooStructInfo() {
            super(FooStruct.class, LONG_SIZE + INT_SIZE);
        }

        public Region getPointer(FooStruct instance) {
            return ((FooStructGenerated)instance).region;
        }

        public long getOffset(FooStruct instance) {
            return ((FooStructGenerated)instance).offset;
        }

        public FooStruct newInstance(Region region, long offset) {
            return new FooStructGenerated(region, offset);
        }
    }

    public interface FooStruct extends Struct, Comparable<FooStruct>
    {
        int getIntValue();

        void setIntValue(int value);

        long getLongValue();

        void setLongValue(long value);
    }

    public static class FooStructGenerated implements FooStruct
    {
        private final Region region;
        private final long offset;

        public FooStructGenerated(Region region, long offset) {
            this.region = region;
            this.offset = offset;
        }

        public int getIntValue() {
            return region.getInt(offset);
        }

        public void setIntValue(int value) {
            region.putInt(offset, value);
        }

        public long getLongValue() {
            return region.getLong(offset + INT_SIZE);
        }

        public void setLongValue(long value) {
            region.putLong(offset + INT_SIZE, value);
        }

        public int compareTo(FooStruct o) {
            int o1 = getIntValue();
            int o2 = o.getIntValue();
            if (o1 > o2) return 1;
            if (o1 < o2) return -1;
            return 0;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("FooStruct");
            sb.append("{intValue=").append(getIntValue());
            sb.append(", longValue=").append(getLongValue());
            sb.append('}');
            return sb.toString();
        }
    }
}
