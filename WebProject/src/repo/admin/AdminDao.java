package repo.admin;

import java.util.List;

import beans.Admin;


public interface AdminDao {
	Admin read(String id);
    List<Admin> read();
}
