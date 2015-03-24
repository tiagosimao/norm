package org.irenical.norm;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.irenical.norm.transaction.NormTransactionInterface;

public class NormStream<OBJECT> implements Stream<OBJECT> {

	private final Stream<OBJECT> s;

	private final NormTransactionInterface t;

	public NormStream(Stream<OBJECT> inner, NormTransactionInterface transaction) {
		this.s = inner;
		this.t = transaction;
	}

	@Override
	public Iterator<OBJECT> iterator() {
		try {
			return s.iterator();
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Spliterator<OBJECT> spliterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isParallel() {
		try {
			return s.isParallel();
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> sequential() {
		try {
			return new NormStream<>(s.sequential(), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> parallel() {
		try {
			return new NormStream<>(s.parallel(), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> unordered() {
		try {
			return new NormStream<>(s.unordered(), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> onClose(Runnable closeHandler) {
		try {
			return new NormStream<>(s.onClose(NormStreamUtils.safeRunnable(closeHandler, t)), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public void close() {
		try {
			s.close();
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> filter(Predicate<? super OBJECT> predicate) {
		try {
			return new NormStream<>(s.filter(NormStreamUtils.safePredicate(predicate, t)), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public <R> Stream<R> map(Function<? super OBJECT, ? extends R> mapper) {
		try {
			return new NormStream<>(s.map(NormStreamUtils.safeFunction(mapper, t)), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super OBJECT> mapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super OBJECT> mapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super OBJECT> mapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Stream<R> flatMap(Function<? super OBJECT, ? extends Stream<? extends R>> mapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntStream flatMapToInt(Function<? super OBJECT, ? extends IntStream> mapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public LongStream flatMapToLong(Function<? super OBJECT, ? extends LongStream> mapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super OBJECT, ? extends DoubleStream> mapper) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Stream<OBJECT> distinct() {
		try {
			return new NormStream<>(s.distinct(), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> sorted() {
		try {
			return new NormStream<>(s.sorted(), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> sorted(Comparator<? super OBJECT> comparator) {
		try {
			return new NormStream<>(s.sorted(NormStreamUtils.safeComparator(comparator, t)), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> peek(Consumer<? super OBJECT> action) {
		try {
			return new NormStream<>(s.peek(NormStreamUtils.safeConsumer(action, t)), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> limit(long maxSize) {
		try {
			return new NormStream<>(s.limit(maxSize), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Stream<OBJECT> skip(long n) {
		try {
			return new NormStream<>(s.skip(n), t);
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public void forEach(Consumer<? super OBJECT> action) {
		try {
			s.forEach(action);
			close();
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public void forEachOrdered(Consumer<? super OBJECT> action) {
		try {
			s.forEachOrdered(action);
			close();
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Object[] toArray() {
		try {
			return s.toArray();
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OBJECT reduce(OBJECT identity, BinaryOperator<OBJECT> accumulator) {
		try {
			OBJECT got = s.reduce(identity,accumulator);
			close();
			return got;
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Optional<OBJECT> reduce(BinaryOperator<OBJECT> accumulator) {
		try {
			Optional<OBJECT> got = s.reduce(accumulator);
			close();
			return got;
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super OBJECT, U> accumulator, BinaryOperator<U> combiner) {
		try {
			U got = s.reduce(identity,accumulator,combiner);
			close();
			return got;
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super OBJECT> accumulator, BiConsumer<R, R> combiner) {
		try {
			R got = s.collect(supplier,accumulator,combiner);
			close();
			return got;
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public <R, A> R collect(Collector<? super OBJECT, A, R> collector) {
		try {
			R got = s.collect(collector);
			close();
			return got;
		} catch (RuntimeException e) {
			t.rollbackAndFree();
			throw e;
		}
	}

	@Override
	public Optional<OBJECT> min(Comparator<? super OBJECT> comparator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<OBJECT> max(Comparator<? super OBJECT> comparator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean anyMatch(Predicate<? super OBJECT> predicate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean allMatch(Predicate<? super OBJECT> predicate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean noneMatch(Predicate<? super OBJECT> predicate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<OBJECT> findFirst() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<OBJECT> findAny() {
		throw new UnsupportedOperationException();
	}

}
