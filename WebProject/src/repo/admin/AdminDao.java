package repo.admin;

import java.util.List;

import beans.Admin;


public interface AdminDao {
	void create(Admin admin);
	Admin read(String id);
    void update(Admin admin);
    List<Admin> read();
}
