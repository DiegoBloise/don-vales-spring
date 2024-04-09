package br.com.don.erp.view.admin;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            .name("vale-" + getDateTime() + ".djprt")
            .contentType("text/plain")
            .stream(() -> userFiles.getInputStream())
            .build();
    }


    public StreamedContent getFile() {
        return this.file;
    }


    private static String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return formattedDateTime;
    }
}