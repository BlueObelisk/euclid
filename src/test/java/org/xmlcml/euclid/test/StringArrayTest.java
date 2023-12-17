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
package org.xmlcml.euclid.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author pm286
 *
 *There is no StringArray class (use List<String)> but here are some 
 *useful functions
 */
public class StringArrayTest {

	@Test
	public void testGetSets() {
		String[] strings = {"a", "b", "c",};
		List<String> stringList = Arrays.asList(strings);
		Assert.assertEquals("stringList", 3, stringList.size());	
		Set stringSet = new HashSet<String>();
		stringSet.addAll(stringList);
		Assert.assertEquals("set", 3, stringSet.size());
	}
	
	@Test
	public void testGetSets1() {
		String[] strings = {"a", "a", "c",};
		List<String> stringList = Arrays.asList(strings);
		Assert.assertEquals("stringList", 3, stringList.size());	
		Set stringSet = new HashSet<String>();
		stringSet.addAll(stringList);
		Assert.assertEquals("set", 2, stringSet.size());
	}
	
	
	@Test
	public void testGetSetsIdentical() {
		String[] strings = {"a", "a", "a",};
		List<String> stringList = Arrays.asList(strings);
		Assert.assertEquals("stringList", 3, stringList.size());	
		Set stringSet = new HashSet<String>();
		stringSet.addAll(stringList);
		Assert.assertEquals("set", 1, stringSet.size());
	}
}
