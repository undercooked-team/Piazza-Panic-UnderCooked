package com.undercooked.game.util.leaderboard;

import com.badlogic.gdx.utils.Array;
import java.util.Comparator;

/**
 * The class for storing the data of an individual leaderboard.
 */
public class Leaderboard {

  /**
   * The name of the {@link Leaderboard}.
   */
  public String name;

  /**
   * The score comparator of the {@link LeaderboardEntry}s.
   */
  private Comparator<LeaderboardEntry> entryComparator;

  /**
   * The entries on the {@link Leaderboard}.
   */
  private final Array<LeaderboardEntry> entries;

  /**
   * Constructor for the {@link Leaderboard}.
   */
  public Leaderboard() {
    entries = new Array<>();
  }

  /**
   * Sets the score comparator to compare the scores.
   *
   * @param comparator {@link Comparator}&lt;{@link LeaderboardEntry}&gt;
   *                                              : The {@link Comparator} to use.
   */
  public void setComparator(Comparator<LeaderboardEntry> comparator) {
    // Set the comparator
    this.entryComparator = comparator;
    // And sort the array
    this.entries.sort(entryComparator);
  }

  /**
   * Add a new {@link LeaderboardEntry} to the {@link Leaderboard}.
   *
   * @param newEntry {@link LeaderboardEntry} : The entry to add.
   */
  public void addLeaderboardEntry(LeaderboardEntry newEntry) {
    // And add it, in the order using the comparator
    // If the comparator doesn't exist, just add it to the end
    if (entryComparator != null) {
      // Otherwise, add it based on the comparator
      for (int i = 0; i < entries.size; i++) {
        // Loop until the comparator is 1
        if (entryComparator.compare(newEntry, entries.get(i)) == 1) {
          // If it is, then insert it here
          entries.insert(i, newEntry);
          // And return
          return;
        }
      }
    }
    // Add the entity to the end if it reaches here
    // This is if it reaches the end of the array, and is the worst,
    // or if the entity comparator doesn't exist
    entries.add(newEntry);
  }

  /**
   * Create and add a new {@link LeaderboardEntry} for the {@link Leaderboard}.
   *
   * @param name  {@link String} : The name of the {@link LeaderboardEntry}.
   * @param score {@code float} : The score of the {@link LeaderboardEntry}.
   * @return {@link LeaderboardEntry} : The newly created {@link LeaderboardEntry}.
   */
  public LeaderboardEntry addLeaderboardEntry(String name, float score) {
    // Make the entry
    LeaderboardEntry newEntry = new LeaderboardEntry(name, score);
    // And add it
    addLeaderboardEntry(newEntry);
    // And return the new entry
    return newEntry;
  }

  /**
   * Creates a copy of the {@link LeaderboardEntry} {@link Array}
   * and returns it.
   *
   * @return {@link Array}&lt;{@link LeaderboardEntry}&gt; : A copy of the entries {@link Array}.
   */
  public Array<LeaderboardEntry> copyLeaderboard() {
    // Create a new array
    Array<LeaderboardEntry> copy = new Array<>();
    // Copy over all the values
    for (LeaderboardEntry entry : entries) {
      copy.add(entry);
    }
    // Return the copy
    return copy;
  }

  /**
   * Returns the {@link Array} of {@link LeaderboardEntry} directly.
   *
   * @return {@link Array}&lt;{@link LeaderboardEntry}&gt; : The entries {@link Array}.
   */
  protected Array<LeaderboardEntry> getLeaderboard() {
    return entries;
  }

  /**
   * Removes a {@link LeaderboardEntry} from the entries.
   *
   * @param index {@code int} : The index to remove.
   * @return {@link LeaderboardEntry} : The {@link LeaderboardEntry} that was removed.
   */
  public LeaderboardEntry remove(int index) {
    // Check that the range is valid
    if (index < 0 || index >= entries.size) {
      return null;
    }

    // Then remove the index
    return entries.removeIndex(index);
  }

  /**
   * Removes all {@link LeaderboardEntry} from the entries if the
   * name matches.
   *
   * @param name {@link String} : The name of the entry to remove.
   * @return {@code int} : The number of {@link LeaderboardEntry}s that were removed.
   */
  public int removeEntry(String name) {
    // Loop through all the entries, backwards
    int entitiesRemoved = 0;
    for (int i = entries.size - 1; i >= 0; i--) {
      // Check if the name matches
      if (entries.get(i).name.equals(name)) {
        // If it does, remove the index and set to return true
        entries.removeIndex(i);
        entitiesRemoved++;
      }
    }
    return entitiesRemoved;
  }

  /**
   * Returns the number of {@link LeaderboardEntry}s in the leaderboard.
   *
   * @return {@link int} : The number of {@link LeaderboardEntry}s.
   */
  public int size() {
    return entries.size;
  }
}
