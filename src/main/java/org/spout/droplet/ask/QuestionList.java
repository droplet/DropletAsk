/*
 * This file is part of DropletAsk.
 *
 * Copyright (c) 2012 Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
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
 */
package org.spout.droplet.ask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spout.api.chat.ChatArguments;

public class QuestionList {
	private final DropletAsk plugin = DropletAsk.getInstance();
	private final File file = new File(plugin.getDataFolder(), "questions.txt");
	private final List<ChatArguments> questions = new ArrayList<ChatArguments>();

	public void load() {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
			String line;
			while ((line = in.readLine()) != null) {
				questions.add(ChatArguments.fromString(line));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			for (ChatArguments question : questions) {
				out.write(question.asString());
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ChatArguments> get() {
		return questions;
	}

	public void add(Object... question) {
		questions.add(new ChatArguments(question));
	}

	public void remove(int index) {
		questions.remove(index);
	}

	public boolean hasNext() {
		return !questions.isEmpty();
	}

	public ChatArguments next() {
		ChatArguments question = questions.get(0);
		questions.remove(question);
		return question;
	}
}
