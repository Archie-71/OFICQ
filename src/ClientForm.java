import javax.swing.*;

public class ClientForm {
    JPanel root;
    private JButton startButton;
    private JTextField sayField;
    private JButton sayButton;
    private JTextField ipField;
    private JTabbedPane tabbedPane1;
    private JTextArea privateChatArea;
    private JTextArea TextAreaOnline;
    private JTextArea groupChatArea;
    private JTextField portField;

    private int tabNum = 0;

    public ClientForm(OFICQClient oficqClient) {
        tabbedPane1.addChangeListener(e -> tabNum = tabbedPane1.getSelectedIndex());
        startButton.addActionListener(e -> {
            if (e.getSource() == startButton) {
                if (tabNum == 0) {
                    oficqClient.connect(privateChatArea,portField.getText(),ipField.getText());
                } else if (tabNum == 1) {
                    oficqClient.connect(groupChatArea,portField.getText(),ipField.getText());
                }
            }
        });
        sayButton.addActionListener(e -> {
            if (e.getSource() == sayButton) {
                oficqClient.sendData(sayField.getText());
            }
        });
    }

    public static void main(String[] args) {
        OFICQClient client = new OFICQClient();
        JFrame frame = new JFrame("客户端");
        frame.setContentPane(new ClientForm(client).root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
}
