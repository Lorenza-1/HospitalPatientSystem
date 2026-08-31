/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hospitalpatientsystem;

import java.util.ArrayList;
import java.util.Comparator;
/**
 *
 * @author emeris
 */
public class HospitalSystem {
    private ArrayList<Patient> patients;
    private Bed[][]wardBeds;
    private static final int ROWS = 4;
    private static final int COLS = 5;
    private static final String WARD_NUMBER = "Ward-A";
    
    public HospitalSystem() {
        patients = new ArrayList<>();
        wardBeds = new Bed[ROWS][COLS];
        initializeBeds();
    }
    
    private void initializeBeds(){
        int bedNum = 1;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                String bedId = String.format("B%02d", bedNum++);
                wardBeds[i][j] = new Bed(bedId);
            }
        }
    }
    
    // === PATIENT MANAGEMENT === 
    
    public boolean registerPatient(Patient patient){
        if(findPatient(patient.getPatientId())!=null){
            return false; // Duplicate ID
        }
        patients.add(patient);
        return true;
    }
    
    public Patient findPatient(String patientId){
        for(Patient p : patients) {
            if(p.getPatientId().equalsIgnoreCase(patientId)){
                return p;
            }
        }
        return null;
    }
    
    public boolean updatePatient(String patientId, String firstName, String lastName, int age, String gender, String medicalCondition, PatientCategory category){
        Patient p = findPatient(patientId);
        if (p == null)return false;
        
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setAge(age);
        p.setGender(gender);
        p.setMedicalCondition(medicalCondition);
        p.setCategory(category);
        return true;
    }
    
    public boolean deletePatient(String patientId){
        Patient p = findPatient(patientId);
        if (p == null) return false;
        
        if(p instanceof Inpatient){
            Inpatient inpatient = (Inpatient)p;
            String bedId = inpatient.getBedNumber();
            if(bedId!=null&&!bedId.isEmpty()){
                releaseBed(bedId);
            }
        }
        patients.remove(p);
        return true;
    }
    
    public ArrayList<Patient> getAllPatients(){
        return new ArrayList<>(patients);
    }
    
    public int getTotalPatients(){
        return patients.size();
    }
    
    // === BED MANAGEMENT === 
    
    public String allocateBed(String patientId){
        Patient p = findPatient(patientId);
        if(p == null){
            return "Error: Patient not found.";
        }
        if(!(p instanceof Inpatient)){
            return "Error: Only inpatients can be allocated a bed.";
        }
        
        Inpatient inpatient = (Inpatient)p;
        if(inpatient.getBedNumber()!=null && !inpatient.getBedNumber().isEmpty()){
            return "Error: Patient already has a bed allocated.";
        }
        
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                if(!wardBeds[i][j].isOccupied()){
                    String bedId = wardBeds[i][j].getBedId();
                    wardBeds[i][j].occupy(patientId);
                    inpatient.setWardNumber(WARD_NUMBER);
                    inpatient.setBedNumber(bedId);
                    return "Bed " + bedId + " allocated successfully.";
                }
            }
        }
        return "Error: No beds available.";
    }
    
    public boolean releaseBed(String bedId){
        for (int i = 0; i < ROWS;i++){
            for(int j = 0; j < COLS;j++){
                if(wardBeds[i][j].getBedId().equalsIgnoreCase(bedId)){
                    if(!wardBeds[i][j].isOccupied()){
                        return false;
                    }
                    String patientId = wardBeds[i][j].getPatientId();
                    wardBeds[i][j].release();
                    
                    Patient p = findPatient(patientId);
                    if(p instanceof Inpatient){
                        ((Inpatient)p).setBedNumber(null);
                    }
                    return true;
            }
          }
        }
        return false;
    }
    
    public void displayWardLayout(){
        System.out.println("\n========== WARD LAYOUT ==========");
        System.out.println("Ward:" + WARD_NUMBER);
        for(int i = 0; i < ROWS; i++){
            for(int j = 0;j < COLS;j++){
                Bed bed = wardBeds[i][j];
                String status = bed.isOccupied()?"[X]":"[ ]";
                System.out.printf("%s %s  ", bed.getBedId(), status);
            }
            System.out.println();
        }
        
        System.out.println("=================================\n");
    }
    
    public ArrayList<Bed> getAvailableBeds(){
        ArrayList<Bed> available = new ArrayList<>();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS;j++){
                if(!wardBeds[i][j].isOccupied()){
                    available.add(wardBeds[i][j]);
                }
            }
        }
        return available;
    }
    
    public ArrayList<Bed> getOccupiedBeds(){
        ArrayList<Bed> occupied = new ArrayList<>();
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS;j++){
                if(wardBeds[i][j].isOccupied()){
                    occupied.add(wardBeds[i][j]);
                }
            }
        }
        return occupied;
    }
    
    public int getTotalOccupiedBeds(){
        int count = 0;
        for (int i = 0;i < ROWS;i++){
            for (int j = 0;j < COLS;j++){
                if(wardBeds[i][j].isOccupied())count++;
            }
        }
        return count;
    }
    public double getOccupancyPercentage(){
        return(getTotalOccupiedBeds()/20.0)*100.0;
   }
    // === SORTING === 
    
    public ArrayList<Patient> sortPatientsBySurname(){
        ArrayList<Patient> sorted = new ArrayList<>(patients);
        sorted.sort(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }
    
    public ArrayList<Patient> sortPatientsById(){
        ArrayList<Patient> sorted = new ArrayList<>(patients);
        sorted.sort(Comparator.comparing(Patient::getPatientId, String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }
    public Bed[][] getWardBeds(){
        return wardBeds;
    }
}
