package com.gettec.fsnip.fsn.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 双缓冲队列
 * 
 * @author licw
 * 
 */
public class DoubleQueue<E> {

	private ConcurrentLinkedQueue<E> first;
	private ConcurrentLinkedQueue<E> second;
	/**
	 * 切换信号量 true 表示first为入队队列 second为出队队列
	 */
	private AtomicBoolean flag;

	public DoubleQueue() {
		first = new ConcurrentLinkedQueue<E>();
		second = new ConcurrentLinkedQueue<E>();
		flag = new AtomicBoolean(true);
	}

	/**
	 * 入队
	 * 
	 * @param e
	 */
	public void offer(E e) {
		if (flag.get()) {
			first.offer(e);
		} else {
			second.offer(e);
		}
	}

	/**
	 * 出队 当队列为空时 返回null
	 * 
	 * @return
	 */
	public E poll() {
		if (!flag.get()) {
			return first.poll();
		} else {
			return second.poll();
		}
	}

	/**
	 * 获取入队队列的元素个数
	 * 
	 * @return
	 */
	public int getOfferCount() {
		if (flag.get()) {
			return first.size();
		} else {
			return second.size();
		}

	}

	/**
	 * 获取出队队列的元素个数
	 * 
	 * @return
	 */
	public int getPollCount() {
		if (flag.get()) {
			return second.size();
		} else {
			return first.size();
		}
	}

	/**
	 * 设置信号量
	 * 
	 * @param flagval
	 */
	public void changeFlag() {
		boolean b = flag.get();
		flag.compareAndSet(b, !b);
	}
}
