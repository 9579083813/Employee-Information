package com.contactapp.controller;

import com.contactapp.model.Contact;
import com.contactapp.repo.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

///api/contact/getAllContacts
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    ContactRepository contactRepository;

    @GetMapping("/getEmployeeContacts")
    public ResponseEntity<List<Contact>> getAllContacts() {
        try {
            List<Contact> contactList = new ArrayList<>();
            contactRepository.findAll().forEach(contactList::add);

            if (contactList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(contactList, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getEmployeeContactById/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contactObj = contactRepository.findById(id);
        if (contactObj.isPresent()) {
            return new ResponseEntity<>(contactObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addEmployeeContact")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        try {
            Contact contactObj = contactRepository.save(contact);
            return new ResponseEntity<>(contactObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateEmployeeContact/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact newContactData) {
        try {
            Optional<Contact> contactData = contactRepository.findById(id);
            if (contactData.isPresent()) {
                Contact updatedContactData = contactData.get();
                updatedContactData.setFirstname(newContactData.getFirstname());
                updatedContactData.setLastname(newContactData.getLastname());
                updatedContactData.setEmail(newContactData.getEmail());
                
                Contact contactObj = contactRepository.save(updatedContactData);
                return new ResponseEntity<>(contactObj, HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteEmployeeContactById/{id}")
    public ResponseEntity<HttpStatus> deleteContact(@PathVariable Long id) {
        try {
            contactRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAllEmployeeContacts")
    public ResponseEntity<HttpStatus> deleteAllContacts() {
        try {
            contactRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
