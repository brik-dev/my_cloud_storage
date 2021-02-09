import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class StorageServer implements ServerSocketThreadListener, SocketThreadListener {

    ServerSocketThread thread;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");

    public void start(int port){
        if (thread != null && thread.isAlive()) {
            System.out.println("Server already started");
        } else {
            thread = new ServerSocketThread(this,"Thread of server", 8189, 2000);
        }
    }

    public void stop(){
        if (thread == null || !thread.isAlive()) {
            System.out.println("Server is not running");
        } else {
            thread.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() + ": " + msg;
        System.out.println(msg);
    }

    /**
     * Server methods
     *
     * */

    //* пока что для все переопределенных методов мы будем логировать
    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server socket created");
    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {
        //putLog("Server timeout");
    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connected");
        String name = "SocketThread " + socket.getInetAddress() + ":" + socket.getPort();
        new SocketThread(this, name, socket);
        // тут, когда произошло соединение, мы совершенно точно можем создавать сокеты. С нашей серверной стороны
        // Когда к нам кто-то присоединился, мы говорим, у нас есть новый сокет тхред, чувак какой-то и слушатель этого нового тхреда - это мы сами
        // то есть этот сокет тхред, окторый создался будет отправлять свои сообщеньки нам
    }

    /**
     * мы по факту будем логировать, кроме одного единственного метода по факту получения строки от клиента, мы будем отправлять этому же клиенту его сообщение с подаписью эхо
     */

    @Override
    public synchronized void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket created");

    }

    @Override
    public synchronized void onSocketStop(SocketThread thread) {
        putLog("Socket stopped");

    }

    @Override
    public synchronized void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");

    }

    @Override
    public synchronized void onReceiveString(SocketThread thread, Socket socket, String msg) {
        thread.sendMessage("echo: " + msg);
    }

    @Override
    public synchronized void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();
    }

}
