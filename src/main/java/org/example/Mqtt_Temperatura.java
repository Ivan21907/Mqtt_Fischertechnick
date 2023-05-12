package org.example;

import org.eclipse.paho.client.mqttv3.*;

import org.json.JSONObject;


public class Mqtt_Temperatura implements MqttCallback {

    private MqttClient client;

    public Mqtt_Temperatura() {
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
    }

    public void connectionLost(Throwable cause) {
        System.out.println("Conexión perdida al broker MQTT.");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Se ha recibido un mensaje del tema de camara
        String payload = new String(message.getPayload());
        System.out.println("Mensaje recibido del tema " + topic + ": " + payload);
        // Parsea el mensaje como JSON
        JSONObject json = new JSONObject(payload);
        double temperatura = json.getDouble("t");
        double humedad = json.getDouble("h");
        //String tiempo = json.getString("ts");

// Imprime la información de temperatura y humedad
        System.out.println("Temperatura: " + temperatura);
        System.out.println("Humedad: " + humedad);
        //System.out.println("Hora/Dia: " + tiempo);

        Thread.sleep(15000);
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // Este método se llama cuando se ha entregado un mensaje publicado
    }

    public static void main(String[] args) {
        new Mqtt_Temperatura();
    }
}