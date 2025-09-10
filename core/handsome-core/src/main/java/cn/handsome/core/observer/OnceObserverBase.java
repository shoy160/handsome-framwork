package cn.handsome.core.observer;

/**
 * @author shoy
 * @date 2021/12/27
 */
public abstract class OnceObserverBase implements OnceObserver {
    private final Object value;

    public OnceObserverBase(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
