package de.ktbl.eikotiger.util;

public interface BiConsumer<T,U> {
    void accept(T toConsume, U toAdd);

}
