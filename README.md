# 🔌 TCPClientServer — Cliente-Servidor TCP en Java

Proyecto Java multihilo que implementa un servidor TCP capaz de atender a varios clientes simultáneamente. Los clientes pueden almacenar números en un archivo compartido en el servidor y consultar estadísticas sobre ellos.

---

## 📁 Estructura del proyecto

```
TCPClientServer/
├── src/
│   └── main/
│       └── java/
│           ├── ServerTCP.java       # Servidor TCP multihilo
│           ├── ClientThread.java    # Hilo por cada cliente conectado
│           └── ClientTCP.java       # Aplicación cliente
├── numbers.txt                      # Archivo compartido de números (generado en runtime)
├── pom.xml
└── README.md
```

---

## ⚙️ Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java       | 23      |
| Maven      | 3.x     |

---

## 🚀 Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/SoNataLiia/TCPClientServer.git
cd TCPClientServer
```

### 2. Compilar

```bash
mvn compile
```

### 3. Iniciar el servidor (primero)

```bash
mvn exec:java -Dexec.mainClass="ServerTCP"
```

### 4. Iniciar el cliente (en otra terminal)

```bash
mvn exec:java -Dexec.mainClass="ClientTCP"
```

> Se pueden abrir **varias terminales** con el cliente para probar la concurrencia.

---

## 💬 Protocolo de comunicación

Al conectarse, el servidor solicita el nombre del cliente. A continuación el cliente puede enviar comandos del menú:

| Opción | Comando enviado | Descripción |
|--------|----------------|-------------|
| `1`    | `1;<número>`   | Almacena un número en `numbers.txt` |
| `2`    | `2`            | Devuelve el total de números almacenados |
| `3`    | `3`            | Devuelve la lista completa de números |
| `4`    | `4;<nombre>`   | Devuelve cuántos números almacenó un cliente concreto |
| `5`    | `5`            | Devuelve solo los números del cliente actual |
| `6`    | `Salida`       | Cierra la conexión |

---

## 📌 Ejemplo de uso

**Servidor:**
```
Server was started on port: 5010
```

**Cliente:**
```
Enter your name:
> Ana
Bienvenido Ana!

Menu:
1. Almacenar un número en un archivo
2. Devolver cuántos números se han almacenado hasta el momento.
...

> 1
Introduce numero: 42
Respuesta de servidor: OK: Número añadido

> 2
Respuesta de servidor: OK: consiguió 1 numero(-s)!

> 6
Server: OK: Cerrado..
```

**numbers.txt** (generado en el servidor):
```
Ana:42
Pedro:7
Ana:15
```

---

## 🧵 Concurrencia

El servidor crea un nuevo `Thread` por cada cliente conectado (`ClientThread implements Runnable`). El acceso al archivo `numbers.txt` está protegido con `synchronized (FILE_LOCK)` para evitar condiciones de carrera entre hilos.

```
ServerTCP
  ├── ClientThread (cliente 1) ──┐
  ├── ClientThread (cliente 2) ──┤── FILE_LOCK ──► numbers.txt
  └── ClientThread (cliente N) ──┘
```

---

## 👩‍💻 Autora

**Nataliia Sokhatska**  
[GitHub](https://github.com/SoNataLiia)