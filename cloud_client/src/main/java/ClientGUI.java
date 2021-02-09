import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {

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

        btnUpload.addActionListener(this);

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
        }else {// оставляем себе подсказку, что мы тыкнули в неизвестный источник. Так как по просшествии времени мы, возможно добавим еще кнопку.
            throw new RuntimeException("Unknown source: " + src);
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
