import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Faal {

    @JsonProperty("Poem")
    private String poem;

    @JsonProperty("Interpretation")
    private String interpretation;
    public String getPoem() {
        return poem;
    }
    public void setPoem(String poem) {
        this.poem = poem;
    }
    public String getInterpretation() {
        return interpretation;
    }
    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public void fixPoem() {
        poem = poem.replace("\\n", "\n").replace("\\r", "\r");
    }
}

class FaalGUI extends JFrame implements ActionListener {

    private static final String GET_URL = "https://faal.spclashers.workers.dev/api";
    private JButton getFaalButton;
    private JButton backButton;

    public FaalGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("فالگیر");
        setFont(new Font("B TITR", Font.PLAIN, 14));
        initializeHomePage();
        setSize(700, 800);
        setVisible(true);
    }
    private static Faal getFaal() throws IOException {
        Faal faal = null;
        try {
            URL url = new URL(GET_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    content.append(inputLine);
                }
                String jsonContent = content.toString();
                faal = new ObjectMapper().readValue(jsonContent, Faal.class);
                faal.fixPoem();
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faal;
    }

    private void initializeHomePage() {
        JPanel homePage = new JPanel();
        homePage.setLayout(null);
        homePage.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        homePage.setBackground(new Color(240, 248, 255));

        JLabel intentionLabel = new JLabel("در دل خویش، آرزویی پروران!");
        intentionLabel.setFont(new Font("B TITR", Font.PLAIN, 20));
        intentionLabel.setBounds(230, 150, 320, 50);

        JLabel instructionLabel = new JLabel("زمانی که اندیشه\u200Cات به سرانجام رسید، با آرامش، راهی را که پیش روست بپیما.");
        instructionLabel.setFont(new Font("B TITR", Font.PLAIN, 14));
        instructionLabel.setBounds(150, 350, 400, 50);

        getFaalButton = new JButton("فال بگیر");
        getFaalButton.setFont(new Font("B TITR", Font.PLAIN, 14));
        getFaalButton.setBounds(235, 600, 200, 50);
        getFaalButton.addActionListener(this);
        getFaalButton.setFocusable(false);
        getFaalButton.setBackground(new Color(173, 216, 230));

        homePage.add(intentionLabel);
        homePage.add(instructionLabel);
        homePage.add(getFaalButton);

        this.add(homePage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new FaalGUI();
            } catch (Exception e) {
                System.out.println("An exception occurred: " + e.getMessage());
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getFaalButton) {
            getContentPane().removeAll();
            try {
                Faal faal = getFaal();
                displayFaalPage(faal.getPoem(), faal.getInterpretation());
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            revalidate();
            repaint();
        } else if (e.getSource() == backButton) {
            getContentPane().removeAll();
            initializeHomePage();
            revalidate();
            repaint();
        }
    }

    private void displayFaalPage(String poem, String interpretation) {
        JPanel faalPage = new JPanel();
        faalPage.setLayout(new BoxLayout(faalPage, BoxLayout.Y_AXIS));
        faalPage.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        faalPage.setBackground(new Color(240, 248, 255));

        faalPage.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel poemLabel = new JLabel("شعر:");
        poemLabel.setFont(new Font("B TITR", Font.PLAIN, 18));
        poemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea poemText = new JTextArea(poem);
        poemText.setFont(new Font("Vazirmatn", Font.PLAIN, 14));
        poemText.setEditable(false);
        poemText.setLineWrap(true);
        poemText.setWrapStyleWord(true);
        poemText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        poemText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JScrollPane poemScrollPane = new JScrollPane(poemText);
        poemScrollPane.setPreferredSize(new Dimension(640, 250));
        poemScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel interpretationLabel = new JLabel("تفسیر:");
        interpretationLabel.setFont(new Font("B TITR", Font.PLAIN, 18));
        interpretationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea interpretationText = new JTextArea(interpretation);
        interpretationText.setFont(new Font("Vazirmatn", Font.PLAIN, 14));
        interpretationText.setEditable(false);
        interpretationText.setLineWrap(true);
        interpretationText.setWrapStyleWord(true);
        interpretationText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        interpretationText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JScrollPane interpretationScrollPane = new JScrollPane(interpretationText);
        interpretationScrollPane.setPreferredSize(new Dimension(640, 100));
        interpretationScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton = new JButton("بازگشت");
        backButton.setFont(new Font("B TITR", Font.PLAIN, 14));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(this);
        backButton.setBackground(new Color(173, 216, 230));

        faalPage.add(poemLabel);
        faalPage.add(poemScrollPane);
        faalPage.add(Box.createRigidArea(new Dimension(0, 15)));
        faalPage.add(interpretationLabel);
        faalPage.add(interpretationScrollPane);
        faalPage.add(Box.createRigidArea(new Dimension(0, 15)));
        faalPage.add(backButton);

        this.setContentPane(faalPage);
    }

}









