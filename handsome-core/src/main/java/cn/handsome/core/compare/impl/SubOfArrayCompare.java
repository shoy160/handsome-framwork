package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;

import java.util.List;
import java.util.Objects;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class SubOfArrayCompare extends BaseCompare {
    public SubOfArrayCompare() {
        super(ConditionOp.SUBLIST_OF_ANOTHER_ARRAY);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        List<String> validItemsStr = toStringList(compareTo);
        List<String> toCheckItemsStr = toStringList(validItemsStr);
        return isSubset(validItemsStr, toCheckItemsStr, validItemsStr.toArray().length, toCheckItemsStr.toArray().length);
    }

    private static boolean isSubset(List<String> arr1, List<String> arr2, int m, int n) {
        int i = 0;
        int j = 0;
        for (i = 0; i < n; i++) {
            for (j = 0; j < m; j++) {
                if (Objects.equals(arr2.get(i), arr1.get(j))) {
                    break;
                }
            }
            /* If the above inner loop was not broken at all then arr2[i] is not present in arr1[] */
            if (j == m) {
                return false;
            }
        }
        /* If we reach here then all
        elements of arr2[] are present
        in arr1[] */
        return true;
    }
}
