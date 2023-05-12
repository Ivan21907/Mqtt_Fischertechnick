package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

public class Mqtt_Local implements MqttCallback {

    private MqttClient client;
    private Connection conexion;

    public Mqtt_Local() {
        // Crea un nuevo cliente MQTT
        String broker = "tcp://192.168.0.10:1883";
        String clientId = "myClient";
        try {
            client = new MqttClient(broker, clientId);
            client.setCallback(this);
            // Conecta al broker
            client.connect();
            // Se suscribe al tema de la camara
            String inventoryTopic = "i/bme680";
            client.subscribe(inventoryTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        // Connectarse a la base de dades
        String url = "jdbc:mysql://localhost:3306/mqtt";
        String usuario = "root";
        String contrasena = "ivan2001";

        try {
            conexion = DriverManager.getConnection(url, usuario, contrasena);
        } catch (SQLException e) {
            System.out.println("Error al connectar a la base de dades: " + e.getMessage());
        }
    }

    public void connectionLost(Throwable cause) {
        System.out.println("S'ha perdut la connexió al broker MQTT.");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Es reb un missatge del broker del tema de la temperatura
        String payload = new String(message.getPayload());
        System.out.println("Mensaje recibido del tema " + topic + ": " + payload);
        // Pasa el missatge a un objecte JSON
        JSONObject json = new JSONObject(payload);
        double temperatura = json.getDouble("t");
        double humitat = json.getDouble("h");
        //String tiempo = json.getString("ts");

        // Mostra la informació de temperatura i humitat
        System.out.println("Temperatura: " + temperatura);
        System.out.println("Humitat: " + humitat);

        // Insertar les dades en la base de dades
        Statement statement = conexion.createStatement();
        String query = "INSERT INTO temp (Temperatura, Humitat) VALUES (" + temperatura + "," + humitat + ")";
        statement.executeUpdate(query);
        statement.close();
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        //Aquest mètode es crida quan s'ha entregat un missatge publicat
    }

    public static void main(String[] args) {
        new Mqtt_Local();
    }
}