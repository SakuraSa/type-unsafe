package com.github.sakurasa.hocon;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class IteratorUtil {

    @SafeVarargs
    public static <T> Iterator<T> concat(Iterator<T>... iterators) {
        return new QueuedIterator<>(iterators);
    }

    @SafeVarargs
    public static <T> Iterator<T> concat(Iterable<T>... iterables) {
        //noinspection rawtypes
        Iterator[] iterators = new Iterator[iterables.length];
        for (int i = 0; i < iterables.length; i++) {
            iterators[i] = iterables[i].iterator();
        }
        //noinspection unchecked,rawtypes
        return new QueuedIterator<>(iterators);
    }

    public static <T, K> Iterator<K> map(Iterator<? extends T> source, Function<? super T, ? extends K> mapper) {
        return new MapperIterator<T, K>(source, mapper);
    }

    public static <T> Iterator<T> flat(Iterator<? extends Iterator<? extends T>> iterator) {
        return new FlattenIterator<>(iterator);
    }

    public static <T, K> Iterator<K> flatMap(
            Iterator<? extends T> source, Function<? super T, ? extends Iterator<? extends K>> mapper) {
        return new FlattenIterator<>(map(source, mapper));
    }

    public static <T> Iterator<T> singleton(T value) {
        return new SingletonIterator<>(value);
    }

    static class QueuedIterator<T> implements Iterator<T> {

        private int ptr = 0;
        @SuppressWarnings("rawtypes")
        private final Iterator[] iterators;

        @SafeVarargs
        QueuedIterator(Iterator<T>... iterators) {
           this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            while (ptr < iterators.length && !iterators[ptr].hasNext()) {
                ptr ++;
            }
            return ptr < iterators.length;
        }

        @Override
        public T next() {
            if (ptr >= iterators.length) {
                throw new NoSuchElementException();
            }
            //noinspection unchecked
            return (T) iterators[ptr].next();
        }
    }

    static class FlattenIterator<T> implements Iterator<T> {

        private final Iterator<? extends Iterator<? extends T>> source;
        private Iterator<? extends T> now;
        private int buffer = 0;

        FlattenIterator(Iterator<? extends Iterator<? extends T>> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            if (buffer == 0) {
                if (!source.hasNext()) {
                    return false;
                }
                now = source.next();
                buffer ++;
            }
            while (!now.hasNext() && source.hasNext()) {
                now = source.next();
                buffer ++;
            }
            return now.hasNext();
        }

        @Override
        public T next() {
            if (buffer == 0 && !hasNext()) {
                throw new NoSuchElementException();
            }
            return now.next();
        }
    }

    static class MapperIterator<T, K> implements Iterator<K> {

        public final Iterator<? extends T> source;
        public final Function<? super T, ? extends K> mapper;

        MapperIterator(Iterator<? extends T> source, Function<? super T, ? extends K> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public K next() {
            return mapper.apply(source.next());
        }

        @Override
        public void remove() {
            source.remove();
        }
    }

    static class SingletonIterator<T> implements Iterator<T> {

        public final T value;
        public boolean hasNext = true;

        SingletonIterator(T value) {
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            hasNext = false;
            return value;
        }
    }
}
