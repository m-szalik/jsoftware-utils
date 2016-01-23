package org.jsoftware.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Holds lock for {@link Serializable}'s <code>id</code>.
 * <p>For new instance use static factory methods.</p> 
 * @author szalik
 * @param <L> Object to hold
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class ConcurrentLockHolderByKey<L> {
	private final Map<Serializable, LockHolder<L>> locks;
	private final Map<L, Serializable> lockToId;
	
	protected ConcurrentLockHolderByKey() {
		locks = new HashMap();
		lockToId = new HashMap();
	}
	
	protected abstract L createLockObject();
	
	/**
	 * Get lock or create new one
	 * @param id id of lock
	 * @return held object
	 */
	public final synchronized L get(Serializable id) {
		LockHolder<L> lh = locks.get(id);
		if (lh == null) {
			L l = createLockObject();
			lh = new LockHolder(l);
			locks.put(id, lh);
			lockToId.put(l, id);
		} else {
			lh.inc();
		}
		return lh.getLock();
	}
	
	
	/**
	 * Release lock
	 * @param id id of lock or lock L
	 */
	public final synchronized void release(Object id) {
		LockHolder<L> lh = locks.get(id);
		if (lh == null) {
			id = lockToId.get(id);
			lh = locks.get(id);
		}
		if (lh != null) {
			lh.dec();
			if (lh.getHolderCount() == 0) {
				locks.remove(id);
				lockToId.remove(lh.getLock());
			}
		}
	}
	
	
	/**
	 * Create new {@link ConcurrentLockHolderByKey} for {@link ReadWriteLock} implemented by {@link ReentrantReadWriteLock}
	 * @return an instance of holder
	 */
	public static ConcurrentLockHolderByKey<ReadWriteLock> newReadWriteLockHolder() {
		return new ConcurrentLockHolderByKey<ReadWriteLock>() {
			@Override
			protected ReentrantReadWriteLock createLockObject() {
				return new ReentrantReadWriteLock();
			}
		};
	}
	
	
}


class LockHolder<L> implements Serializable {
	private static final long serialVersionUID = -2515067112848085786L;
	private final L lock;
	private int holderCount;
	
	LockHolder(L lock) {
		this.lock = lock;
		this.holderCount = 1;
	}
	
	public void inc() {
		this.holderCount++;
	}
	
	public void dec() {
		this.holderCount--;
	}

	public L getLock() {
		return lock;
	}
	
	public int getHolderCount() {
		return holderCount;
	}
	
}