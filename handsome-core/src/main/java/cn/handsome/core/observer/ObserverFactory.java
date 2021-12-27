package cn.handsome.core.observer;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 观察者工厂
 *
 * @author shoy
 * @date 2021/12/27
 */
@Slf4j
public class ObserverFactory {
    private final CopyOnWriteArrayList<Observer> list;
    private final CopyOnWriteArrayList<Object> valueList;

    private ObserverFactory() {
        this.list = new CopyOnWriteArrayList<>();
        this.valueList = new CopyOnWriteArrayList<>();
    }

    public static ObserverFactory getInstance() {
        return new ObserverFactory();
    }

    public void addObserver(Observer observer) {
        if (null == observer) {
            return;
        }
        if (observer instanceof OnceObserver) {
            OnceObserver once = (OnceObserver) observer;
            if (valueList.contains(once.getValue())) {
                once.update();
                valueList.remove(once.getValue());
                return;
            }
        }
        if (!list.contains(observer)) {
            list.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        if (null == observer) {
            return;
        }
        list.remove(observer);
    }

    public void notify(Object value) {
        notify(value, false);
    }

    public void notify(Object value, boolean isOnce) {
        log.info("notify size:{}", this.list.size());
        for (int i = 0; i < this.list.size(); i++) {
            Observer next = list.get(i);
            if (next instanceof OnceObserver) {
                OnceObserver once = (OnceObserver) next;
                if (Objects.equals(value, once.getValue())) {
                    once.update();
                    list.remove(i);
                    i--;
                    isOnce = false;
                }
            } else {
                next.update(value, this);
            }
        }
        if (isOnce) {
            valueList.add(value);
        }
    }
}
