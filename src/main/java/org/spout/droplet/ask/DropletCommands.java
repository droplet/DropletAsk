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

import java.util.List;

import org.spout.droplet.ask.DropletAsk;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.plugin.Platform;

public class DropletCommands {
	private final DropletAsk plugin = DropletAsk.getInstance();

	@Command(aliases = {"question", "q", "ask"}, usage = "<question>", desc = "Submit a question to the staff.")
	@CommandPermissions("dropletask.command.question")
	public void question(CommandContext args, CommandSource source) throws CommandException {
		ChatArguments message = new ChatArguments(ChatStyle.BRIGHT_GREEN, "Question submitted by ", ChatStyle.BLUE, source.getName(), ChatStyle.BRIGHT_GREEN, ": ", ChatStyle.BLUE, args.getJoinedString(0));
		plugin.getQuestionList().add(message);
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Your question has been submitted for response.");
	}

	@Command(aliases = "nextq", desc = "Print the next question to receivers.", min = 0, max = 0)
	@CommandPermissions("dropletask.command.nextq")
	public void nextQuestion(CommandContext args, CommandSource source) throws CommandException {
		Platform p = Spout.getPlatform();
		if (p != Platform.SERVER && p != Platform.PROXY) {
			throw new CommandException("You may only read questions in server mode.");
		}
		if (!plugin.getQuestionList().hasNext()) {
			throw new CommandException("There are no questions to answer!");
		}
		((Server) Spout.getEngine()).broadcastMessage(plugin.getQuestionList().next());
	}

	@Command(aliases = "listq", desc = "List submitted questions.", min = 0, max = 1)
	@CommandPermissions("dropletask.command.listq")
	public void listQuestions(CommandContext args, CommandSource source) throws CommandException {
		List<ChatArguments> questions = plugin.getQuestionList().get();
		if (questions.size() == 0) {
			throw new CommandException("There are no questions to answer!");
		}
		int amount = questions.size();
		if (args.length() > 0) {
			int i = args.getInteger(0);
			if (i <= amount) {
				amount = i;
			}
		}
		for (int i = 0; i < amount; i++) {
			source.sendMessage(ChatStyle.BLUE, i + 1, ". ", questions.get(i));
		}
	}

	@Command(aliases = "showq", usage = "<#>", desc = "Shows specified question.", min = 1, max = 1)
	@CommandPermissions("dropletask.command.showq")
	public void showQuestion(CommandContext args, CommandSource source) throws CommandException {
		int i = args.getInteger(0) - 1;
		List<ChatArguments> questions = plugin.getQuestionList().get();
		if (i > questions.size() - 1 || i < 0) {
			throw new CommandException("There are no questions to show! List size: " + questions.size());
		}
		source.sendMessage(questions.get(i));
	}

	@Command(aliases = {"deleteq", "delq"}, usage = "<#>", desc = "Delete specified question.", min = 1, max = 1)
	@CommandPermissions("dropletask.command.deleteq")
	public void deleteQuestion(CommandContext args, CommandSource source) throws CommandException {
		int i = args.getInteger(0) - 1;
		List<ChatArguments> questions = plugin.getQuestionList().get();
		if (i > questions.size() - 1 || i < 0) {
			throw new CommandException("Index out of bounds. List size: " + questions.size());
		}
		source.sendMessage(ChatStyle.BRIGHT_GREEN, "Removed question '", ChatStyle.BLUE, i + 1, ". ", questions.get(i), ChatStyle.BRIGHT_GREEN, "'");
		plugin.getQuestionList().remove(i);
	}
}
