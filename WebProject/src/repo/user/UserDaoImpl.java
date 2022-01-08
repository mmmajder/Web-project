package repo.user;

import java.util.List;
import beans.User;


//TODO update

public class UserDaoImpl implements UserDao {
	@Override
	public void create(User user) {
		UserRepo.users.add(user);
		UserRepo.updateFile();
	}
	
	@Override
	public List<User> read() {
		return UserRepo.users;
	}
	
	@Override
	public User read(String id) {
		for (User user : UserRepo.users) {
			if (user.getId().equals(id)) {
				return user;
			}
		}
		return null;
	}
	
	@Override
	public void update(User user) {
		//TODO
	}

	@Override
	public User read(String username, String password) {
		for (User user : UserRepo.users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
		
	}
}
