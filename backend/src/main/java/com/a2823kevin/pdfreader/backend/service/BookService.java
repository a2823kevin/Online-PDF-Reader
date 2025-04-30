package com.a2823kevin.pdfreader.backend.service;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.a2823kevin.pdfreader.backend.config.FileSavingProperties;
import com.a2823kevin.pdfreader.backend.dto.BookDTO;
import com.a2823kevin.pdfreader.backend.model.Book;
import com.a2823kevin.pdfreader.backend.model.User;
import com.a2823kevin.pdfreader.backend.model.Visibility;
import com.a2823kevin.pdfreader.backend.repository.BookRepository;
import com.a2823kevin.pdfreader.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final UserRepository userRepository;
    private final FileSavingProperties fileSavingProperties;
    private final BookRepository bookRepository;

    public BookDTO addBook(MultipartFile file, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found."));

        if (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("pdf")) {
            throw new RuntimeException("Invalid file extension.");
        }

        // save pdf file
        try {
            byte[] fileBytes = file.getBytes();
            String fileName = UUID.randomUUID().toString();
            File outputFile = Paths.get(fileSavingProperties.getPdfpath(), fileName+".pdf").toFile();
            FileUtils.writeByteArrayToFile(outputFile, fileBytes);

            Book book = new Book();
            book.setName(FilenameUtils.getBaseName(file.getOriginalFilename()));
            book.setPdfPath(String.format(
                "%s/%s", 
                fileSavingProperties.getPdfpath(), 
                fileName + ".pdf"
            ));

            generateThumbnail(outputFile, fileName);
            book.setThumbnail("/api/bookshelf/thumbnail/"+fileName);

            book.setCategory("uncategorized");
            book.setVisibility(Visibility.PRIVATE);
            book.setOwner(user);
            bookRepository.save(book);

            return new BookDTO(book);
        }
        catch (Exception e) {
            throw new RuntimeException("Can not save pdf file.");
        }
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
            .stream()
            .map((book)->new BookDTO(book))
            .toList();
    }

    public List<String> getCategories(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        return bookRepository.findCategories(user.getId());
    }

    public BookDTO getBook(UUID bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        checkVisibility(book, user);

        return new BookDTO(book);
    }

    public List<BookDTO> getUserBooks(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        return bookRepository.findByOwner(user)
            .stream()
            .map((book)->new BookDTO(book))
            .toList();
    }

    public List<BookDTO> getUserBooksWithCategory(Long userId, String category) {
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        return bookRepository.findByOwnerAndCategory(user, category)
            .stream()
            .map((book)->new BookDTO(book))
            .toList();
    }

    public byte[] getBookContent(UUID bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        checkVisibility(book, user);

        try {
            byte[] content = FileUtils.readFileToByteArray(new File(book.getPdfPath()));
            return content;
        }
        catch (IOException e) {
            throw new RuntimeException("PDF file not found");
        }
    }

    public byte[] getBookThumbnail(String thumbnailId) {
        try {
            String thumbnailPath = String.format("%s/%s.png", fileSavingProperties.getThumbnailpath(), thumbnailId);
            byte[] content = FileUtils.readFileToByteArray(new File(thumbnailPath));
            return content;
        }
        catch (IOException e) {
            throw new RuntimeException("Thumbnail file not found");
        }
    }

    public BookDTO updateBookName(UUID bookId, Long userId, String name) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        checkOwnership(book, user);

        book.setName(name);
        bookRepository.save(book);
        return new BookDTO(book);
    }

    public BookDTO updateBookCategory(UUID bookId, Long userId, String category) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        checkOwnership(book, user);

        book.setCategory(category);
        bookRepository.save(book);
        return new BookDTO(book);
    }

    public BookDTO updateBookVisibility(UUID bookId, Long userId, Visibility visibility) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(()->new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        checkOwnership(book, user);

        book.setVisibility(visibility);
        bookRepository.save(book);
        return new BookDTO(book);
    }

    public boolean deleteBook(UUID bookId, Long userId) {
        Optional<Book> book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found"));
        if (book.isPresent()) {
            checkOwnership(book.get(), user);

            try {
                Files.deleteIfExists(Paths.get(book.get().getPdfPath()));
                String[] urlSplit = book.get().getThumbnail().split("/");
                Files.deleteIfExists(Paths.get(
                    String.format(
                        "%s/%s.png", 
                        fileSavingProperties.getThumbnailpath(), urlSplit[urlSplit.length-1]
                )));
            }
            catch (IOException | NullPointerException e) {}
            bookRepository.delete(book.get());
            return true;
        }
        return false;
    }

    public void checkOwnership(Book book, User user) {
        if (!user.getRoles().stream().map(role->role.getName()).toList().contains("ADMIN")) {
            if (book.getOwner().getId()!=user.getId()) {
                throw new RuntimeException("Book not owned by user");
            }
        }
    }

    private void checkVisibility(Book book, User user) {
        if (book.getVisibility()==Visibility.PRIVATE) {
            if (book.getOwner().getId()!=user.getId()) {
                throw new RuntimeException("Book is private");
            }
        }
    }
    
    public String generateThumbnail(File pdfFile, String fileName) {
        PDDocument document;
        try {
            document = Loader.loadPDF(pdfFile);
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 100);

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            double widthRatio = (double) 192 / originalWidth;
            double heightRatio = (double) 120 / originalHeight;
            double scaleFactor = Math.min(widthRatio, heightRatio);
            
            int newWidth = (int) (originalWidth * scaleFactor);
            int newHeight = (int) (originalHeight * scaleFactor);
            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            BufferedImage thumbnail = new BufferedImage(192, 120, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = thumbnail.createGraphics();
            g2d.setColor(new Color(220, 220, 220));
            g2d.fillRect(0, 0, 192, 120);

            int x = (192-newWidth) / 2;
            int y = (120-newHeight) / 2;
            g2d.drawImage(scaledImage, x, y, null);
            g2d.dispose();

            String thumbnailPath = String.format("%s/%s.png", fileSavingProperties.getThumbnailpath(), fileName);
            ImageIO.write(
                thumbnail, 
                "PNG", 
                new File(thumbnailPath)
            );
            document.close();
            return thumbnailPath;
        }
        catch (IOException e) {
            throw new RuntimeException("PDF file not found");
        }
    }
}
