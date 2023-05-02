package com.undercooked.game.util.leaderboard;

import com.undercooked.game.util.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class for storing the information of a single {@link Leaderboard} entry.
 */
public class LeaderboardEntry {

  /**
   * The name of the entry.
   */
  public String name;

  /**
   * The score of the entry.
   */
  public float score;

  /**
   * The date and time the entry was made.
   */
  private Date date;

  /**
   * Constructor for the {@link LeaderboardEntry}.
   *
   * @param name  {@link String} : The name of the entry.
   * @param score {@code float} : The name of the entry.
   */
  public LeaderboardEntry(String name, float score) {
    this.name = name;
    this.score = score;
    setDateNow();
  }

  /**
   * Set the date that the {@link LeaderboardEntry} was made to right now.
   */
  public void setDateNow() {
    // Set time to now
    setDate(new Date());
  }

  /**
   * Set the date of the {@link LeaderboardEntry} to a specific {@link Date}.
   *
   * @param date {@link Date} : The date to use.
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Set the dat eof the {@link LeaderboardEntry} using the format defined in
   * {@link Constants#DATE_TIME}.
   * <br><br>
   * If the format is invalid, it just sets the date to null.
   *
   * @param date {@link String} : The date to set to as a {@link String}.
   */
  public void setDate(String date) {
    // Try to parse the date and set it
    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME);
    try {
      setDate(dateFormat.parse(date));
    } catch (ParseException e) {
      // If it fails, then just set date to null
      this.date = null;
    }
  }

  /**
   * Returns the record {@link Date} of the {@link LeaderboardEntry}.
   *
   * @return {@link Date} : The {@link Date} that the {@link LeaderboardEntry} was made.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Returns the record {@link Date} of the {@link LeaderboardEntry} as a {@link String}.
   *
   * @return {@link String} : The {@link #date} as a {@link String} using the format
   *                          defined in {@link Constants#DATE_TIME}.
   */
  public String dateAsString() {
    // If date is null, then return an empty string
    if (date == null) {
      return Constants.UNKNOWN_DATE;
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_TIME);

    // Return the formatted date
    return dateFormat.format(date);
  }
}
