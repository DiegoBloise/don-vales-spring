package br.com.don.erp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.don.erp.model.User;


public class UserDao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<User> list = new ArrayList<User>();;
	
	public List<User> getUsers() {
		for (int i = 1; i < 21; i++) {
			User user = new User();
			//user.setId(i);
			user.setName("Nome: " + i);
			list.add(user);
		}
		
		return list;

	}



	public void merge(User user) {
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(user)) {
				list.get(i).setName(user.getName());
			}
		}
	}

}
