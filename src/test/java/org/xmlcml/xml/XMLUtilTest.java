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
package org.xmlcml.xml;

import nu.xom.Element;

import org.junit.Assert;
import org.junit.Test;

public class XMLUtilTest {

	static String ARTICLE = ""+
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
"<!DOCTYPE article\n"+
"  PUBLIC \"-//NLM//DTD Journal Publishing DTD v3.0 20080202//EN\" \"http://dtd.nlm.nih.gov/publishing/3.0/journalpublishing3.dtd\">\n"+
"<article xmlns:mml=\"http://www.w3.org/1998/Math/MathML\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" article-type=\"research-article\" dtd-version=\"3.0\" xml:lang=\"en\">\n"+
"<front>\n"+
"<journal-meta>\n"+
"<journal-id journal-id-type=\"nlm-ta\">PLoS ONE</journal-id>\n"+
"</journal-meta>\n"+
"</front>\n"+
"</article>\n"+
"";		

	@Test
	public void testStripDTD() {
		Assert.assertEquals(467, ARTICLE.length());
		String s = XMLUtil.stripDTD(ARTICLE);
		Assert.assertEquals(322, s.length());
		Element element = XMLUtil.parseXML(s);
		Assert.assertEquals("elements ", 4, XMLUtil.getQueryElements(element, "//*").size());
	}

	@Test
	public void testStripDTDAndParse() {
		Assert.assertEquals(467, ARTICLE.length());
		Element root = XMLUtil.stripDTDAndParse(ARTICLE);
		Assert.assertEquals("elements ", 4, XMLUtil.getQueryElements(root, "//*").size());
	}
	
	@Test
	public void testAddMissingEndTags() {
		String s = "<a><meta></a>";
		s = XMLUtil.addMissingEndTags(s, "meta");
		Assert.assertEquals("<a><meta/></a>", s);
	}

	@Test
	public void testAddMissingEndTags1() {
		String s = "<a><meta></meta></a>";
		s = XMLUtil.addMissingEndTags(s, "meta");
		Assert.assertEquals("<a><meta/></a>", s);
	}

	@Test
	public void testAddMissingEndTags2() {
		String s = "<a><meta a=\"b\"></meta></a>";
		s = XMLUtil.addMissingEndTags(s, "meta");
		Assert.assertEquals("<a><meta a=\"b\"/></a>", s);
	}

}
