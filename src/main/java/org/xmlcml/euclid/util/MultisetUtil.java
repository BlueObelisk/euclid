/**
 *    Copyright 2011 Peter Murray-Rust
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.xmlcml.euclid.util;

import java.util.ArrayList;
import java.util.List;

import org.xmlcml.euclid.IntRange;
import org.xmlcml.xml.XMLUtil;

import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;

import nu.xom.Node;

/** mainly static tools.
 * 
 * @author pm286
 *
 */
public class MultisetUtil {

	/** sort entrySet by count.
	 * convenience method.
	 * @param wordSet
	 * @return
	 */
	public static Iterable<Multiset.Entry<String>> getEntriesSortedByCount(Multiset<String> wordSet) {
		return Multisets.copyHighestCountFirst(wordSet).entrySet();
	}

	public static Iterable<Entry<String>> getEntriesSortedByValue(Multiset<String> wordSet) {
		return  ImmutableSortedMultiset.copyOf(wordSet).entrySet();
	}

	
	public static Iterable<Entry<Integer>> getIntegerEntriesSortedByValue(Multiset<Integer> integerSet) {
		return  ImmutableSortedMultiset.copyOf(integerSet).entrySet();		
	}
	
	public static Iterable<Multiset.Entry<Integer>> getIntegerEntriesSortedByCount(Multiset<Integer> integerSet) {
		return Multisets.copyHighestCountFirst(integerSet).entrySet();
	}


	public static Iterable<Entry<Double>> getDoubleEntriesSortedByValue(Multiset<Double> doubleSet) {
		return  ImmutableSortedMultiset.copyOf(doubleSet).entrySet();		
	}
	
	public static Iterable<Multiset.Entry<Double>> getDoubleEntriesSortedByCount(Multiset<Double> doubleSet) {
		return Multisets.copyHighestCountFirst(doubleSet).entrySet();
	}




	/** extracts a list of attribute values.
	 * 
	 * @return
	 */
	public static List<String> getAttributeValues(Node searchNode, String xpath) {
		List<Node> nodes = XMLUtil.getQueryNodes(searchNode, xpath);
		List<String> nodeValues = new ArrayList<String>();
		for (Node node : nodes) {
			String value = node.getValue();
			if (value != null && value.trim().length() != 0) {
				nodeValues.add(value);
			}
		}
		return nodeValues;
	}


}
