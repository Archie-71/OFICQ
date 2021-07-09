import javax.swing.*;
import java.io.IOException;

public class ServerForm {
    private int tabNum = 0;

    JPanel root;
    private JButton startButton;
    private JTextField sayField;
    private JButton sayButton;
    private JTextField portTextField;
    private JTabbedPane tabbedPane1;
    private JTextArea privateChatArea;
    private JTextArea textAreaOnline;
    private JTextArea groupChatArea;

    public ServerForm(final OFICQServer oficqServer) {
        tabbedPane1.addChangeListener(e -> tabNum = tabbedPane1.getSelectedIndex());

        startButton.addActionListener(e -> {
            if (e.getSource() == startButton) {
                try {
                    if (tabNum == 0) {
                        oficqServer.getClientsList().clear();
                        oficqServer.server = oficqServer.serverStart(portTextField.getText(), privateChatArea);
                        oficqServer.connectClient(privateChatArea,null);
                    } else if (tabNum == 1) {
                        oficqServer.clientsList.clear();
                        oficqServer.serverStart(portTextField.getText(), groupChatArea);
                        oficqServer.connectClient(groupChatArea, textAreaOnline);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        sayButton.addActionListener(e -> {
            if (e.getSource() == sayButton) {
                if (tabNum == 0){
                    oficqServer.sendData(sayField.getText(),0);
                }
                else if (tabNum == 1){
                    oficqServer.send2All(sayField.getText());
                }
            }
        });
    }

    public static void main(String[] args) {
        OFICQServer testServer = new OFICQServer();

        JFrame frame = new JFrame("服务器");
        frame.setContentPane(new ServerForm(testServer).root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
}

