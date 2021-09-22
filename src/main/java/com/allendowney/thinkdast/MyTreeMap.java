/**
 *
 */
package com.allendowney.thinkdast;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a Map using a binary search tree.
 *
 * @param <K>
 * @param <V>
 *
 */
public class MyTreeMap<K, V> implements Map<K, V> {

	private int size = 0;
	private Node root = null;

	/**
	 * Represents a node in the tree.
	 *
	 */
	protected class Node {
		public K key;
		public V value;
		public Node left = null;
		public Node right = null;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	@Override
	public boolean containsKey(Object target) {
		return findNode(target) != null;
	}

	/**
	 * Returns the entry that contains the target key, or null if there is none.
	 *
	 * @param target
	 */
	private Node findNode(Object target) {
		// 일부 구현은 null을 키로 다루기도 하지만 여기서는 아닙니다.
		if (target == null) {
			throw new IllegalArgumentException();
		}

		// 컴파일러 경고 무시
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) target;

		// 실제 탐색
		/* 반복문은 매번 target 변수를 node.key 변수와 비교합니다.
		   대상이 현재 키보다 작으면 왼쪽 자식 노드로 이동하고, 크다면 오른쪽 자식 노드로 이동합니다.
		   두 값이 같으면 현재 노드를 반환합니다.
		   대상을 찾지 못하고 트리의 바닥에 이르면 대상이 트리에 없는 것으로 판단하고 null을 반환
		*/
		Node node = root;
		while(node != null) {
			int cmp = k.compareTo(node.key);

			if (cmp < 0)
				node = node.left;
			else if (cmp > 0)
				node = node.right;
			else
				return node;
		}
		return null;
	}

	/**
	 * Compares two keys or two values, handling null correctly.
	 *
	 * @param target
	 * @param obj
	 * @return
	 */
	private boolean equals(Object target, Object obj) {
		if (target == null) {
			return obj == null;
		}
		return target.equals(obj);
	}

	@Override
	public boolean containsValue(Object target) {
		return containsValueHelper(root, target);
	}

	private boolean containsValueHelper(Node node, Object target) {
		if (node == null) {
			// 현재노드가 null인 경우는 대상을 찾지 못하고 트리의 바닥에 이른 것이므로, false를 반환
			return false;
		}
		if(equals(target, node.value)) {
			// 현재 노드의 Value가 target과 일치하는 값이라면, true를 반환
			return true;
		}
		if(containsValueHelper(node.left, target)) {
			// 왼쪽 하위 트리에서 target을 찾기 위한 재귀 호출
			return true;
		}
		if(containsValueHelper(node.right, target)) {
			// 오른쪽 하위 트리에서 target을 찾기 위한 재귀 호출
			return true;
		}
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		Node node = findNode(key);
		if (node == null) {
			return null;
		}
		return node.value;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new LinkedHashSet<K>();
		// 헬퍼 메서드를 호출하여 트리를 순회
		addInOrder(root, set);
		return set;
	}

	private void addInOrder(Node node, Set<K> set) {
		if (node == null) return;
		// 왼쪽 하위 트리를 순서대로 순회
		addInOrder(node.left, set);
		// node.key를 set에 추가
		set.add(node.key);
		// 오른쪽 하위 트리를 순서대로 순회
		addInOrder(node.right, set);
	}

	@Override
	public V put(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (root == null) {
			root = new Node(key, value);
			size++;
			return null;
		}
		return putHelper(root, key, value);
	}

	private V putHelper(Node node, K key, V value) {
		Comparable<? super K> k = (Comparable<? super K>) key;
		// 트리의 어떤 경로를 찾아가야 하는지 키를 비교
		int cmp = k.compareTo(node.key);

		if (cmp < 0) {
			// 키가 현재 노드의 키보다 작다면 왼쪽 서브 트리로 이동
			if (node.left == null) {
				// 왼쪽 서브 트리가 비었다면 새 노드로 삽입
				node.left = new Node(key, value);
				size++;
				return null;
			} else {
				return putHelper(node.left, key, value);
			}
		}
		if (cmp > 0) {
			// 키가 현재 노드의 키보다 크다면 오른쪽 서브 트리로 이동
			if (node.right == null) {
				// 오른쪽 서브 트리가 비었다면 새 노드로 삽입
				node.right = new Node(key, value);
				size++;
				return null;
			} else {
				return putHelper(node.right, key, value);
			}
		}
		//현재 노드와 키 값이 같다면, 값을 대체하고 기존 값을 반환
		V oldValue = node.value;
		node.value = value;
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		// OPTIONAL TODO: FILL THIS IN!
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<V>();
		Deque<Node> stack = new LinkedList<Node>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			if (node == null) continue;
			set.add(node.value);
			stack.push(node.left);
			stack.push(node.right);
		}
		return set;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new MyTreeMap<String, Integer>();
		map.put("Word1", 1);
		map.put("Word2", 2);
		Integer value = map.get("Word1");
		System.out.println(value);

		for (String key: map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
	}

	/**
	 * Makes a node.
	 *
	 * This is only here for testing purposes.  Should not be used otherwise.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public MyTreeMap<K, V>.Node makeNode(K key, V value) {
		return new Node(key, value);
	}

	/**
	 * Sets the instance variables.
	 *
	 * This is only here for testing purposes.  Should not be used otherwise.
	 *
	 * @param node
	 * @param size
	 */
	public void setTree(Node node, int size ) {
		this.root = node;
		this.size = size;
	}

	/**
	 * Returns the height of the tree.
	 *
	 * This is only here for testing purposes.  Should not be used otherwise.
	 *
	 * @return
	 */
	public int height() {
		return heightHelper(root);
	}

	private int heightHelper(Node node) {
		if (node == null) {
			return 0;
		}
		int left = heightHelper(node.left);
		int right = heightHelper(node.right);
		return Math.max(left, right) + 1;
	}
}
