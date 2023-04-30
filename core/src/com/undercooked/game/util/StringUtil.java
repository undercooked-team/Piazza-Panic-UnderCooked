package com.undercooked.game.util;

/**
 * A class that provides functions for {@link String}s.
 */
public class StringUtil {

    /**
     * Converts the input into Title Case.
     *
     * @param text {@link String} : The text to convert to Title Case.
     * @return {@link String} : The text in Title Case.
     */
    public static String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder output = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (!Character.isAlphabetic(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toUpperCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            output.append(ch);
        }
        return output.toString();
    }

    /**
     * Converts the time provided into a {@link String}.
     *
     * @param hours {@code int}
     * @param minutes {@code int}
     * @param seconds {@code int}
     * @return {@link String} : The time converted into a {@link String}.
     */
    public static String formatTime(int hours, int minutes, int seconds) {
        // Seconds and minutes can only be, at most, 59
        if (seconds >= 60) {
            minutes += seconds / 60;
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        // Format time
        String output = "";
        // If hours is provided
        if (hours > 0) {
            // Add it
            output += hours + ":";
            // If minutes < 10, then add a 0
            if (minutes < 10) {
                output += "0";
            }
        }
        // Add minutes
        output += minutes + ":";
        // Add a 0 if seconds is < 10
        if (seconds < 10) {
            output += "0";
        }
        // Add seconds
        output += seconds;

        // Return the formatted time
        return output;
    }

    /**
     * Converts the time provided into a {@link String}.
     *
     * @param minutes {@code int}
     * @param seconds {@code int}
     * @return {@link String} : The time converted into a {@link String}.
     */
    public static String formatTime(int minutes, int seconds) {
        return formatTime(0, minutes, seconds);
    }


    /**
     * Converts the time provided into a {@link String}.
     *
     * @param seconds {@code int}
     * @return {@link String} : The time converted into a {@link String}.
     */
    public static String formatTime(int seconds) {
        return formatTime(0, 0, seconds);
    }


    /**
     * Converts the time provided into a {@link String}.
     *
     * @param seconds {@code float}
     * @return {@link String} : The time converted into a {@link String}.
     */
    public static String formatTime(float seconds) {
        return formatTime((int) seconds);
    }

    /**
     * Converts the time provided into a {@link String}, with 2 milliseconds.
     *
     * @param seconds {@link float} : The number of seconds.
     * @return {@link String} : The time converted into a {@link String}.
     */
    public static String formatSeconds(float seconds) {
        return formatSeconds(seconds, 2);
    }

    /**
     * Converts the time provided into a {@link String}, with milliseconds.
     *
     * @param seconds {@link float} : The number of seconds.
     * @param numOfMillis {@code int} : The number of milliseconds to display.
     * @return {@link String} : The time converted into a {@link String}.
     */
    public static String formatSeconds(float seconds, int numOfMillis) {
        String time = formatTime((int) seconds);
        // Then remove the second portion
        String millis = Integer.toString((int) Math.floor((Math.pow(10, numOfMillis)) * (seconds - Math.floor(seconds))));
        // If the length of the string is too short, then add 0s
        if (millis.length() < numOfMillis) {
            millis = "0".repeat(numOfMillis - millis.length()) + millis;
        }
        return time + "." + millis;
    }

}
