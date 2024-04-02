package com.example.chatapp.common.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface DTOConverter<I, O> {
    O map(I input);
    I reversedMap(O output);

    default List<O> map(Collection<I> inputs){
        return inputs.stream().map(this::map).collect(Collectors.toList());
    }
    default List<I> reversedMap(Collection<O> outputs){
        return outputs.stream().map(this::reversedMap).collect(Collectors.toList());

    }
}
