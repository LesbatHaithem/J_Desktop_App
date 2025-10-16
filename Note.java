import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String folderId;
    
    public Note(String title, String content) {
        this.id = generateId();
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
        this.folderId = null;
    }
    
    public Note(String id, String title, String content, LocalDateTime created, LocalDateTime modified, String folderId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = created;
        this.modifiedDate = modified;
        this.folderId = folderId;
    }
    
    private String generateId() {
        return "NOTE_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        this.modifiedDate = LocalDateTime.now();
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    
    public String getFolderId() { // this is an abstract class so it can be extended
        return folderId;
    }
    
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return modifiedDate.format(formatter);
    }
    
    public String getPreview(int maxLength) {
        if (content == null || content.isEmpty()) {
            return "No content";
        }
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
    
    @Override
    public String toString() {
        return title + " (" + getFormattedDate() + ")";
    }
}