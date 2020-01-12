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
package org.xmlcml;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Fixtures {

	
	private static final Logger LOG = Logger.getLogger(Fixtures.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public final static File TEST_RESOURCES = new File("src/test/resources");
	public final static File TEST_DIR = new File(Fixtures.TEST_RESOURCES, "org/xmlcml");
	public final static File FILES_DIR = new File(Fixtures.TEST_DIR, "files");
	public final static File TEST_PLOSONE_0115884_DIR = new File(Fixtures.FILES_DIR, "journal.pone.0115884");
}
