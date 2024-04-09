package br.com.don.erp.session;

import java.io.InputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import lombok.Data;

@Data
@Named
@SessionScoped
public class UserFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean autoPrintVale = false;

    InputStream inputStream;

    public UserFiles() {

    }
}
