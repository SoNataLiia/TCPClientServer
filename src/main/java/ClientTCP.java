import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientTCP {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5010;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream(),
                             StandardCharsets.UTF_8
                     ));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(
                             socket.getOutputStream(),
                             StandardCharsets.UTF_8
                     ));
             Scanner sc = new Scanner(System.in)) {

            System.out.println(reader.readLine());
            String name = sc.nextLine().trim();
            writer.write(name);
            writer.newLine();
            writer.flush();

            System.out.println(reader.readLine());

            while(true){
                System.out.println("\nMenu:");
                System.out.println("""
                        1. Almacenar un número en un archivo 
                        2. Devolver cuántos números se han almacenado hasta el momento.
                        3. Devolver la lista de números almacenados.
                        4. Devolver el número de números almacenados por un cliente
                        5. Recibir un archivo, sólo, con sus números
                        6. Salida
                        """);

                String option = sc.nextLine().trim();
                String cmd;

                switch (option){
                    case "1" ->{
                        System.out.print("Introduce numero: ");
                        String num = sc.nextLine().trim();
                        cmd = "1;" + num;
                    }
                    case "2" -> cmd = "2";
                    case "3" -> cmd = "3";
                    case "4" ->{
                        System.out.print("Introduce nombre: ");
                        String otherName = sc.nextLine().trim();

                        cmd = "4;" + otherName;
                    }
                    case "5" -> cmd = "5";
                    case "6" ->{
                        cmd = "Salida";
                        writer.write(cmd);
                        writer.newLine();
                        writer.flush();
                        System.out.println("Server: " + reader.readLine());
                        return;
                    }
                    default -> {
                        System.out.println("Error!");
                        continue;
                    }
                }

                writer.write(cmd);
                writer.newLine();
                writer.flush();

                String response = reader.readLine();

                if(response == null){
                    System.out.println("Respuesta incorrecta ");
                    return;
                }

                System.out.println("Respuesta de servidor: " +
                        response.replace("\\n", "\n"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
