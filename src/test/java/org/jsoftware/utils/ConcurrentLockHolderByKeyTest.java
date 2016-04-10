package org.jsoftware.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConcurrentLockHolderByKeyTest {
	private ConcurrentLockHolderByKey<ReadWriteLock> instance;
	private ThreadLock threadLock;
	
	@Before
	public void init() {
		instance = ConcurrentLockHolderByKey.newReadWriteLockHolder();
		threadLock = new ThreadLock();
	}
	
	@After
	public void clean() {
		if (threadLock != null) {
			threadLock.stopThread();
		}
	}
	
	@Test // TRUE
	public void downgradingToReadLock() {
		ReadWriteLock rwLock = instance.get("A");
		assertTrue(rwLock.writeLock().tryLock());
		assertTrue(rwLock.readLock().tryLock());
	}


	@Test // FALSE
	public void writeLockedTryRead() throws InterruptedException, ExecutionException {
		ReadWriteLock rwLock = instance.get("A");
		threadLock.tryLock(rwLock.writeLock());
		assertFalse(rwLock.readLock().tryLock());
	}
	

	@Test // FALSE
	public void readLockedTryWrite() throws InterruptedException, ExecutionException {
		ReadWriteLock rwLock = instance.get("A");
		threadLock.tryLock(rwLock.readLock());
		assertFalse(rwLock.writeLock().tryLock());
	}
	
	@Test // TRUE
	public void readLockedTryRead() throws InterruptedException, ExecutionException {
		ReadWriteLock rwLock = instance.get("A");
		threadLock.tryLock(rwLock.readLock());
		assertTrue(rwLock.readLock().tryLock());
	}
	
	@Test 
	public void twiceTest() throws InterruptedException {
		ReadWriteLock rwLock1 = instance.get("A");
		ReadWriteLock rwLock2 = instance.get("A");
		assertTrue(rwLock1 == rwLock2);
	}
	
	@Test 
	public void releaseByIdTest() throws InterruptedException {
		ReadWriteLock rwLock1 = instance.get("A");
		instance.release("A");
		assertFalse(rwLock1 == instance.get("A"));
	}
	
	@Test 
	public void releaseByLock() throws InterruptedException {
		ReadWriteLock rwLock1 = instance.get("A");
		instance.release(rwLock1);
		assertFalse(rwLock1 == instance.get("A"));
	}
	
}


class ThreadLock {
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	public Boolean tryLock(final Lock lock) throws InterruptedException, ExecutionException {
		return add(() -> {
            boolean b = lock.tryLock();
			return b;
        });
	}
	
	public Boolean tryLock(final Lock lock, final long milis) throws InterruptedException, ExecutionException {
		return add(() -> {
            try {
                return lock.tryLock(milis, TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                /* ignore */
				return false;
            }
        });
	}
	
	public void unlock(final Lock lock) throws InterruptedException, ExecutionException {
		add(() -> { lock.unlock(); return true; });
	}
	
	private Boolean add(Callable<Boolean> callable) throws InterruptedException, ExecutionException {
		Future<Boolean> f = executorService.submit(callable);
		return f.get();
	}

	
	public void stopThread() {
		executorService.shutdownNow();
	}
}