package repo.admin;

import java.util.List;

import beans.Admin;

//TODO update

public class AdminDaoImpl implements AdminDao {

	@Override
	public void create(Admin admin) {
		AdminRepo.admins.add(admin);
		AdminRepo.updateFile();
	}

	@Override
	public Admin read(String id) {
		for (Admin admin : AdminRepo.admins) {
			if (admin.getId().equals(id)) {
				return admin;
			}
		}
		return null;
	}

	@Override
	public void update(Admin admin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Admin> read() {
		return AdminRepo.admins;
	}

}
