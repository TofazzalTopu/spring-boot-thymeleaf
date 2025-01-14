package com.info.prescription.controller.api.expose;

import com.info.prescription.controller.PrescriptionController;
import com.info.prescription.model.Document;
import com.info.prescription.model.Prescription;
import com.info.prescription.service.PrescriptionService;
import com.info.prescription.service.encryptor.EncryptorDecryptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);

    @Autowired
    PrescriptionService prescriptionService;

    @Autowired
    EncryptorDecryptorService encryptorDecryptorService;

    // @Comment: This is Api endpoint to find a tutorial by id.
    @GetMapping(value = "/api/v1/findAllPrescription" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Prescription>> findAllPrescription() {
        List<Prescription> prescriptionList = prescriptionService.findAll();
        if (!prescriptionList.isEmpty()) {
            return new ResponseEntity<>(prescriptionList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @Comment: This is Api endpoint to find a tutorial by id.
    @GetMapping(value = "/api/v1/getPrescriptionById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable("id") Long id) {
        Prescription prescription = prescriptionService.findById(id);

        if (prescription != null) {
            return new ResponseEntity<>(prescription, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @Comment: This is Api endpoint to return all prescriptions as list.
    @GetMapping("/api/v1/findAllPrescriptionList/list")
    public ResponseEntity<List<Prescription>> findAllPrescriptionList() {
        try {
            List<Prescription> prescriptions = prescriptionService.findAll();
            if (prescriptions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<Prescription> prescriptionList = new ArrayList<>();
            prescriptions.forEach(prescription -> {
                prescription.setPatientName(encryptorDecryptorService.setEncryptedValue(prescription.getPatientName()));
                prescription.setMedicines(encryptorDecryptorService.setEncryptedValue(prescription.getMedicines()));
                prescription.setPatientAge(prescription.getPatientAge());
                prescription.setMedicines(encryptorDecryptorService.setEncryptedValue(prescription.getMedicines()));
                prescription.setDiagnosis(encryptorDecryptorService.setEncryptedValue(prescription.getDiagnosis()));
                prescription.setGender(encryptorDecryptorService.setEncryptedValue(prescription.getGender()));
                prescription.setPrescriptionDate(prescription.getPrescriptionDate());
                prescriptionList.add(prescription);
            });

            return new ResponseEntity<>(prescriptionList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    // @Comment: This is Api endpoint to update a Prescription by id.
    @PutMapping(value = "/api/v1/updatePrescriptionById/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Prescription> updatePrescription(@PathVariable("id") Long id, @RequestBody Prescription prescription) {
        Prescription prescriptionData = prescriptionService.findById(id);

        if (prescriptionData!=null) {

            return new ResponseEntity<>(prescriptionService.save(prescriptionData), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @Comment: This is Api endpoint to find a tutorial by id.
//    @GetMapping(value = "/api/v1/document" , produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/api/v1/document")
    public ResponseEntity<Document> getDocument() {
        Document document = new Document();
        document.setDocument("9705");
        document.setOrs_issue_id("5765");


        if (document != null) {
            return new ResponseEntity<>(document, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
