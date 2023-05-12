package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class proves implements MqttCallback {

    private MqttClient client;

    public proves() {
        // Crea un nuevo cliente MQTT
        String broker = "tcp://192.168.0.10:1883";
        String clientId = "myClient";
        try {
            client = new MqttClient(broker, clientId);
            client.setCallback(this);
            // Conecta al broker
            client.connect();
            // Se suscribe al tema de la camara
            String inventoryTopic = "f/i/stock";
            client.subscribe(inventoryTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectionLost(Throwable cause) {
        System.out.println("Conexión perdida al broker MQTT.");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Se ha recibido un mensaje del tema de camara
        String payload = new String(message.getPayload());
        //System.out.println("Mensaje recibido del tema " + topic + ": " + payload);
        // Analiza el mensaje para extraer la información del inventario
        JSONObject inventory = new JSONObject(payload);
        JSONArray stockItems = inventory.getJSONArray("stockItems");

        // Recorre la lista de stockItems para obtener la información del inventario
        for (int i = 0; i < stockItems.length(); i++) {
            JSONObject stockItem = stockItems.getJSONObject(i);
            JSONObject workpiece = stockItem.getJSONObject("workpiece");
            String type = workpiece.getString("type");
            String location = stockItem.getString("location");

            // Imprime la información del inventario de cada ubicación
            if (type.isEmpty()) {
                System.out.println("No hay pieza en la ubicación " + location);
            } else {
                System.out.println("Pieza de color " + type + " en la ubicación " + location);
            }
        }
        Thread.sleep(15000);
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // Este método se llama cuando se ha entregado un mensaje publicado
    }

    public static void main(String[] args) {
        new proves();
    }
}