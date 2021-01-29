import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JList fileList = new JList();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));

    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField("Natalia");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnLogout = new JButton("Logout");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    //private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField fileName = new JTextField();
    private final JButton btnUpload = new JButton("Upload");

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

        //*** Что должно быть у клиента?
        JScrollPane scrollFileList = new JScrollPane(fileList);
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(btnLogin);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogout);
        //panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(fileList, BorderLayout.WEST);
        panelBottom.add(btnUpload, BorderLayout.EAST);

        add(scrollFileList, BorderLayout.CENTER);
        //add(scrollUser, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (true){

        }else {// оставляем себе подсказку, что мы тыкнули в неизвестный источник. Так как по просшествии времени мы, возможно добавим еще кнопку.
            throw new RuntimeException("Unknown source: " + src);
        }
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
}
