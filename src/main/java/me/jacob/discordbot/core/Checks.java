package me.jacob.discordbot.core;


import me.jacob.discordbot.exception.BaseException;


public class Checks {

	private Checks() {
	}

	public static void notNull(final Object object, final String name) {
		if (object == null)
			throw new BaseException(name + " is null!");
	}

	public static void check(final boolean expression, final String name) {
		if (expression)
			throw new BaseException(name + " is " + true);
	}

	public static void notEmpty(final String string, final String name) {
		if (string.isEmpty())
			throw new BaseException(name + " is empty!");
	}

}
