package org.irenical.norm;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.irenical.norm.transaction.NormTransactionInterface;

public class NormStreamUtils {

	static <OBJECT> Predicate<? super OBJECT> safePredicate(Predicate<? super OBJECT> predicate, NormTransactionInterface transaction) {
		return row -> {
			try {
				return predicate.test(row);
			} catch (RuntimeException e) {
				transaction.rollbackAndFree();
				throw e;
			}
		};
	}

	static Runnable safeRunnable(Runnable closeHandler, NormTransactionInterface transaction) {
		return () -> {
			try {
				closeHandler.run();
			} catch (RuntimeException e) {
				transaction.rollbackAndFree();
				throw e;
			}
		};
	}

	static <OBJECT> Comparator<? super OBJECT> safeComparator(Comparator<? super OBJECT> comparator, NormTransactionInterface transaction) {
		return (o1, o2) -> {
			try {
				return comparator.compare(o1, o2);
			} catch (RuntimeException e) {
				transaction.rollbackAndFree();
				throw e;
			}
		};
	}

	static <OBJECT> Consumer<? super OBJECT> safeConsumer(Consumer<? super OBJECT> consumer, NormTransactionInterface transaction) {
		return (o) -> {
			try {
				consumer.accept(o);
			} catch (RuntimeException e) {
				transaction.rollbackAndFree();
				throw e;
			}
		};
	}

	static <IN, OUT> Function<? super IN, ? extends OUT> safeFunction(Function<? super IN, ? extends OUT> function, NormTransactionInterface transaction) {
		return (in) -> {
			try {
				return function.apply(in);
			} catch (RuntimeException e) {
				transaction.rollbackAndFree();
				throw e;
			}
		};
	}

}
