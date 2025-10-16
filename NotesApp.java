import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

// ===== MAIN APP =====
public class NotesApp extends JFrame {
    // Apple-inspired color palette
    private Color bgDark = new Color(25, 25, 28);
    private Color sidebarBg = new Color(35, 35, 38);
    private Color cardBg = new Color(45, 45, 48);
    private Color cardHover = new Color(55, 55, 58);
    private Color accent = new Color(255, 159, 10);
    private Color accentHover = new Color(255, 179, 64);
    private Color textPrimary = new Color(255, 255, 255);
    private Color textSecondary = new Color(152, 152, 157);
    private Color selectedBg = new Color(65, 65, 68);
    
    private JPanel mainContent;
    private JPanel notesContainer;
    private JLabel mainTitleLabel;
    private JTextField searchField;
    private DataManager dataManager;
    private String currentView = "notes"; // notes, tasks, folders
    
    private JButton allNotesBtn;
    private JButton tasksBtn;
    private JButton foldersBtn;
    
    public NotesApp() {
        super("Notes");
        dataManager = DataManager.getInstance();
        setupUI();
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(bgDark);
        
        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Main content
        mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);
        
        // Load initial notes
        refreshNotesView();
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(sidebarBg);
        
        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        header.setBackground(sidebarBg);
        JLabel titleLabel = new JLabel("Notes");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 26));
        titleLabel.setForeground(textPrimary);
        header.add(titleLabel);
        
        // Navigation
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(sidebarBg);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        allNotesBtn = createNavButton("📝 All Notes", true);
        tasksBtn = createNavButton("✓ Tasks", false);
        foldersBtn = createNavButton("📁 Folders", false);
        
        allNotesBtn.addActionListener(e -> switchView("notes", allNotesBtn));
        tasksBtn.addActionListener(e -> switchView("tasks", tasksBtn));
        foldersBtn.addActionListener(e -> switchView("folders", foldersBtn));
        
        navPanel.add(allNotesBtn);
        navPanel.add(Box.createVerticalStrut(8));
        navPanel.add(tasksBtn);
        navPanel.add(Box.createVerticalStrut(8));
        navPanel.add(foldersBtn);
        
        JPanel navContainer = new JPanel(new BorderLayout());
        navContainer.setBackground(sidebarBg);
        navContainer.add(navPanel, BorderLayout.NORTH);
        
        // Bottom button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottomPanel.setBackground(sidebarBg);
        JButton newBtn = createNewNoteButton();
        newBtn.addActionListener(e -> createNewItem());
        bottomPanel.add(newBtn);
        
        sidebar.add(header, BorderLayout.NORTH);
        sidebar.add(navContainer, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);
        
        return sidebar;
    }
    
    private void switchView(String view, JButton selectedBtn) {
        currentView = view;
        
        // Update button states
        allNotesBtn.setBackground(sidebarBg);
        allNotesBtn.setForeground(textSecondary);
        tasksBtn.setBackground(sidebarBg);
        tasksBtn.setForeground(textSecondary);
        foldersBtn.setBackground(sidebarBg);
        foldersBtn.setForeground(textSecondary);
        
        selectedBtn.setBackground(selectedBg);
        selectedBtn.setForeground(textPrimary);
        
        // Update main view
        switch (view) {
            case "notes":
                mainTitleLabel.setText("All Notes");
                refreshNotesView();
                break;
            case "tasks":
                mainTitleLabel.setText("Tasks");
                refreshTasksView();
                break;
            case "folders":
                mainTitleLabel.setText("Folders");
                refreshFoldersView();
                break;
        }
    }
    
    private void createNewItem() {
        switch (currentView) {
            case "notes":
                createNewNote();
                break;
            case "tasks":
                createNewTask();
                break;
            case "folders":
                createNewFolder();
                break;
        }
    }
    
    private void createNewNote() {
        NoteDialog dialog = new NoteDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshNotesView();
        }
    }
    
    private void createNewTask() {
        JOptionPane.showMessageDialog(this, "Tasks view not yet implemented!");
    }
    
    private void createNewFolder() {
        JOptionPane.showMessageDialog(this, "Folders view not yet implemented!");
    }
    
    private JButton createNavButton(String text, boolean selected) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
        btn.setForeground(selected ? textPrimary : textSecondary);
        btn.setBackground(selected ? selectedBg : sidebarBg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setPreferredSize(new Dimension(230, 44));
        btn.setMaximumSize(new Dimension(230, 44));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new RoundedBorder(12));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground() == sidebarBg) btn.setBackground(new Color(48, 48, 51));
            }
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground() != selectedBg) btn.setBackground(sidebarBg);
            }
        });
        return btn;
    }
    
    private JButton createNewNoteButton() {
        JButton btn = new JButton("+ New Note");
        btn.setFont(new Font("SF Pro Text", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(accent);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(220, 44));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new RoundedBorder(12));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(accentHover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(accent); }
        });
        return btn;
    }
    
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(bgDark);
        
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(bgDark);
        topBar.setBorder(BorderFactory.createEmptyBorder(30, 35, 20, 35));
        
        mainTitleLabel = new JLabel("All Notes");
        mainTitleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        mainTitleLabel.setForeground(textPrimary);
        
        searchField = createSearchField();
        
        topBar.add(mainTitleLabel, BorderLayout.WEST);
        topBar.add(searchField, BorderLayout.EAST);
        
        // Notes container
        notesContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        notesContainer.setBackground(bgDark);
        notesContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(notesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(bgDark);
        
        main.add(topBar, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);
        return main;
    }
    
   private JTextField createSearchField() {
    JTextField field = new JTextField("🔍  Search notes...", 20);
    field.setPreferredSize(new Dimension(260, 36));
    field.setBackground(cardBg);
    field.setForeground(Color.GRAY); // placeholder color
    field.setCaretColor(textPrimary);
    field.setBorder(new RoundedBorder(12));
    field.setFont(new Font("SF Pro Text", Font.PLAIN, 14));

    // Add focus behavior
    field.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (field.getText().equals("🔍  Search notes...")) {
                field.setText("");
                field.setForeground(textPrimary);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (field.getText().isEmpty()) {
                field.setForeground(Color.GRAY);
                field.setText("🔍  Search notes...");
            }
        }
    });

    field.addActionListener(e -> searchNotes(field.getText().trim()));
    return field;
}

    
    private void searchNotes(String query) {
        List<Note> results = dataManager.searchNotes(query);
        displayNotes(results);
    }
    
    private void refreshNotesView() {
        List<Note> notes = dataManager.getAllNotes();
        displayNotes(notes);
    }
    
    private void refreshTasksView() {
        notesContainer.removeAll();
        notesContainer.add(new JLabel("Tasks view coming soon!", SwingConstants.CENTER));
        notesContainer.revalidate();
        notesContainer.repaint();
    }
    
    private void refreshFoldersView() {
        notesContainer.removeAll();
        notesContainer.add(new JLabel("Folders view coming soon!", SwingConstants.CENTER));
        notesContainer.revalidate();
        notesContainer.repaint();
    }
    
    private void displayNotes(List<Note> notes) {
        notesContainer.removeAll();
        for (Note note : notes) {
            JPanel card = createNoteCard(note);
            notesContainer.add(card);
        }
        notesContainer.revalidate();
        notesContainer.repaint();
    }
    
    private JPanel createNoteCard(Note note) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(260, 160));
        card.setLayout(new BorderLayout());
        card.setBackground(cardBg);
        card.setBorder(new RoundedBorder(16));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel title = new JLabel(note.getTitle());
        title.setForeground(textPrimary);
        title.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        
        JLabel date = new JLabel(note.getFormattedDate());
        date.setForeground(textSecondary);
        date.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        
        JTextArea preview = new JTextArea(note.getPreview(80));
        preview.setWrapStyleWord(true);
        preview.setLineWrap(true);
        preview.setEditable(false);
        preview.setOpaque(false);
        preview.setForeground(textSecondary);
        preview.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(cardBg);
        top.add(title, BorderLayout.NORTH);
        top.add(date, BorderLayout.SOUTH);
        
        card.add(top, BorderLayout.NORTH);
        card.add(preview, BorderLayout.CENTER);
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(cardHover); }
            public void mouseExited(MouseEvent e) { card.setBackground(cardBg); }
            public void mouseClicked(MouseEvent e) {
                NoteDialog dialog = new NoteDialog(NotesApp.this, note);
                dialog.setVisible(true);
                if (dialog.isSaved()) refreshNotesView();
            }
        });
        return card;
    }
     // checkbox to get it's property we do case1.isSelected()
    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotesApp::new);
    }
}

// ===== CUSTOM BORDER CLASS =====
class RoundedBorder implements Border {
    private int radius;
    RoundedBorder(int radius) { this.radius = radius; }
    public Insets getBorderInsets(Component c) { return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius); }
    public boolean isBorderOpaque() { return false; }
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}

// ===== DUMMY DATAMANAGER =====
class DataManager {
    private static DataManager instance;
    private List<Note> notes = new ArrayList<>();
    private DataManager() {
        notes.add(new Note("Welcome", "This is your first note!"));
        notes.add(new Note("Java AWT", "AWT stands for Abstract Window Toolkit."));
    }
    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }
    public List<Note> getAllNotes() { return notes; }
    public void addNote(Note note) { notes.add(note); }
    public List<Note> searchNotes(String query) {
        if (query.isEmpty()) return getAllNotes();
        List<Note> result = new ArrayList<>();
        for (Note n : notes) {
            if (n.getTitle().toLowerCase().contains(query.toLowerCase())) result.add(n);
        }
        return result;
    }
}

// ===== SIMPLE NOTE DIALOG =====
class NoteDialog extends JDialog {
    private JTextField titleField;
    private JTextArea contentArea;
    private boolean saved = false;
    private Note note;
    private DataManager dataManager = DataManager.getInstance();
    
    public NoteDialog(JFrame parent, Note existingNote) {
        super(parent, "Note Editor", true);
        this.note = existingNote;
        setupUI();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
   private void setupUI() {
    setLayout(new BorderLayout(10, 10));

    JPanel form = new JPanel();
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title label and field
    JLabel titleLabel = new JLabel("Title:");
    titleField = new JTextField();
    titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

    // Content area
    JLabel contentLabel = new JLabel("Content:");
    contentArea = new JTextArea(10, 30);
    contentArea.setLineWrap(true);
    contentArea.setWrapStyleWord(true);
    JScrollPane contentScroll = new JScrollPane(contentArea);

    // Fill existing data if editing
    if (note != null) {
        titleField.setText(note.getTitle());
        contentArea.setText(note.getContent());
    }

    // Add components in order
    form.add(titleLabel);
    form.add(Box.createVerticalStrut(5));
    form.add(titleField);
    form.add(Box.createVerticalStrut(10));
    form.add(contentLabel);
    form.add(Box.createVerticalStrut(5));
    form.add(contentScroll);

    // Buttons
    JButton saveBtn = new JButton("Save");
    saveBtn.addActionListener(e -> saveNote());

    JButton cancelBtn = new JButton("Cancel");
    cancelBtn.addActionListener(e -> dispose());

    JPanel actions = new JPanel();
    actions.add(saveBtn);
    actions.add(cancelBtn);

    // Add everything to the dialog
    add(form, BorderLayout.CENTER);
    add(actions, BorderLayout.SOUTH);
}

    
    private void saveNote() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty!");
            return;
        }
        if (note == null) {
            dataManager.addNote(new Note(title, content));
        } else {
            note.setTitle(title);
            note.setContent(content);
        }
        saved = true;
        dispose();
    }
    
    public boolean isSaved() { return saved; }
}
