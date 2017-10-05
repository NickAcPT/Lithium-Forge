/*
 * MIT License
 *
 * Copyright (c) 2017 NickAc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package net.nickac.lithium.frontend.mod.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by NickAc for Lithium!<br><br>
 * This is a custom map that allows reverse search.<br>
 * Makes use of 2 internal HashMaps
 */
@SuppressWarnings({"SuspiciousMethodCalls", "NullableProblems"})
public class NickHashMap<K, V> implements Map<K, V> {
	private Map<K, V> map1 = new HashMap<>();
	private Map<V, K> map2 = new HashMap<>();

	public int size() {
		return map1.size();
	}

	public boolean isEmpty() {
		return map1.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map1.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map1.containsValue(value);
	}

	public V get(Object key) {
		return map1.get(key);
	}


	public K getKey(Object value) {
		return map2.get(value);
	}


	public V put(K key, V value) {
		map2.put(value, key);
		return map1.put(key, value);
	}

	public V remove(Object key) {
		map2.remove(map1.get(key));
		return map1.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach((o, o2) -> map2.put(o2, o));
		map1.putAll(m);
	}

	public void clear() {
		map1.clear();
		map2.clear();
	}

	public Set<K> keySet() {
		return map1.keySet();
	}

	public Collection<V> values() {
		return map1.values();
	}

	public Set<Map.Entry<K, V>> entrySet() {
		return map1.entrySet();
	}

	public V getOrDefault(Object key, V defaultValue) {
		return map1.getOrDefault(key, defaultValue);
	}

	public K getKeyOrDefault(Object value, K defaultValue) {
		return map2.getOrDefault(value, defaultValue);
	}

	public void forEach(BiConsumer<? super K, ? super V> action) {
		map1.forEach(action);
	}

	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		map1.replaceAll(function);
	}

	public V putIfAbsent(K key, V value) {
		map2.putIfAbsent(value, key);
		return map1.putIfAbsent(key, value);
	}

	public boolean remove(Object key, Object value) {
		map2.remove(value, key);
		return map1.remove(key, value);
	}

	public boolean replace(K key, V oldValue, V newValue) {
		return map1.replace(key, oldValue, newValue);
	}

	public V replace(K key, V value) {
		map2.replace(map1.get(key), key);
		return map1.replace(key, value);
	}

	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return map1.computeIfAbsent(key, mappingFunction);
	}

	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map1.computeIfPresent(key, remappingFunction);
	}

	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map1.compute(key, remappingFunction);
	}

	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return map1.merge(key, value, remappingFunction);
	}
}
