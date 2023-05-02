package com.undercooked.game.map;

import com.undercooked.game.entity.customer.Customer;

/**
 * A class which stores the information of a {@link Register}.
 *
 * @see Customer
 * @see com.undercooked.game.entity.customer.CustomerController
 */
public class Register {

  /**
   * The {@link Customer} assigned to the {@link Register}.
   * <br>
   * If it is {@code null}, then the {@link Register} is open.
   */
  protected Customer customer;

  /**
   * The {@link MapCell} that the {@link Register} is on.
   */
  protected MapCell registerCell;

  /**
   * Constructor for the {@link Register}.
   *
   * @param registerCell {@link MapCell} : The {@link MapCell} that the {@link Register}
   *                     is on.
   */
  public Register(MapCell registerCell) {
    this.registerCell = registerCell;
  }

  /**
   * Returns whether the {@link Register} does or does not have a {@link Customer}
   * linked to it.
   *
   * @return {@code boolean} : {@code true} if the {@link Register} has a {@link Customer},
   *                           {@code false} if it does not.
   */
  public boolean hasCustomer() {
    return customer != null;
  }

  /**
   * Set the {@link Customer} that is linked to the {@link Register}.
   *
   * @param customer {@link Customer} : The {@link Customer} to link.
   */
  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  /**
   * Returns the {@link Customer} linked to the {@link Register}.
   *
   * @return {@link Customer} : The {@link Customer} at the {@link Register}.
   */
  public Customer getCustomer() {
    return customer;
  }

  /**
   * Returns the {@link MapCell} that the {@link Register} is on.
   *
   * @return {@link MapCell} : The {@link MapCell} of the {@link Register}.
   */
  public MapCell getRegisterCell() {
    return registerCell;
  }
}