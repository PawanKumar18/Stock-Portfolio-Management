package user;

/**
 * UserImpl class implements User interface. It has a name for the user and getter methods to
 * retrieve the value of the name.
 */
public class UserImpl implements User {

  private final String username;

  /**
   * Constructor for UserImpl class. It initializes the user name to the given name.
   *
   * @param user username.
   */
  public UserImpl(String user) {
    this.username = user;
  }

  @Override
  public String getName() {
    return this.username;
  }

  @Override
  public String toString() {
    return this.username;
  }
}
