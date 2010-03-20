/* =====================================================================
 *
 * Copyright (c) 2006 Dain Sundstrom.  All rights reserved.
 *
 * =====================================================================
 */
package org.jstruct;

import java.util.Comparator;
import java.util.Random;

public class Util {
    private static final int QUICKSORT_THRESHOLD = 7;
    private static final Random random = new Random();

    public static <T extends Struct> void shuffle(StructArray<T> list) {
        for (int i = list.size(); i > 1; i--) {
            swap(list, i - 1, random.nextInt(i));
        }
    }

    protected static <T extends Struct> void swap(StructArray<T> list, int i, int j) {
        list.swap(i, j);
    }

    protected static <T extends Struct> int compare(StructArray<T> list, int i, int j, Comparator<T> cmp) {
        return cmp.compare(list.get(i), list.get(j));
    }

    public static <T extends Comparable<T> & Struct> void quickSort(StructArray<T> list) {
        quickSort(list, 0, list.size() - 1, new NaturalOrder<T>());
    }

    public static <T extends Comparable<T> & Struct> void quickSort(int start, int end, StructArray<T> list) {
        quickSort(list, start, end, new NaturalOrder<T>());
    }

    public static <T extends Struct> void quickSort(StructArray<T> list, Comparator<T> cmp) {
        quickSort(list, 0, list.size() - 1, cmp);
    }

    public static <T extends Struct> void quickSort(StructArray<T> list, int lo, int hi, Comparator<T> cmp) {
        quickSortHelper(list, lo, hi, cmp);
        insertionSort(list, lo, hi, cmp);
    }

    private static <T extends Struct> void quickSortHelper(StructArray<T> list, int lo, int hi, Comparator<T> cmp) {
        for (; ;) {
            int diff = hi - lo;
            if (diff <= QUICKSORT_THRESHOLD) {
                break;
            }
            int i = (hi + lo) / 2;
            if (compare(list, lo, i, cmp) > 0) {
                swap(list, lo, i);
            }
            if (compare(list, lo, hi, cmp) > 0) {
                swap(list, lo, hi);
            }
            if (compare(list, i, hi, cmp) > 0) {
                swap(list, i, hi);
            }
            int j = hi - 1;
            swap(list, i, j);
            i = lo;
            int v = j;
            for (; ;) {
                while (compare(list, ++i, v, cmp) < 0) {
                    /* nothing */
                }
                while (compare(list, --j, v, cmp) > 0) {
                    /* nothing */
                }
                if (j < i) {
                    break;
                }
                swap(list, i, j);
            }
            swap(list, i, hi - 1);
            if (j - lo <= hi - i + 1) {
                quickSortHelper(list, lo, j, cmp);
                lo = i + 1;
            } else {
                quickSortHelper(list, i + 1, hi, cmp);
                hi = j;
            }
        }
    }

    private static <T extends Struct> void insertionSort(StructArray<T> list, int lo, int hi, Comparator<T> cmp) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo; j--) {
                if (compare(list, j - 1, j, cmp) > 0) {
                    swap(list, j - 1, j);
                } else {
                    break;
                }
            }
        }
    }

    private static class NaturalOrder<T extends Comparable<T>> implements Comparator<T> {
        public int compare(T o1, T o2) {
            return o1.compareTo(o2);
        }
    }
}
