package com.snipers.wheel.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.snipers.wheel.model.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.cloud.firestore.WriteResult;

import java.util.HashMap;
import java.util.Map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Service
public class PrizeService {

    @Autowired
    private Firestore firestore;

    public List<Prize> getAllPrizes() {
        List<Prize> prizes = new ArrayList<>();
        try {
            CollectionReference prizesCollection = firestore.collection("prizes");

            // Asynchronously retrieve all documents
            ApiFuture<QuerySnapshot> querySnapshot = prizesCollection.get();

            // Get the results
            for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Prize prize = document.toObject(Prize.class);
                prizes.add(prize);
                System.out.println("prize " +prize.getName());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return prizes;
    }

    // Method to save prize winner details to Firestore
    public void saveWinner(String name, String phone, String location, Prize winningPrize) {
        CollectionReference winnersCollection = firestore.collection("prizeWinners");

        // Get the current date in DD/MM/YYYY format
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        // Create a map of data to store in Firestore
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("phone", phone);
        data.put("location", location);
        data.put("prize", winningPrize.getName());
        data.put("spinDate", formattedDate); // Add the spin date

        // Asynchronously write data to Firestore
        ApiFuture<WriteResult> future = winnersCollection.document().set(data);
        try {
            future.get(); // This will block until the write is complete
            System.out.println("Winner data saved successfully.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public ByteArrayInputStream generateCsvForWinners() {
        List<QueryDocumentSnapshot> winnersList = fetchPrizeWinners();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Write CSV header
        writer.println("Name,Phone,Location,Prize,Spin Date");

        // Write CSV data
        for (QueryDocumentSnapshot winner : winnersList) {
            String name = winner.getString("name");
            String phone = winner.getString("phone");
            String location = winner.getString("location");
            String prize = winner.getString("prize");
            String spinDate = winner.getString("spinDate"); // Get the spin date


            writer.println(String.format("%s,%s,%s,%s,%s", name, phone, location, prize, spinDate));
        }

        writer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private List<QueryDocumentSnapshot> fetchPrizeWinners() {
        CollectionReference winnersCollection = firestore.collection("prizeWinners");
        ApiFuture<QuerySnapshot> querySnapshot = winnersCollection.get();
        try {
            return querySnapshot.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
