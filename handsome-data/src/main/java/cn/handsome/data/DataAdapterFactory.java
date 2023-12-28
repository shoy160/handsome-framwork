package cn.handsome.data;

import cn.handsome.core.Singleton;
import cn.handsome.core.exception.BusinessException;
import cn.handsome.core.utils.ReflectUtils;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author luoyong
 * @date 2023/12/28
 */
@Getter
public final class DataAdapterFactory {
    private final DbType dbType;
    private static final Map<DbType, DataAdapter> adapterMap = new HashMap<>();

    private DataAdapterFactory(DbType dbType) {
        this.dbType = dbType;
        Set<Class<?>> adapters = ReflectUtils.findClasses(DataAdapter.class::isAssignableFrom);
        for (Class<?> clazz : adapters) {
            Object instance = ReflectUtil.newInstance(clazz);
            if (instance instanceof DataAdapter) {
                DataAdapter adapter = (DataAdapter) instance;
                adapterMap.put(adapter.getDbType(), adapter);
            }
        }
    }

    public static void init(DbType dbType) {
        Singleton.instance(new DataAdapterFactory(dbType));
    }

    public static DataAdapterFactory getInstance() {
        return Singleton.instance(DataAdapterFactory.class);
    }

    public static DataAdapter adapter() {
        return getInstance().getAdapter();
    }

    public DataAdapter getAdapter() {
        DataAdapter adapter = adapterMap.get(this.dbType);
        if (Objects.isNull(adapter)) {
            throw new BusinessException("不支持的数据库类型");
        }
        return adapter;
    }
}
