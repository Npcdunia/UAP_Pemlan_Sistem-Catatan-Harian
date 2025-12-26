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

    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color ACCENT_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);

    private JTextField txtJudul;
    private JComboBox<String> cbMood;
    private JTextArea txtIsi;
    private DefaultTableModel tableModel;
    private JTable tabelCatatan;

    private DefaultTableModel searchResultModel;
    private JTable tabelHasilCari;
    private ArrayList<Integer> indeksAsliPencarian = new ArrayList<>();

    private int editIndex = -1;

    public AppGUI() {
        manager = new CatatanManager();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(buatHalamanDashboard(), "Dashboard");
        mainPanel.add(buatHalamanForm(), "Form");
        mainPanel.add(buatHalamanPencarian(), "Search");

        add(mainPanel);
        setTitle("✨ My Personal Digital Diary 2025");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

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

        JLabel title = new JLabel("📓 Koleksi Cerita Hari Ini", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        panel.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"No", "Tanggal", "Judul", "Mood"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelCatatan = new JTable(tableModel);
        panel.add(new JScrollPane(tabelCatatan), BorderLayout.CENTER);

        JPanel sideMenu = new JPanel(new GridLayout(4, 1, 10, 10));
        sideMenu.setBackground(BACKGROUND_COLOR);

        JButton btnTambah = createStyledButton("➕ Tambah Baru", PRIMARY_COLOR);
        JButton btnEdit = createStyledButton("📝 Edit Terpilih", ACCENT_COLOR);
        JButton btnHapus = createStyledButton("🗑️ Hapus", DANGER_COLOR);
        JButton btnCariPage = createStyledButton("🔍 Cari Data", new Color(155, 89, 182));

        btnTambah.addActionListener(e -> { editIndex = -1; resetForm(); cardLayout.show(mainPanel, "Form"); });
        btnEdit.addActionListener(e -> {
            int row = tabelCatatan.getSelectedRow();
            if (row != -1) bukaUntukEdit(row);
            else JOptionPane.showMessageDialog(this, "Pilih catatan di tabel!");
        });
        btnHapus.addActionListener(e -> {
            int row = tabelCatatan.getSelectedRow();
            if (row != -1 && JOptionPane.showConfirmDialog(this, "Hapus permanen?") == 0) {
                manager.hapusCatatan(row); refreshTable();
            }
        });
        btnCariPage.addActionListener(e -> cardLayout.show(mainPanel, "Search"));

        sideMenu.add(btnTambah); sideMenu.add(btnEdit); sideMenu.add(btnHapus); sideMenu.add(btnCariPage);
        panel.add(sideMenu, BorderLayout.EAST);

        refreshTable();
        return panel;
    }

    private JPanel buatHalamanForm() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 60, 30, 60));

        txtJudul = new JTextField();
        cbMood = new JComboBox<>(new String[]{"😊 Senang", "😢 Sedih", "😐 Biasa", "😫 Lelah", "🔥 Semangat"});
        txtIsi = new JTextArea();
        txtIsi.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtIsi.setBorder(BorderFactory.createTitledBorder("Isi Catatan"));

        JPanel top = new JPanel(new GridLayout(2, 2, 10, 10));
        top.setBackground(Color.WHITE);
        top.add(new JLabel("Judul:")); top.add(txtJudul);
        top.add(new JLabel("Mood:")); top.add(cbMood);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtIsi), BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        botPanel.setBackground(Color.WHITE);

        JButton btnBack = createStyledButton("🔙 Kembali", Color.GRAY);
        JButton btnSimpan = createStyledButton("💾 Simpan Catatan", ACCENT_COLOR);

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        btnSimpan.addActionListener(e -> {
            if (txtJudul.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Judul tidak boleh kosong!");
                return;
            }
            if (editIndex == -1) manager.tambahCatatan(new Catatan(txtJudul.getText(), txtIsi.getText(), (String)cbMood.getSelectedItem()));
            else manager.updateCatatan(editIndex, txtJudul.getText(), txtIsi.getText(), (String)cbMood.getSelectedItem());
            refreshTable(); cardLayout.show(mainPanel, "Dashboard");
        });

        botPanel.add(btnBack);
        botPanel.add(btnSimpan);
        panel.add(botPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatHalamanPencarian() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JTextField txtInputCari = new JTextField(20);
        JButton btnAksiCari = createStyledButton("Cari", PRIMARY_COLOR);
        JButton btnBukaData = createStyledButton("📂 Buka & Edit Data", ACCENT_COLOR);
        JButton btnBack = createStyledButton("🔙 Kembali ke Utama", Color.GRAY);

        searchResultModel = new DefaultTableModel(new String[]{"Tanggal", "Judul", "Mood"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelHasilCari = new JTable(searchResultModel);

        btnAksiCari.addActionListener(e -> {
            searchResultModel.setRowCount(0);
            indeksAsliPencarian.clear();
            ArrayList<Catatan> semua = manager.getDaftarCatatan();
            for (int i = 0; i < semua.size(); i++) {
                if (semua.get(i).getJudul().toLowerCase().contains(txtInputCari.getText().toLowerCase())) {
                    searchResultModel.addRow(new Object[]{semua.get(i).getTanggal(), semua.get(i).getJudul(), semua.get(i).getMood()});
                    indeksAsliPencarian.add(i);
                }
            }
        });

        btnBukaData.addActionListener(e -> {
            int row = tabelHasilCari.getSelectedRow();
            if (row != -1) {
                int indeksAsli = indeksAsliPencarian.get(row);
                bukaUntukEdit(indeksAsli);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data hasil cari terlebih dahulu!");
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        JPanel topPencarian = new JPanel();
        topPencarian.setBackground(BACKGROUND_COLOR);
        topPencarian.add(new JLabel("Kata Kunci: ")); topPencarian.add(txtInputCari); topPencarian.add(btnAksiCari);

        JPanel botPencarian = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botPencarian.setBackground(BACKGROUND_COLOR);
        botPencarian.add(btnBack);
        botPencarian.add(btnBukaData);

        panel.add(topPencarian, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabelHasilCari), BorderLayout.CENTER);
        panel.add(botPencarian, BorderLayout.SOUTH);
        return panel;
    }

    private void bukaUntukEdit(int index) {
        editIndex = index;
        Catatan c = manager.getDaftarCatatan().get(index);
        txtJudul.setText(c.getJudul());
        cbMood.setSelectedItem(c.getMood());
        txtIsi.setText(c.getIsi());
        cardLayout.show(mainPanel, "Form");
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