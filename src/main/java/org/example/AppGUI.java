package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AppGUI extends JFrame {
    private CatatanManager manager;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Warna Estetik (Palette Modern)
    private final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Soft Gray
    private final Color ACCENT_COLOR = new Color(46, 204, 113); // Emerald Green
    private final Color DANGER_COLOR = new Color(231, 76, 60);  // Alizarin Red

    private JTextField txtJudul;
    private JComboBox<String> cbMood;
    private JTextArea txtIsi;
    private DefaultTableModel tableModel;
    private JTable tabelCatatan;

    // Tabel untuk History
    private DefaultTableModel historyTableModel;

    private int editIndex = -1;

    public AppGUI() {
        manager = new CatatanManager();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(buatHalamanDashboard(), "Dashboard");
        mainPanel.add(buatHalamanForm(), "Form");
        mainPanel.add(buatHalamanHistory(), "History");

        add(mainPanel);
        setTitle("✨ My Personal Digital Diary 2025");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Custom Button Helper
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buatHalamanDashboard() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JLabel title = new JLabel("📓 Koleksi Cerita Hari Ini", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(44, 62, 80));
        panel.add(title, BorderLayout.NORTH);

        // Tabel Custom
        String[] kolom = {"No", "Tanggal", "Judul Catatan", "Suasana Hati"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit langsung
            }
        };
        tabelCatatan = new JTable(tableModel);
        tabelCatatan.setRowHeight(30);
        tabelCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelCatatan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelCatatan.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scrollPane = new JScrollPane(tabelCatatan);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel Tombol Kanan (Side Menu)
        JPanel sideMenu = new JPanel(new GridLayout(5, 1, 10, 10));
        sideMenu.setBackground(BACKGROUND_COLOR);

        JButton btnTambah = createStyledButton("➕ Tambah Baru", PRIMARY_COLOR);
        JButton btnEdit = createStyledButton("📝 Edit Catatan", ACCENT_COLOR);
        JButton btnHapus = createStyledButton("🗑️ Hapus", DANGER_COLOR);
        JButton btnHistory = createStyledButton("📊 Statistik", new Color(155, 89, 182));

        btnTambah.addActionListener(e -> { editIndex = -1; resetForm(); cardLayout.show(mainPanel, "Form"); });
        btnEdit.addActionListener(e -> {
            int row = tabelCatatan.getSelectedRow();
            if (row != -1) {
                editIndex = row;
                Catatan c = manager.getDaftarCatatan().get(row);
                txtJudul.setText(c.getJudul());
                cbMood.setSelectedItem(c.getMood());
                txtIsi.setText(c.getIsi());
                cardLayout.show(mainPanel, "Form");
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih catatan yang ingin diedit.");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = tabelCatatan.getSelectedRow();
            if (row != -1) {
                if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus kenangan ini?", "Konfirmasi", 0) == 0) {
                    manager.hapusCatatan(row);
                    refreshTable();
                }
            }
        });

        btnHistory.addActionListener(e -> cardLayout.show(mainPanel, "History"));

        sideMenu.add(btnTambah); sideMenu.add(btnEdit); sideMenu.add(btnHapus); sideMenu.add(btnHistory);
        panel.add(sideMenu, BorderLayout.EAST);

        refreshTable();
        return panel;
    }

    private JPanel buatHalamanForm() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel lblTitle = new JLabel("✍️ Tuliskan Curhatanmu", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);

        // Form Fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        txtJudul = new JTextField();
        cbMood = new JComboBox<>(new String[]{"😊 Senang", "😢 Sedih", "😐 Biasa", "😫 Lelah", "🔥 Semangat"});

        inputPanel.add(new JLabel("Judul Cerita:"));
        inputPanel.add(txtJudul);
        inputPanel.add(new JLabel("Bagaimana Mood-mu?"));
        inputPanel.add(cbMood);

        txtIsi = new JTextArea();
        txtIsi.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtIsi.setBorder(BorderFactory.createTitledBorder("Tulis Detail Kenangan..."));

        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(txtIsi), BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Buttons
        JPanel btmPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btmPanel.setBackground(Color.WHITE);
        JButton btnBatal = createStyledButton("Batal", Color.GRAY);
        JButton btnSimpan = createStyledButton("💾 Simpan Permanen", ACCENT_COLOR);

        btnBatal.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        btnSimpan.addActionListener(e -> {
            if (txtJudul.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Judul tidak boleh kosong ya!");
                return;
            }
            if (editIndex == -1) manager.tambahCatatan(new Catatan(txtJudul.getText(), txtIsi.getText(), (String)cbMood.getSelectedItem()));
            else manager.updateCatatan(editIndex, txtJudul.getText(), txtIsi.getText(), (String)cbMood.getSelectedItem());

            refreshTable();
            cardLayout.show(mainPanel, "Dashboard");
        });

        btmPanel.add(btnBatal); btmPanel.add(btnSimpan);
        panel.add(btmPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatHalamanHistory() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Judul Halaman
        JLabel lblTitle = new JLabel("📊 Laporan Riwayat Catatan", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Tabel untuk Statistik/History
        String[] kolom = {"Tanggal", "Judul Catatan", "Mood"};
        historyTableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel history tidak bisa diedit
            }
        };
        JTable historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(30);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        JButton btnBack = createStyledButton("🔙 Kembali", PRIMARY_COLOR);
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(236, 240, 241));
        footerPanel.add(btnBack);
        panel.add(footerPanel, BorderLayout.SOUTH);

        // Update isi tabel history setiap kali halaman ditampilkan
        panel.addHierarchyListener(e -> {
            if (panel.isShowing()) {
                historyTableModel.setRowCount(0); // Bersihkan tabel
                ArrayList<Catatan> list = manager.getDaftarCatatan();
                for (Catatan c : list) {
                    historyTableModel.addRow(new Object[]{
                            c.getTanggal(),
                            c.getJudul(),
                            c.getMood()
                    });
                }
            }
        });

        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Catatan> list = manager.getDaftarCatatan();
        for (int i = 0; i < list.size(); i++) {
            Catatan c = list.get(i);
            tableModel.addRow(new Object[]{i + 1, c.getTanggal(), c.getJudul(), c.getMood()});
        }
    }

    private void resetForm() {
        txtJudul.setText(""); txtIsi.setText(""); cbMood.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
    }
}