package br.com.don.erp.view;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.don.erp.session.UserFiles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Named
@RequestScoped
public class FileDownloadView implements Serializable {

    private StreamedContent file;

    @Inject
    private UserFiles userFiles;


    public FileDownloadView() {
        createFile();
    }


    public void createFile() {
        this.file = DefaultStreamedContent.builder()
            .name("vale.djprt")
            .contentType("text/plain")
            .stream(() -> userFiles.getInputStream())
            .build();
    }


    public StreamedContent getFile() {
        System.out.println("Imprimindo...");
        return this.file;
    }
}