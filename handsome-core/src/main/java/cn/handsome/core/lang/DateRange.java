package cn.handsome.core.lang;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 时间范围类
 *
 * @author luoyong
 * @date 2024/8/21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRange {
    private Long begin;
    private Long end;

    public DateRange(Integer minutes) {
        this(parseLong(minutes), TimeUnit.MINUTES);
    }

    public DateRange(Long time, TimeUnit timeUnit) {
        if (Objects.isNull(time) || time == 0L) {
            return;
        }
        timeUnit = Optional.ofNullable(timeUnit).orElse(TimeUnit.MINUTES);
        this.begin = DateUtil.date().getTime() - timeUnit.toMillis(time);
    }

    public DateRange(Date begin, Date end) {
        if (Objects.nonNull(begin)) {
            this.begin = begin.getTime();
        }
        if (Objects.nonNull(end)) {
            this.end = end.getTime();
        }
    }

    private static Long parseLong(Integer value) {
        return Objects.isNull(value) ? null : value.longValue();
    }

    public void setEndDate(Date date) {
        if (Objects.nonNull(date)) {
            this.end = date.getTime();
        }
    }

    public Date getBeginDate() {
        return Objects.isNull(this.begin) ? null : new Date(this.begin);
    }

    public Date getEndDate() {
        return Objects.isNull(this.end) ? null : new Date(this.end);
    }

    public boolean isBetween(Long dateValue) {
        if (Objects.isNull(dateValue) || dateValue <= 0L) {
            return false;
        }
        return isBetween(new Date(dateValue));
    }

    public boolean isBetween(Date date) {
        if (Objects.isNull(date)) {
            return false;
        }
        if (hasBegin(this) && date.before(this.getBeginDate())) {
            return false;
        }
        if (hasEnd(this) && date.after(this.getEndDate())) {
            return false;
        }
        return true;
    }

    public static boolean hasBegin(DateRange range) {
        return Objects.nonNull(range) && Objects.nonNull(range.getBegin());
    }

    public static boolean hasEnd(DateRange range) {
        return Objects.nonNull(range) && Objects.nonNull(range.getEnd());
    }
}
