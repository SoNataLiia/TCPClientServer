import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ClientThread implements Runnable {
    private static final Path FILE_PATH = Paths.get("numbers.txt");
    private static final Object FILE_LOCK = new Object();

    private final Socket socket;

    public ClientThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            createFileIfNotExist();

            try(BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8
                    ));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),
                                StandardCharsets.UTF_8
                        )
                );
            ){

                writer.write("Enter your name: ");
                writer.newLine();
                writer.flush();

                String name = reader.readLine();

                if(name == null || name.isBlank()){
                    writer.write("ERROR: Nombre incorrecto.");
                    writer.newLine();
                    writer.flush();
                    return;
                }

                name = name.trim();

                writer.write("Bienvenido " + name + "!");
                writer.newLine();
                writer.flush();

                String line;
                while((line = reader.readLine()) != null){
                    line = line.trim();

                    if(line.equalsIgnoreCase("Salida")){
                        writer.write("OK: Cerrado..");
                        writer.newLine();
                        writer.flush();
                        return;
                    }

                    String response = handle(name, line);
                    writer.write(response);
                    writer.newLine();
                    writer.flush();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try{
                    socket.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createFileIfNotExist() throws IOException {
        if(!Files.exists(FILE_PATH)) Files.createFile(FILE_PATH);
    }

    private String handle(String clientName, String command){
        //1;num
        try {
            if (command.startsWith("1;")) {
                String strNum = command.substring(2).trim();
                int num = Integer.parseInt(strNum);

                synchronized (FILE_LOCK){
                    Files.writeString(
                            FILE_PATH,
                            clientName + ":" + num + System.lineSeparator(),
                            StandardCharsets.UTF_8,
                            StandardOpenOption.APPEND
                            );

                    return "OK: Número añadido";
                }
            }else if(command.equals("2")){
                synchronized (FILE_LOCK){
                    long count = Files.lines(FILE_PATH, StandardCharsets.UTF_8)
                            .filter(s -> !s.isBlank())
                            .count();

                    return "OK: consiguió " + count + " numero(-s)!";
                }
            }
            else if(command.equals("3")){
                synchronized (FILE_LOCK){
                    List<String> lines = Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8);
                    if(lines.isEmpty()) return "ERROR: vacío.";
                    return String.join("\\n", lines);
                }
            }
            else if(command.startsWith("4;")){
                String name = command.substring(2).trim();

                if(name.isBlank()){
                    return "ERROR: No hay nombre.";
                }
                //4;Ana
                synchronized (FILE_LOCK){
                    long count = Files.lines(FILE_PATH, StandardCharsets.UTF_8)
                            .filter(s -> s.startsWith(name + ":"))
                            .count();

                    return "OK: " + name + " consiguió " + count + " numero(-s)!";
                }
            }
            else if(command.equals("5")){
                synchronized (FILE_LOCK){
                    List<String> lines =
                            Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)
                                    .stream()
                                    .filter(s -> s.startsWith(clientName + ":"))
                                    .toList();
                    if(lines.isEmpty()) return "ERROR: Vacío.";
                    return String.join("\\n", lines);
                }
            }


            return "ERROR: Formato de comando u opción de comando no válido.";

        } catch (NumberFormatException e) {
            return "ERROR: Nombre invalido";
        }
        catch (Exception e){
            return "ERROR: " + e.getMessage();
        }
    }
}
