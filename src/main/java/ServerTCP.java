import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    private static final int PORT = 5010;

    public static void main(String[] args) {
        System.out.println("Server was started on port: " + PORT);

        try(ServerSocket socket = new ServerSocket(PORT)){
            while(true){
                Socket clientSocket = socket.accept();
                Thread t = new Thread(new ClientThread(clientSocket));
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
