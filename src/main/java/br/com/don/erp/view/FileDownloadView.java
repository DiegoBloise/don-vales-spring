package br.com.don.erp.view;

import java.io.InputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Named
@SessionScoped
public class FileDownloadView implements Serializable {

    private StreamedContent file;

    private InputStream inputStream;


    public FileDownloadView() {

        if (file != null) {
            file = DefaultStreamedContent.builder()
                    .name("vale.djprt")
                    .contentType("text/plain")
                    .stream(() -> inputStream)
                    .build();

            System.out.println("Imprimindo...");
        }
    }


    public StreamedContent getFile() {
        return file;
    }
}