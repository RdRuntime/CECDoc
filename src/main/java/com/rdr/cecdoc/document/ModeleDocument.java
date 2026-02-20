package com.rdr.cecdoc.document;

public interface ModeleDocument<T> {
    void rediger(T donnees, RedactriceDocument redactrice);
}
