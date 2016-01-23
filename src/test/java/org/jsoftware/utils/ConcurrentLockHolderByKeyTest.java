package org.jsoftware.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

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
	public void upgradeReadToWriteLock() {
		ReadWriteLock rwLock = instance.get("A");
		rwLock.readLock().tryLock();
		rwLock.writeLock().tryLock();
	}
	
	@Test // FALSE
	public void writeLockedTryRead() throws InterruptedException {
		ReadWriteLock rwLock = instance.get("A");
		threadLock.tryLock(rwLock.writeLock());
		Assert.assertFalse(rwLock.readLock().tryLock());
	}
	

	@Test // FALSE
	public void readLockedTryWrite() throws InterruptedException {
		ReadWriteLock rwLock = instance.get("A");
		threadLock.tryLock(rwLock.readLock());
		Assert.assertFalse(rwLock.writeLock().tryLock());
	}
	
	@Test // TRUE
	public void readLockedTryRead() throws InterruptedException {
		ReadWriteLock rwLock = instance.get("A");
		threadLock.tryLock(rwLock.readLock());
		Assert.assertTrue(rwLock.readLock().tryLock());
	}
	
	@Test 
	public void twiceTest() throws InterruptedException {
		ReadWriteLock rwLock1 = instance.get("A");
		ReadWriteLock rwLock2 = instance.get("A");
		Assert.assertTrue(rwLock1 == rwLock2);
	}
	
	@Test 
	public void releaseByIdTest() throws InterruptedException {
		ReadWriteLock rwLock1 = instance.get("A");
		instance.release("A");
		Assert.assertFalse(rwLock1 == instance.get("A"));
	}
	
	@Test 
	public void releaseByLock() throws InterruptedException {
		ReadWriteLock rwLock1 = instance.get("A");
		instance.release(rwLock1);
		Assert.assertFalse(rwLock1 == instance.get("A"));
	}
	
}


class ThreadLock extends Thread {
	private final Queue<Runnable> tasks;
	private volatile boolean stop;
	
	public ThreadLock() {
		super();
		start();
        tasks = new LinkedList<Runnable>();
    }
	
	public void tryLock(final Lock lock) throws InterruptedException {
		add(new Runnable() {
			@Override
			public void run() {
				boolean b = lock.tryLock();
				Assert.assertTrue(b);
			}
		});
	}
	
	public void tryLock(final Lock lock, final long milis) throws InterruptedException {
		add(new Runnable() {
			@Override
			public void run() {
				try {
					lock.tryLock(milis, TimeUnit.MICROSECONDS);
				} catch (InterruptedException e) {
					/* ignore */
				}
			}
		});
	}
	
	public void unlock(final Lock lock) throws InterruptedException {
		add(new Runnable() {
			@Override
			public void run() {
				lock.unlock();
			}
		});
	}
	
	private void add(Runnable runnable) throws InterruptedException {
		synchronized (tasks) {
			tasks.add(runnable);
			tasks.notify();
		}
		synchronized (runnable) {
			runnable.wait();	// firebug ok 
		}
	}

	@Override
	public void run() {
		while(! stop) {
			synchronized (tasks) {
				Runnable r = tasks.poll();
				if (r != null) {
					r.run();
					synchronized (r) {
						r.notifyAll();	// firebug ok 
					}
				}
				try {
					tasks.wait(20);
				} catch (InterruptedException e) {
					/* ignore */
				}
			}
		}
	}
	
	public void stopThread() {
		this.stop = true;
	}
}