package com.project.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import com.project.exceptions.StorageException;
import com.project.exceptions.StorageFileNotFoundException;
import com.project.model.Image;
import com.project.service.abstraction.StorageService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path path = Paths.get("img/tmp/");

    @Override
    public String saveImage(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        File tmpPackage = new File(String.valueOf(path));
        if (!tmpPackage.exists()) {
            tmpPackage.mkdirs();
        }
        File fileImg = new File(String.valueOf(path.resolve(file.getOriginalFilename())));
        try (InputStream inputStream = file.getInputStream()) {
            fileImg.createNewFile();
            Files.copy(inputStream, this.path.resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.path, 1)
                    .filter(path -> !path.equals(this.path))
                    .map(this.path::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return path.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            file.toFile();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename + "");
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public String getRandomFileNameString() {
        return String.valueOf(new Random().nextLong() + ".jpg");
    }

    @Override
    public void deleteImageByFileName(String fileName) {
        try {
            Files.delete(Paths.get(path + "/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createNewPaperForImages(String namePaper) {
        try {
            Files.createDirectory(Paths.get("img/book" + namePaper));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cutImagesFromTmpPaperToNewPaperByLastIdBook(String namePaper, List<Image> imageList) {
        try {
            File folder = new File("img/tmp");
            File[] listOfFiles = folder.listFiles();
            Path destDir = Paths.get("img/book" + namePaper);
            if (listOfFiles != null)
                for (Image image : imageList) {
                    for (File file : listOfFiles) {
                        if (image.getNameImage().equals(file.getName())) {
                            Files.copy(file.toPath(), destDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearPaperTmp();
    }

    @Override
    public void cutImagesFromTmpPaperToNewPaperByLastIdBook(String namePaper) {
        try {
            File folder = new File("img/tmp");
            File[] listOfFiles = folder.listFiles();
            Path destDir = Paths.get("img/book" + namePaper);
            if (listOfFiles != null)
                for (File file : listOfFiles) {
                    Files.copy(file.toPath(), destDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearPaperTmp();
    }

    @Override
    public void clearPaperTmp() {
        for (File myFile : new File(String.valueOf(path)).listFiles()) {
            if (myFile.isFile()) {
                myFile.delete();
            }
        }
    }

    @Override
    public void deleteImageByFileNameByEditPage(String fileName) {
        try {
            Files.delete(Paths.get("img/book" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveImageByEditBook(MultipartFile file, String numberPaper) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get("img/book" + numberPaper + "/").resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public void deletePaperById(long number) {
        try {
            FileUtils.deleteDirectory(new File(String.valueOf(Paths.get("img/book" + number))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTmpFolderForImages() {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesFolderTmpExist() {
        if (Files.exists(path)) {
            return true;
        } else {
            return false;
        }
    }
}
