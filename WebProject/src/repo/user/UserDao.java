package repo.user;

import java.util.List;

import beans.User;

public interface UserDao {
	void create(User user);
    User read(String id);
    void update(User user);
    void logicalDelete(String id);
    List<User> read();
}
