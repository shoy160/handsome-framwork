package cn.handsome.core.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Key-Value
 *
 * @author shay
 * @date 2020/9/24
 */
@Getter
@Setter
@ToString
public class EntryDTO<TKey, TValue> implements Serializable {
    private static final long serialVersionUID = -2630196578792825551L;
    
    private TKey key;
    private TValue value;

    public EntryDTO() {
    }

    public EntryDTO(TKey key, TValue value) {
        this.key = key;
        this.value = value;
    }
}
