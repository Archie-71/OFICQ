import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField TextField;
    private JPasswordField PasswordField;
    private JRadioButton RadioButton2;
    private JRadioButton RadioButton1;
    private JLabel label1;
    private ButtonGroup buttonGroup1;

    public StartForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // 单击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        contentPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ENTER){
                    onOK();
                }
            }
        });
    }

    private void onOK() {
        if (login()) {
            dispose();

            if (buttonGroup1.isSelected(RadioButton1.getModel())) {
                OFICQClient oficqClient = new OFICQClient();
                JFrame frame = new JFrame("客户端");
                frame.setContentPane(new ClientForm(oficqClient).root);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(640, 480);
                frame.setVisible(true);
            }
            else if (buttonGroup1.isSelected(RadioButton2.getModel())) {
                OFICQServer OFICQServer = new OFICQServer();
                JFrame frame = new JFrame("服务器");
                frame.setContentPane(new ServerForm(OFICQServer).root);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(640, 480);
                frame.setVisible(true);
            }
        }
    }

    private void onCancel() {
        System.out.println("Login failed!");
        dispose();
    }

    private boolean login() {
        UserData data = new UserData();
        setData(data);
        if (!getData(data)) {
            TextField.repaint();
            PasswordField.repaint();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        StartForm dialog = new StartForm();
        dialog.setTitle("OFICQ Login");
        dialog.setSize(450, 420);
        dialog.setVisible(true);
//        System.exit(0);
    }

    private void createUIComponents() {
        label1 = new JLabel();
        ImageIcon icon = new ImageIcon("res/startPic.jpg");
        label1.setIcon(icon);
        label1.setVisible(true);
    }

    public void setData(UserData data) {
        if (data.isUserNameExist(TextField.getText())) {
            data.userNameExist = true;
            if (data.isLoginSuccess(String.valueOf(PasswordField.getPassword()))) {
                data.loginSuccess = true;
            }
        }
    }

    public boolean getData(UserData data) {
        return data.loginSuccess;
    }
}
