/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crudklinik;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author muhammadrydwan
 */
public class MainFormMethod {
    
    
    private List<Patient> patientList = new ArrayList<>();
    private boolean isEdit = false;
    private Patient selectedEditPatient;
    private int currentIndex = -1;
    private MainForm form;
    
    public MainFormMethod(MainForm form) {
        this.form = form;
        createTable();
        form.jtNama.setDocument(new JTextFieldLimit(20));
        form.jtNIK.setDocument(new NumericDocument());
        form.jtAlamat.setDocument(new JTextFieldLimit(50));
        form.jtTanggalLahir.setDocument(new JTextFieldLimit(10));
        form.jbHapus.setEnabled(false);
        form.jbUbah.setEnabled(false);
    }
    
     private void setIsEdit(boolean val) {
        this.isEdit = val;
    }
    
    private void setEditPatient(Patient val) {
        this.selectedEditPatient = val;
    }
    
    private static class NumericDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }

            // Allow only numeric input
            if (str.matches("\\d+") && getLength() + str.length() <= 15) {
                super.insertString(offset, str, attr);
            }
        }
    }
    
    private static class JTextFieldLimit extends PlainDocument {
        final private int limit;
        JTextFieldLimit(int limit) {
          super();
          this.limit = limit;
        }
        
        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
              return;

            if ((getLength() + str.length()) <= limit) {
              super.insertString(offset, str, attr);
            }
          }
    }
    
    private void populateRecordToForm(){
        form.jtNama.setText(form.tbPasien.getModel().getValueAt(currentIndex, 2).toString());
        form.jtNIK.setText(form.tbPasien.getModel().getValueAt(currentIndex, 3).toString());
        form.jtTanggalLahir.setText(parseFromHumanReadbleDateToISO(form.tbPasien.getModel().getValueAt(currentIndex, 4).toString()));
        form.jtAlamat.setText(form.tbPasien.getModel().getValueAt(currentIndex, 5).toString());

        onEdit(form);
        setEditPatient(patientList.get(currentIndex));
    }
    
    private void onEdit(MainForm form) {
        form.jbHapus.setEnabled(true);
        form.jbUbah.setEnabled(true);
        form.jbSimpan.setEnabled(false);
        setIsEdit(true);
    }
    
    public void previousRecord() {
        
        if (currentIndex > 0) {
            currentIndex--;
            populateRecordToForm();
            highlightCurrentRow();

        }
    }
    
    public void nextRecord() {
        if (currentIndex < patientList.size() - 1) {
            currentIndex++;
            populateRecordToForm();
            highlightCurrentRow();
        }
    }
    
    private void highlightCurrentRow() {
        int rowIndex = currentIndex;
        form.tbPasien.clearSelection();
        form.tbPasien.setRowSelectionInterval(rowIndex, rowIndex);
        form.tbPasien.setSelectionBackground(Color.gray); // Replace with your desired color
        form.tbPasien.setSelectionForeground(Color.BLACK);
    }
    
    
    public void createTable() {
//        patientList.add(new Patient(1, "John Doe", "123456789", "1990-05-15", "123 Main St"));
//        patientList.add(new Patient(2, "Jane Smith", "987654321", "1985-08-20", "456 Oak St"));

        
        DefaultTableModel table = new DefaultTableModel();
        table.addColumn("No");
        table.addColumn("ID");
        table.addColumn("Nama Pasien");
        table.addColumn("NIK");
        table.addColumn("Tanggal Lahir");
        table.addColumn("Alamat");
        
        for (int i = 0; i < patientList.size(); i++) {
            Patient patient = patientList.get(i);
            
            table.addRow(new Object[]{ 
                i+1,
                patient.getId(),
                patient.getNamaPasien(), 
                patient.getNik(), 
                parseFromISOToHumanReadableDate(patient.getTanggalLahir()), 
                patient.getAlamat() });
        }
        
        form.tbPasien.setModel(table);
    }
    
    public void refreshTable() {
        createTable();
    }
    
    public void exitApplication() {
        int confirmed = JOptionPane.showConfirmDialog(form,
                "Keluar dari aplikasi?", "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public void updatePatient() {
        if(isFormValid()) {
            if(showConfirmDialog("Konfirmasi ubah data", "Update pasien ini?")) {
                int row = form.tbPasien.getSelectedRow();
                if(row != -1) {
                  Patient selectedPatient = patientList.get(row);
                  selectedPatient.setNamaPasien(form.jtNama.getText().trim());
                  selectedPatient.setNik(form.jtNIK.getText().trim());
                  selectedPatient.setTanggalLahir(form.jtTanggalLahir.getText().trim());
                  selectedPatient.setAlamat(form.jtAlamat.getText().trim());
                  
                  refreshTable();
                  cancel();
                  showSuccessDialog(form, "Informasi", "Sukses ubah data pasien");
                } 
            }
        }
    }
    
    public void savePatient() {
        if(isFormValid()) {
            Patient newPatient = new Patient();
            newPatient.setId(generateId());
            newPatient.setNamaPasien(form.jtNama.getText().trim());
            newPatient.setNik(form.jtNIK.getText().trim());
            newPatient.setTanggalLahir(form.jtTanggalLahir.getText().trim());
            newPatient.setAlamat(form.jtAlamat.getText().trim());

            patientList.add(newPatient);
            refreshTable();
            clearForm();
            showSuccessDialog(form, "Information", "Sukses input pasien baru");
        }
    }
    
    public int generateId() {
        if(patientList.isEmpty()) return 1;
        return patientList.get(patientList.size() - 1).getId() + 1;
    }
    
    public void deletePatient() {
        if(showConfirmDialog("Konfirmasi hapus data", "Hapus pasien ini?")) {
                int row = form.tbPasien.getSelectedRow();
                if(row != -1) {
                  patientList.remove(row); 
                  refreshTable();
                  cancel();
                  showSuccessDialog(form, "Informasi", "Sukses hapus data pasien");
                } 
            }
    }
    
    public void clickRow() {
        try {
            int row = form.tbPasien.getSelectedRow();
            form.jtNama.setText(form.tbPasien.getModel().getValueAt(row, 2).toString());
            form.jtNIK.setText(form.tbPasien.getModel().getValueAt(row, 3).toString());
            form.jtTanggalLahir.setText(parseFromHumanReadbleDateToISO(form.tbPasien.getModel().getValueAt(row, 4).toString()));
            form.jtAlamat.setText(form.tbPasien.getModel().getValueAt(row, 5).toString());
            
            onEdit(form);
            setEditPatient(patientList.get(row));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cancel() {
        clearForm();
        selectedEditPatient = null;
        currentIndex = -1;
        form.jbSimpan.setEnabled(true);
        form.jbUbah.setEnabled(false);
        form.jbHapus.setEnabled(false);
        form.tbPasien.clearSelection();
        setIsEdit(false);
    }
    
    public void clearForm() {
        form.jtNama.setText(null);
        form.jtNIK.setText(null);
        form.jtTanggalLahir.setText(null);
        form.jtAlamat.setText(null);
    }
    
    public boolean showConfirmDialog(String title, String message) {
        int confirmed = JOptionPane.showConfirmDialog(form,
                message, title,
                JOptionPane.YES_NO_OPTION);

        return confirmed == JOptionPane.YES_OPTION;
    }
    
    public boolean isFormValid() {
        String message = "";
        
        if (form.jtNama.getText().isBlank()) {
            message = message + "\n" + "Field Nama tidak boleh kosong!";
        }

        if (form.jtNIK.getText().isBlank()) {
            message = message + "\n" + "Field NIK tidak boleh kosong!";
        } else {
            if(isNikAlreadyExist(form.jtNIK.getText())) {
                message = message + "\n" + "NIK telah terdaftar!";
            }
        }

        if (form.jtTanggalLahir.getText().isBlank()) {
            message = message + "\n" + "Field Tanggal Lahir tidak boleh kosong!";
        } else {
            if(!isDateFormatValid(form.jtTanggalLahir.getText())) {
                message = message + "\n" + "Format tanggal lahir tidak valid!,\nFormat tanggal lahir harus : yyyy-MM-dd";
            }
        }

        if (form.jtAlamat.getText().isBlank()) {
            message = message + "\n" + "Field Alamat tidak boleh kosong!";
        }
        
        if(!message.isBlank()) {
            JOptionPane.showMessageDialog(form,
                message, "Validasi Gagal",
                JOptionPane.YES_OPTION);
            return false;
        }
        return true;
    }
    
    public void showSuccessDialog(MainForm form, String title, String message) {
        JOptionPane.showMessageDialog(
                form,
                message, 
                title,
                JOptionPane.INFORMATION_MESSAGE
                );
    }
    
    public boolean isNikAlreadyExist(String nik) {
       
       if(this.isEdit) {
            return patientList.stream()
                   .filter(e -> e.getId() != this.selectedEditPatient.getId())
                   .anyMatch(patient -> patient.getNik().equals(nik.trim()));
          
       }
        return patientList.stream().anyMatch(patient -> patient.getNik().equals(nik.trim()));
    }
   
    public boolean isDateFormatValid(String value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(value.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    public String parseFromISOToHumanReadableDate(String inputDate) {
         try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("YYYY-MMM-DD");

             Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String parseFromHumanReadbleDateToISO(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MMM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
  
}
