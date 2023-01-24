package com.github.alexkhromov.concurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

public class CompletableFutureTest {

	private static final String OUT_PATTERN_2 = "%s %s";
	private static final String OUT_PATTERN_3 = "%s %s %s";

	private final ExecutorService executor = Executors.newFixedThreadPool(10);

	public static void main(String[] args) throws ExecutionException, InterruptedException {

		CompletableFutureTest test = new CompletableFutureTest();

		out.println("Before test - " + currentThread());
		test.test_1();
		out.println("After test - " + currentThread());
	}

	private void test_1() throws ExecutionException, InterruptedException {

		CompletableFuture<String> completableFuture = new CompletableFuture<>();
		out.println(completableFuture.complete(format(OUT_PATTERN_3, "completableFuture", currentThread(), currentThread().isDaemon())));
		out.println(completableFuture.get());
		out.println(completableFuture.complete(format(OUT_PATTERN_2, "completableFuture", currentThread())));
	}

	private void test_2() throws ExecutionException, InterruptedException {

		CompletableFuture<Void> completableFuture = runAsync(() -> {
			try {
				SECONDS.sleep(5);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread()));
			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread().isDaemon()));
		});

		//completableFuture.get();
	}

	private void test_3() throws ExecutionException, InterruptedException {

		CompletableFuture<String> completableFuture = supplyAsync(() -> {

			try {
				SECONDS.sleep(5);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread()));
			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread().isDaemon()));
			return format(OUT_PATTERN_2, "completableFuture", currentThread());
		});

		//completableFuture.get();
	}

	private void test_4() throws ExecutionException, InterruptedException {

		ExecutorService executor = newFixedThreadPool(10);

		CompletableFuture<Void> completableFuture = supplyAsync(() -> {

			try {
				SECONDS.sleep(5);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread()));
			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread().isDaemon()));
			return format(OUT_PATTERN_2, "completableFuture", currentThread());
		}, executor)
				.thenRun(executor::shutdown);

		//completableFuture.get();
	}

	private void test_5() throws ExecutionException, InterruptedException {

		ExecutorService executor = newFixedThreadPool(10);

		CompletableFuture<Void> completableFuture = supplyAsync(() -> {

			try {
				SECONDS.sleep(5);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread()));
			out.println(format(OUT_PATTERN_2, "completableFuture", currentThread().isDaemon()));
			return format(OUT_PATTERN_2, "completableFuture", currentThread());
		}, executor)
				.thenApplyAsync(info -> {

					try {
						SECONDS.sleep(5);
					} catch (InterruptedException e) {
						throw new IllegalStateException(e);
					}

					out.println(format(OUT_PATTERN_3, info, "thenApplyAsync() - 1", currentThread()));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 1", currentThread().isDaemon()));
					return format(OUT_PATTERN_3, info, "thenApplyAsync() - 1", currentThread());
				}, executor)
				.thenApplyAsync(info -> {

					try {
						SECONDS.sleep(2);
					} catch (InterruptedException e) {
						throw new IllegalStateException(e);
					}

					out.println(format(OUT_PATTERN_3, info, "thenApplyAsync() - 2", currentThread()));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 2", currentThread().isDaemon()));
					return format(OUT_PATTERN_3, info, "thenApplyAsync() - 2", currentThread());
				}, executor)
				.thenRun(executor::shutdown);

		//completableFuture.get();
	}

	private void test_6() throws ExecutionException, InterruptedException {

		ExecutorService executor = newFixedThreadPool(10);

		CompletableFuture<Void> completableFuture = supplyAsync(() -> {

			try {
				SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			out.println(format(OUT_PATTERN_2, "completableFuture_1", currentThread()));
			out.println(format(OUT_PATTERN_2, "completableFuture_1", currentThread().isDaemon()));
			return format(OUT_PATTERN_2, "completableFuture_1", currentThread());
		}, executor)
				.thenCompose(previousSage -> supplyAsync(() -> {

					try {
						SECONDS.sleep(3);
					} catch (InterruptedException e) {
						throw new IllegalStateException(e);
					}

					out.println(format(OUT_PATTERN_3, previousSage, "completableFuture_2", currentThread()));
					out.println(format(OUT_PATTERN_2, "completableFuture_2", currentThread().isDaemon()));
					return format(OUT_PATTERN_3, previousSage, "completableFuture_2", currentThread());
				}, executor))
				.thenRun(executor::shutdown);

		//completableFuture.get();
	}

	private void test_7() throws ExecutionException, InterruptedException {

		ExecutorService executor = newFixedThreadPool(10);

		CompletableFuture<String> completableFuture_1 = supplyAsync(() -> {

			try {
				SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			out.println(format(OUT_PATTERN_2, "completableFuture_1", currentThread()));
			out.println(format(OUT_PATTERN_2, "completableFuture_1", currentThread().isDaemon()));
			return format(OUT_PATTERN_2, "completableFuture_1", currentThread());
		}, executor);

		CompletableFuture<String> completableFuture_2 = supplyAsync(() -> {

			try {
				SECONDS.sleep(3);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			out.println(format(OUT_PATTERN_2, "completableFuture_2", currentThread()));
			out.println(format(OUT_PATTERN_2, "completableFuture_2", currentThread().isDaemon()));
			return format(OUT_PATTERN_2, "completableFuture_2", currentThread());
		}, executor);

		CompletableFuture<Void> combinedFuture = completableFuture_1
				.thenCombine(completableFuture_2, (cf1, cf2) -> {
					out.println(format(OUT_PATTERN_3, format(OUT_PATTERN_2, cf1, cf2), "combinedFuture", currentThread()));
					out.println(format(OUT_PATTERN_2, "combinedFuture", currentThread().isDaemon()));
					return format(OUT_PATTERN_3, format(OUT_PATTERN_2, cf1, cf2), "combinedFuture", currentThread());
				})
				.thenRun(executor::shutdown);

		//combinedFuture.get();
	}

	private void test_8() throws ExecutionException, InterruptedException {

		ExecutorService executor = newFixedThreadPool(10);

		List<CompletableFuture<Integer>> completableFutureList = of(1, 2, 3, 4, 5).map(el -> supplyAsync(() -> {

			try {
				SECONDS.sleep(el);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}

			out.println(format(OUT_PATTERN_3, "completableFuture_", el, currentThread()));
			out.println(format(OUT_PATTERN_3, "completableFuture_", el, currentThread().isDaemon()));
			return el;
		}, executor)).collect(toList());

		CompletableFuture<Void> allOfFutures = allOf(completableFutureList.toArray(new CompletableFuture[0]))
				.thenApplyAsync(v -> {
					List<Integer> futureIntegers = completableFutureList.stream().map(CompletableFuture::join).collect(toList());
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 1", futureIntegers));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 1", currentThread()));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 1", currentThread().isDaemon()));
					return futureIntegers;
				}, executor)
				.thenApplyAsync(futureIntegers -> {
					List<Integer> evenIntegers = futureIntegers.stream().filter(val -> val % 2 == 0).collect(toList());
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 2", evenIntegers));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 2", currentThread()));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 2", currentThread().isDaemon()));
					return evenIntegers;
				}, executor)
				.thenApplyAsync(evenIntegers -> {
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 3", evenIntegers));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 3", currentThread()));
					out.println(format(OUT_PATTERN_2, "thenApplyAsync() - 3", currentThread().isDaemon()));
					return evenIntegers;
				}, executor)
				.thenRun(executor::shutdown);

		//allOfFutures.get();
		//out.println(format(OUT_PATTERN_3, "Last Integer is generated :", completableFutureList.get(4).get(), currentThread()));
	}

	private void test_9() throws ExecutionException, InterruptedException {

		List<Integer> itemIDs = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
		List<String> results = new ArrayList<>();

		List<Future<String>> futures = simpleBatchConfigure(itemIDs);

		for (Future<String> future : futures) {
			out.println(Thread.currentThread() + " blocked");
			results.add(future.get());
		}

		out.println(results);
	}

	private List<Future<String>> simpleBatchConfigure(List<Integer> itemIDs) {

		List<Future<String>> futures = new ArrayList<>();
		for (final int itemID : itemIDs) {
			futures.add(executor.submit(() -> {
				try {
					out.println(Thread.currentThread() + " sleep");
					SECONDS.sleep(new Random().ints(5, 20)
							.findFirst()
							.getAsInt());
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
				return format(OUT_PATTERN_2, itemID, currentThread());
			}));
		}
		executor.shutdown();

		return futures;
	}
}