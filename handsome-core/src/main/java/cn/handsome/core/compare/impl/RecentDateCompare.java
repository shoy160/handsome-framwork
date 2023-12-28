package cn.handsome.core.compare.impl;

import cn.handsome.core.compare.BaseCompare;
import cn.handsome.core.compare.enums.ConditionOp;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author luoyong
 * @date 2023/6/25
 */
public class RecentDateCompare extends BaseCompare {
    public RecentDateCompare() {
        super(ConditionOp.RECENT_DATE);
    }

    @Override
    public boolean compare(Object value, Object compareTo) {
        Date date = Convert.toDate(value);
        Integer offset = Convert.toInt(compareTo);
        // 日期，最近 N 天
        return DateUtil.between(date, DateUtil.date(), DateUnit.DAY) <= offset;
    }
}
