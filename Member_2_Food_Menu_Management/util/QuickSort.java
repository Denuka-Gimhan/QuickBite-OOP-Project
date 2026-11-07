package com.quickbite.util;

import java.util.Comparator;
import java.util.List;

/**
 * Group-allocated ALGORITHM: Quick Sort (used to sort food items by price).
 * Hand-written on purpose (not Collections.sort) so it can be explained in the viva.
 * Average time O(n log n). Uses the middle element as pivot, so already-sorted lists are fast too.
 */
public final class QuickSort {
    private QuickSort() {}

    public static <T> void sort(List<T> list, Comparator<? super T> cmp) {
        if (list == null || list.size() < 2) return;
        quickSort(list, 0, list.size() - 1, cmp);
    }

    private static <T> void quickSort(List<T> a, int lo, int hi, Comparator<? super T> cmp) {
        while (lo < hi) {
            T pivot = a.get(lo + (hi - lo) / 2);
            int i = lo, j = hi;
            while (i <= j) {                                   // partition
                while (cmp.compare(a.get(i), pivot) < 0) i++;
                while (cmp.compare(a.get(j), pivot) > 0) j--;
                if (i <= j) { T t = a.get(i); a.set(i, a.get(j)); a.set(j, t); i++; j--; }
            }
            // recurse into the smaller half, loop on the bigger half (keeps the call stack small)
            if (j - lo < hi - i) { quickSort(a, lo, j, cmp); lo = i; }
            else                 { quickSort(a, i, hi, cmp); hi = j; }
        }
    }
}
