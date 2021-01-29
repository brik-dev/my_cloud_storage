import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler { //реализуем метод actionPerformed c) ->

    private static final int POS_X = 1000;
    private static final int POS_Y = 550;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 100;

    private final StorageServer chatServer = new StorageServer();

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");



    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() { // то есть тут у нас находится ананимный класс, реализующий интерфейс runnable -> почему через invokeLater?
            // -> мы делаем это чтобы отправить то, что находится внутри метода в специальный AWT Event Dispatching Thread
            // это специальный потом, отдельный, параллельный или нить исполнения, который будет осуществлять диспечеризацию событий в EDT. AWT - то такой графичесткий фреймворк, родитель свинга
            @Override
            public void run() {
                new ServerGUI();
            }
        });

    }

    private ServerGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(true);
        setTitle("Storage server");
        setAlwaysOnTop(true);
        setLayout(new GridLayout(1, 2));
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);
        add(btnStart);
        add(btnStop);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStop){
            chatServer.stop();
        }else if (src == btnStart){
            //throw new RuntimeException("Exception test");
            chatServer.start(8189);
        }else {
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
