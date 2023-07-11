package br.com.don.erp.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.don.erp.model.User;
import br.com.don.erp.repository.Usuarios;

@Named
@ApplicationScoped
public class UserBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<User> userList;

	@Inject
	private Usuarios usuarios;

	@Inject
	@Push(channel = "user")
	private PushContext push;

	@PostConstruct
	public void init() {
		userList = usuarios.buscarUsuarios();
	}
	
	public List<User> getUserList() {
		return userList;
	}

	public void update(User updatedUser){
		usuarios.merge(updatedUser);
		push.send("updateUserList");
	}

	public String getBotao() {
		return "Atualizar";
	}
	


}
