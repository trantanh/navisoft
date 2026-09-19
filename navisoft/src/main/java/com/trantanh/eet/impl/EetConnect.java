package com.trantanh.eet.impl;

import com.trantanh.navipos.dao.impl.BillDaoImpl;
import com.trantanh.navipos.dao.impl.EetDaoImpl;
import com.trantanh.navipos.model.EetConfigModel;
import com.trantanh.navipos.service.BillService;
import com.trantanh.navipos.service.impl.BillServiceImpl;
import com.trantanh.navipos.utils.DateUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class EetConnect {

    public TextField dicTextField;
    private TextField storeTextField;
    private PasswordField passwordField;
    private FileChooser fileChooser;
    private Button openButton;
    private Button saveButton;
    private Button deleteButton;
    private Alert alert;
    private Desktop desktop;
    private Label validityLabel;
    private TextField cashTextField;
    private TextField porad_cisTextField;
    private EetDaoImpl eetData;
    private BillService billData;

    public EetConnect(TextField dicTextField, TextField storeTextField, PasswordField passwordField, Button openButton, Button saveButton, Button deleteButton, Label validityLabel, TextField cashTextField, TextField porad_cisTextField) throws SQLException {
        this.dicTextField = dicTextField;
        this.storeTextField = storeTextField;
        this.passwordField = passwordField;
        this.openButton = openButton;
        this.saveButton = saveButton;
        this.deleteButton = deleteButton;
        this.validityLabel = validityLabel;
        this.cashTextField = cashTextField;
        this.porad_cisTextField = porad_cisTextField;
        billData = BillServiceImpl.getInstance();
        openCertificate(openButton);
        saveButton(saveButton);
        resetButton();
        getEet();
    }

    private void resetButton() {
        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Informace");
            alert.setHeaderText("EET");
            alert.setContentText("Přejete si smazat EET?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

            } else {
                alert.close();
            }

        });
    }

    private void saveButton(Button saveButton) {
        saveButton.setOnAction(e -> {
            validForm();
            eetData = new EetDaoImpl();
            EetConfigModel eetConfigModel = new EetConfigModel();
            eetConfigModel.setId(1);
            eetConfigModel.setDic(dicTextField.getText());
            eetConfigModel.setPassword(passwordField.getText());
            eetConfigModel.setPath(openButton.getText());
            eetConfigModel.setProvoz(storeTextField.getText());
            eetConfigModel.setPokl(cashTextField.getText());
            eetConfigModel.setValidity(validityLabel.getText());
            eetConfigModel.setSpace("sd");
            eetData.saveOrUpdate(eetConfigModel);
            try {
                InputStream is = new FileInputStream(openButton.getText());
                KeyStore store = KeyStore.getInstance("pkcs12");
                String password = passwordField.getText();
                store.load(is, password.toCharArray());
                Enumeration en = store.aliases();
                while (en.hasMoreElements()) {
                    String alias = (String) en.nextElement();
                    X509Certificate c = (X509Certificate) store.getCertificate(alias);
                    String validateDate = DateUtils.formatDate(c.getNotAfter());
                    validityLabel.setText(validateDate);
                    Principal subject = c.getSubjectDN();
                    String subjectArray[] = subject.toString().split(",");
                    for (String s : subjectArray) {
                        String[] str = s.trim().split("=");
                        String key = str[0];
                        String value = str[1];
                        System.out.println(key + " - " + value);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(EetConnect.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CertificateException e1) {
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            } catch (KeyStoreException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

    }

    private void validForm() {
        if (dicTextField.getText() == null || dicTextField.getText().length() == 0) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Informační dialog");
            alert.setHeaderText("Pozor, máte špatný format DIČ");
            alert.setContentText("Příklad CZ123456789");
            alert.showAndWait();
        }
        if (storeTextField.getText() == null || storeTextField.getText().length() == 0) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Informační dialog");
            alert.setHeaderText("Pozor, máte špatný format provozovna");
            alert.setContentText("Příklad číslo 1-99");
            alert.showAndWait();
        }
    }

    private void openCertificate(Button openButton) {
        openButton.setOnAction(
                e -> {
                    fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(dicTextField.getScene().getWindow());
                    if (file != null) {
                        openButton.setText(file.getAbsolutePath());
                    }
                });

    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(EetConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TextField getDicTextField() {
        return dicTextField;
    }

    public void setDicTextField(String dicTextField) {
        this.dicTextField.setText(dicTextField);
    }

    public TextField getStoreTextField() {
        return storeTextField;
    }

    public void setStoreTextField(String storeTextField) {
        this.storeTextField.setText(storeTextField);
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(String passwordField) {
        this.passwordField.setText(passwordField);
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    public Button getFileButton() {
        return openButton;
    }

    public void setFileButton(String path) {
        this.openButton.setText(path);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public void setValidityLabel(String valiLabel) {
        this.validityLabel.setText(valiLabel);
    }

    private void getEet() {
        EetConfigModel eet = new EetDaoImpl().getEet();
        setDicTextField(eet.getDic());
        setPasswordField(eet.getPassword());
        setFileButton(eet.getPath());
        setStoreTextField(eet.getProvoz());
        setValidityLabel(eet.getValidity());
        setCashTextField(eet.getPokl());
        porad_cisTextField.setText(String.valueOf(billData.getPoradCisel()));
    }

    public void setCashTextField(String cashTextField) {
        this.cashTextField.setText(cashTextField);
    }

}
