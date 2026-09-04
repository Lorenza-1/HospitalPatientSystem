/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hospitalpatientsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 *
 * @author emeris
 */
public class HospitalSystemTest {
    
    private HospitalSystem system; 
    
    @BeforeEach
    public void setUp(){
        system = new HospitalSystem();
    }
    
    @Test 
    public void testRegisterPatient(){
        Patient p = new Patient("P001", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        assertTrue(system.registerPatient(p));
        assertNotNull(system.findPatient("P001"));
    }
    
    @Test
    public void testSearchPatient(){
        Patient p = new Patient("P002", "Jane","Smith",25,"Female","Cold",PatientCategory.OUTPATIENT);
        system.registerPatient(p);
        Patient found = system.findPatient("P002");
        assertNotNull(found);
        assertEquals("Jane", found.getFirstName());
    }
    
    @Test
    public void testUpdatePatient(){
        Patient p = new Patient("P003","Bob","Brown",40,"Male","Asthma",PatientCategory.OUTPATIENT);
        system.registerPatient(p);
        assertTrue(system.updatePatient("P003", "Robert", "Brown", 41, "Male", "Asthma", PatientCategory.OUTPATIENT));
        Patient updated = system.findPatient("P003");
        assertEquals("Robert", updated.getFirstName());
        assertEquals(41, updated.getAge());
    }
    
    @Test
    public void testDeletePatient(){
        Patient p = new Patient("P004", "Alice", "Green", 35, "Female", "Fever", PatientCategory.OUTPATIENT);
        system.registerPatient(p);
        assertTrue(system.deletePatient("P004"));
        assertNull(system.findPatient("P004"));
    }
    
    @Test
    public void testAllocateBed(){
        Inpatient ip = new Inpatient("P005", "Tom", "White", 50, "Male", "Surgery", PatientCategory.INPATIENT, "Not Assigned", "Not Assigned");
        system.registerPatient(ip);
        String result = system.allocateBed("P005");
        assertTrue(result.contains("allocated successfully"));
        Inpatient updated = (Inpatient)system.findPatient("P005");
        assertNotNull(updated.getBedNumber());
        assertFalse(updated.getBedNumber().equalsIgnoreCase("Not Assigned"));
    }
    
    @Test
    public void testReleaseBed() {
        Inpatient ip = new Inpatient("P006", "Sue", "Black", 45, "Female", "Recovery", PatientCategory.INPATIENT, "Not Assigned", "Not Assigned");
        system.registerPatient(ip);
        system.allocateBed("P006");
        String bedId = ((Inpatient) system.findPatient("P006")).getBedNumber();
        assertTrue(system.releaseBed(bedId));
        assertEquals(0, system.getTotalOccupiedBeds());
    }
    
    @Test
    public void testPreventDuplicatePatientIds(){
        Patient p1 = new Patient("P007", "First", "User", 20, "Male", "A", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P007", "Second", "User", 21, "Female", "B", PatientCategory.OUTPATIENT);
        assertTrue(system.registerPatient(p1));
        assertFalse(system.registerPatient(p2));
    }
    
    @Test
    public void testPreventAllocatingOccupiedBed(){
        // Register two inpatients
        Inpatient ip1 = new Inpatient("P008", "A", "One", 30, "Male", "X", PatientCategory.INPATIENT, "Not Assigned", "Not Assigned");
        Inpatient ip2 = new Inpatient("P009", "B", "Two", 31, "Female", "Y", PatientCategory.INPATIENT, "Not Assigned", "Not Assigned");
        system.registerPatient(ip1);
        system.registerPatient(ip2);
        
        // Allocate first bed
        system.allocateBed("P008");
        String bed1 = ((Inpatient) system.findPatient("P008")).getBedNumber();
        
        //Try to allocate again to same patient (should fail)
        String result = system.allocateBed("P008");
        assertEquals("Error: Patient already has a bed allocated.", result);
        
        //Allocate second patient - must get a different bed
        system.allocateBed("P009");
        String bed2 = ((Inpatient) system.findPatient("P009")).getBedNumber();
        assertNotEquals(bed1, bed2);
    }
    
    @Test 
    public void testPreventBedAllocationWhenAllBedsOccupied(){
        // Fill all 20 beds
        for (int i = 1; i <= 20; i++){
            String id = String.format("PF%02d", i);
            Inpatient ip = new Inpatient(id, "F" + i, "L" + i, 20 + i, "Male", "Condition", PatientCategory.INPATIENT, "Not Assigned", "Not Assigned");
            system.registerPatient(ip);
            String result = system.allocateBed(id);
            assertTrue(result.contains("allocated successfully"), "Failed on patient" + id);
        }
        
        //Try 21st patient 
        Inpatient ip21 = new Inpatient("P21", "Extra", "Patient", 99, "Female", "Full", PatientCategory.INPATIENT, "Not Assigned", "Not Assigned");
        system.registerPatient(ip21);
        String result = system.allocateBed("P21");
        assertEquals("Error: No beds available.", result);
    }
    
    @Test 
    public void testSortPatientsBySurname(){
        system.registerPatient(new Patient("P010", "Charlie", "Adams", 20, "M", "A", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P011", "Bravo", "Zulu", 21, "M", "B", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P012", "Alpha", "Middle", 22, "M", "C", PatientCategory.OUTPATIENT));
        
        ArrayList<Patient>sorted = system.sortPatientsBySurname();
        assertEquals("Adams", sorted.get(0).getLastName());
        assertEquals("Middle", sorted.get(1).getLastName());
        assertEquals("Zulu", sorted.get(2).getLastName());
    }
    
    @Test
    public void testSortPatientsById(){
        system.registerPatient(new Patient("P100", "Z", "Z", 20, "M", "A", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P010", "A", "A", 21, "M", "B", PatientCategory.OUTPATIENT));
        system.registerPatient(new Patient("P050", "M", "M", 22, "M", "C", PatientCategory.OUTPATIENT));
        
        ArrayList<Patient> sorted = system.sortPatientsById();
        assertEquals("P010", sorted.get(0).getPatientId());
        assertEquals("P050", sorted.get(1).getPatientId());
        assertEquals("P100", sorted.get(2).getPatientId());
    }
}
