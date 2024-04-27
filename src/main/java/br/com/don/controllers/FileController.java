package br.com.don.controllers;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.don.services.UploadService;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

    @Autowired
    UploadService uploadService;

    //private static final String UPLOAD_DIR = "/caminho/para/armazenar/arquivos";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadXML(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo vazio.");
            }

            @SuppressWarnings("null")
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            if (!fileName.toLowerCase().endsWith(".xlsx")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo inválido");
            }

            InputStream fileInputStream = file.getInputStream();

            Integer qtdImportados = uploadService.trataXML2(fileInputStream);

            if (qtdImportados != null) {
                return ResponseEntity.status(HttpStatus.OK).body("Arquivo '" + fileName + "' enviado com sucesso, " + qtdImportados + " registros importados.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao processar o arquivo.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao fazer upload do arquivo.");
        }
    }

    /* @PostMapping("/upload")
    public ResponseEntity<String> uploadArquivo(@RequestParam("file") MultipartFile file) {
        try {
            // Verifica se o arquivo é vazio
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Por favor, selecione um arquivo para fazer upload.");
            }

            // Obter o nome do arquivo
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Copia o arquivo para o diretório de upload
            Path uploadPath = Path.of(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            try (var inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return ResponseEntity.status(HttpStatus.OK).body("Arquivo '" + fileName + "' enviado com sucesso.");
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao fazer upload do arquivo.");
        }
    } */

    /* @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<byte[]> downloadArquivo(@PathVariable String fileName) {
        try {
            // Carrega o arquivo como recurso
            Path filePath = Path.of(UPLOAD_DIR).resolve(fileName).normalize();
            byte[] fileBytes = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(fileBytes);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    } */
}
