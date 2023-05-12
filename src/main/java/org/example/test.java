package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class test {

    static Firestore db;

    public static void conectar() throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount =
                new FileInputStream("provesFirebase.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
        System.out.println("Conectado a la base de datos de Firebase.");
        // Crear un documento en la colección "proves" con los campos "Humitat" y "Temperatura"
        DocumentReference documentRef = db.collection("proves").document("test");
        Map<String, Object> data = new HashMap<>();
        data.put("Humitat", 65.66);
        data.put("Temperatura", 25);

        // Insertar el documento en la base de datos
        ApiFuture<WriteResult> result = documentRef.set(data);


        WriteResult writeResult = result.get();
        System.out.println("Documento creado con ID: " + documentRef.getId());
        //eliminar document
        /*ApiFuture<WriteResult> writeResult = db.collection("proves").document("new").delete();
        System.out.println("Update time : " + writeResult.get().getUpdateTime());*/

    }
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        conectar();
    }
}