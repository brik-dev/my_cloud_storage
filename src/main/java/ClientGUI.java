import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;


    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));

    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField("Natalia");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnLogout = new JButton("Logout");


    private final JTextArea fileList = new JTextArea();


    private final JPanel panelRight = new JPanel(new GridLayout(5,1));

    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnRename = new JButton("Rename");


    private final JPanel panelBottom = new JPanel(new BorderLayout());

    private final JButton btnSelect = new JButton("Select");
    private final JTextField tfName = new JTextField();
    private final JButton btnUpload = new JButton("<html><b>Upload</b></html>");

    SocketThread socketThread;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);

        // расположение панелей, кнопок и полей
        JScrollPane scrollFileList = new JScrollPane(fileList);
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(btnLogin);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogout);
        panelRight.add(btnDelete);
        panelRight.add(btnRename);
        panelBottom.add(btnSelect, BorderLayout.WEST);
        panelBottom.add(tfName, BorderLayout.CENTER);
        panelBottom.add(btnUpload, BorderLayout.EAST);

        add(scrollFileList, BorderLayout.CENTER);
        add(panelRight, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        btnLogin.addActionListener(this);
        btnUpload.addActionListener(this);
        //add(btnLogin);

        setVisible(true);
    }

    //uploadFile()

    //deleteFile()

    //editFile()

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == null) return;
        if (src == btnUpload){
            upload();
        }else if (src == btnLogin){
            connect();
        } else {// оставляем себе подсказку, что мы тыкнули в неизвестный источник. Так как по просшествии времени мы, возможно добавим еще кнопку.
            throw new RuntimeException("Unknown source: " + src);
        }
    }

    private void connect(){
        try{
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            // сокет создали и если мы попали на этострочку, значит сокет соединился с сервером и мы его оборачиваем в SocketThread

            socketThread = new SocketThread(this, "Client", socket);
            // теперь сентмаседж должен отправлять в socketThread message
            // -> делаем его видимым e) ->
        }catch (IOException e){ // если что, ловим ИО ексепшн
            showException(Thread.currentThread(), e); // по факту IOException показываем IOException

        }
    }

    private void upload() {
        String name = tfName.getText();
        if ("".equals(name)) return;
        putFileList(name);
        uploadToServer();
    }

    private void putFileList(final String name) {
        if ("".equals(name)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fileList.append (name + "\n");
                fileList.setCaretPosition(fileList.getDocument().getLength());
            }
        });
    }

    private void uploadToServer() {

    }
    private void showException(Thread t, Exception e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
            JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        msg = "Exception in " + t.getName() + " " +
                e.getClass().getCanonicalName() + ": " +
                e.getMessage() + "\n\t at " + ste[0];
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    /**
     * Socket thread listener methods
     * */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        System.out.println("OnSocketStart");//putLog("Start"); -> store in log file
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        System.out.println("OnSocketStop");//putLog("Stop"); -> store in log file
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        System.out.println("OnSocketReady");//putLog("Ready"); -> store in log file
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        System.out.println("OnReceiveString");//putLog(msg); -> store in log file
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        showException(thread, exception);
    }
}
