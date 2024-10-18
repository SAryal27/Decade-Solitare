import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

/**
 * class DecadeWindow
 *
 * displays the current status of a Decade and handles user events
 *
 * ATTENTION:  Students may not edit this file.
 */
public class DecadeWindow extends JPanel implements MouseListener, ActionListener
{

    //These constants define how big the window is.
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 600;

    //These constants define how the cards are laid out
    public static final int COLS = 6;
    public static final int CARD_WIDTH = WINDOW_WIDTH / (COLS + 1);
    public static final int CARD_HEIGHT = 3 * CARD_WIDTH / 2;
    public static final int MARGIN = CARD_WIDTH / COLS;

    //font size for card graphics
    public static final Font cardFont = new Font("Serif", Font.BOLD, CARD_WIDTH / 5);

    //A reference to an instance the student's Decade class
    Decade decade = new Decade();

    //these variables specify which cards are highlighted
    int firstCard = 0;  //highlighting begins at this position
    int highlightLen = 0;  //number of cards highlighted

    //These are GUI controls that are modified during play
    JLabel totalText = new JLabel("Total: 0");
    JButton removeButton = new JButton("Remove");

    /** ctor asks the decade object to init the cards */
    public DecadeWindow() {
        Random gen = new Random();
        int numCards = 10 + gen.nextInt(10);
        decade.initCards(numCards);
    }
    
    /** initialize the controls on the user interface */
    public void initGUI(JFrame myFrame)
    {
        Container root = myFrame.getContentPane();
        Box topBox = new Box(BoxLayout.PAGE_AXIS);
        root.add(topBox);
        topBox.add(this);
        Box selectionBox = new Box(BoxLayout.LINE_AXIS);
        topBox.add(selectionBox);

        //Setup the total field
        selectionBox.add(totalText);
        selectionBox.add(Box.createHorizontalGlue());


        //Setup the "remove" button
        removeButton.setActionCommand("r");
        removeButton.addActionListener(this);
        removeButton.setEnabled(false); //button not active by default
        selectionBox.add(removeButton);
        
        //Setup the "hint" button
        JButton hintButton = new JButton("Hint");
        hintButton.setActionCommand("h");
        hintButton.addActionListener(this);
        selectionBox.add(hintButton);
        
        //Setup the "shuffle" button
        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.setActionCommand("s");
        shuffleButton.addActionListener(this);
        selectionBox.add(shuffleButton);

        //Setup the "close" button
        JButton closeButton = new JButton("Close");
        closeButton.setActionCommand("x");
        closeButton.addActionListener(this);
        selectionBox.add(closeButton);

        
        //listen for mouse clicks
        addMouseListener(this);

    }//initGUI

    /**
     * validCards
     *
     * @return null if the cards array is valid.  If not, a descriptive error is
     * returned.
     */
    private String validCards() {
        //Check for missing array
        if (decade == null) return "unable to create Decade object";
        if (decade.cards == null) return "cards array is null";
        if (decade.cards.length == 0) return "cards array is zero length";

        //check for invalid cards in the array
        for(int i = 0; i < this.decade.cards.length; ++i) {
            char c = this.decade.cards[i];
            if (Decade.DECK.indexOf(c) == -1) {
                return "cards array contains an invalid card: '" + c + " at index " + i;
            }
        }

        return null;
    }//validCards
    


    /**
     * calcXCoord
     *
     * calculates the X screen coordinate of the card at a given position
     */
    private int calcXCoord(int pos) {
        int row = pos % COLS;
        int xCoord = (row + 1) * MARGIN + row * CARD_WIDTH;
        return xCoord;
    }
    
    /**
     * calcYCoord
     *
     * calculates the Y screen coordinate of the card at a given position
     */
    private int calcYCoord(int pos) {
        int col = pos / COLS;
        int yCoord = (col + 1) * MARGIN + col * CARD_HEIGHT;
        return yCoord;
    }

    /**
     * calculates the position in the array from a given x,y coordinate
     *
     * @return -1 if there is no corresponding position
     */
    private int calcPos(int xCoord, int yCoord) {
        //check for invalid deck
        if (validCards() != null) return -1;
        
        int col = xCoord / (MARGIN + CARD_WIDTH);
        int row = yCoord / (MARGIN + CARD_HEIGHT);

        int pos = (row * COLS) + col;

        if ((pos < 0) || (pos >= decade.cards.length)) return -1;
        
        return pos;
    }//calcPos

    /**
     * drawCard
     *
     * draws a single card in the window at a specified position
     *
     * @param canvas  draw the card on this Graphics
     * @param x       x position (not pixel coordinate).  This should be a
     *                number between [0..numCards)
     * @param rank    a char representing the card's rank
     */
    public void drawCard(Graphics canvas, int x, char rank) {

        //Translate from row/col to screen coordinates
        int xCoord = calcXCoord(x);
        int yCoord = calcYCoord(x);
        
        canvas.setColor(Color.black);
        canvas.drawRect(xCoord, yCoord, CARD_WIDTH, CARD_HEIGHT);
        canvas.setColor(Color.white);
        canvas.fillRect(xCoord, yCoord, CARD_WIDTH, CARD_HEIGHT);
        canvas.setColor(Color.black);
        canvas.setFont(cardFont);
        canvas.drawString("" + rank,
                          xCoord + CARD_WIDTH / 3, yCoord + CARD_HEIGHT/3);
        
        
    }//drawCard

    /**
     * highlight
     *
     * draws a highlight rectangle around the appropriate cards
     */
    public void highlight(Graphics canvas) {
        //check for invalid deck
        if (validCards() != null) return;
        
        //check for invalid highlight
        if (highlightLen < 1) return;  //nothing highlighted
        if ( (firstCard < 0)
             || (firstCard + highlightLen > decade.cards.length) ) {
            highlightLen = 0;
            return;
        }

        canvas.setColor(Color.yellow);
        for(int i = firstCard; i < firstCard + highlightLen; ++i) {
            int xCoord = calcXCoord(i);
            int yCoord = calcYCoord(i);

            //draw the highlight (width 3)
            for(int j = 2; j < 5; ++j) {
                canvas.drawRect(xCoord - j, yCoord - j,
                                CARD_WIDTH + 2*j, CARD_HEIGHT + 2*j);
            }
        }
        
    }//highlight
       
    
    
    /**
     * paint
     *
     * draws the cards
     */
    @Override
    public void paint(Graphics canvas)
    {
        //A nice green background
        super.paint(canvas);
        setBackground(Color.green.darker());

        //Draw the cards
        //check for invalid deck
        String errMsg = validCards();
        if (errMsg  == null) {
            for(int i = 0; i < decade.cards.length; ++i) {
                drawCard(canvas, i, decade.cards[i]);
            }
            
            //highlight some cards as required
            highlight(canvas);
        }
        else {
            //warn the user about the invalid deck
            canvas.setColor(Color.yellow);
            canvas.drawString(errMsg, WINDOW_WIDTH / 7, WINDOW_HEIGHT / 2);
        }

        
    }//paint

    /**
     * updateDisplay
     *
     * updates the user interface for the user
     */
    public void updateDisplay() {
        
        //Update the card total
        int total = 0;
        if (this.highlightLen > 0) {
            total = decade.totalCards(this.firstCard, this.highlightLen);
        }
        totalText.setText("Total: " + total);

        //If the total is a valid decade, then enable the "Remove" button
        if (this.highlightLen >= 2) {
            boolean removeable = ( (total <= 30) && ((total % 10) == 0) );
            removeButton.setEnabled(removeable);
        }
        else {
            removeButton.setEnabled(false);
        }

        //Redraw the cards and highlights
        repaint();
        
    }//updateDisplay
    

    /**
     * actionPerformed
     *
     * when the user selects a new category or presses a button, this method is
     * called to handle it
     */
    public void actionPerformed(ActionEvent event) {
        char cmd = event.getActionCommand().charAt(0);
        if (cmd == 'x')  //close button
        {
            System.exit(0);
        }
        else if (cmd == 's') //shuffle button
        {
            this.decade.shuffle();
        }
        else if (cmd == 'h') //hint button
        {
            //find a decade
            int index = decade.findDecade();
            if (index == -1) { //no sequences left
                this.highlightLen = 0;
            }
            else {
                this.firstCard = index;
                this.highlightLen = 1;
            }
        }
        else if (cmd == 'r') //remove button
        {
            this.decade.removeCards(this.firstCard, this.highlightLen);
            this.highlightLen = 0;
        }
        
        updateDisplay();
    }//actionPerformed

    /**
     * This code is called whenever the user clicks the mouse
     */
    public void	mouseReleased(MouseEvent e)
    {
        //find out which card was clicked
        int x = e.getX();
        int y = e.getY();
        int pos = calcPos(x,y);

        //nothing was clicked so unselect all
        if (pos == -1) {
            this.highlightLen = 0;
        }

        //Does this click extend the highlight to the right?
        else if (pos == this.firstCard + this.highlightLen) {
            this.highlightLen++;
        }

        //Does this click extend the highlight to the left?
        else if (pos == this.firstCard - 1) {
            this.firstCard = pos;
            this.highlightLen++;
        }

        //Does this click remove a highlighted card from the left
        else if (pos == this.firstCard) {
            this.firstCard++;
            this.highlightLen--;
        }

        //Does this click remove a highlighted card from the right
        else if (pos == this.firstCard + this.highlightLen - 1) {
            this.highlightLen--;
        }

        //This must be a new highlight
        else {
            this.firstCard = pos;
            this.highlightLen = 1;
        }

        updateDisplay();
    }//mouseReleased

    
    //ignore these
    public void	mouseClicked(MouseEvent e) {}
    public void	mouseEntered(MouseEvent e) {}
    public void	mouseExited(MouseEvent e) {}
    public void	mousePressed(MouseEvent e) {}
    
    /**
     * This method creates a window frame and displays the Decade
     * app inside of it.  
     */
    public static void main(String[] args)
    {
        //Create a properly sized window for this program
        final JFrame myFrame = new JFrame("Decade Solitaire");
        myFrame.setSize(WINDOW_WIDTH+10, WINDOW_HEIGHT+30);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display a canvas in the window
        DecadeWindow canvas = new DecadeWindow();
        canvas.initGUI(myFrame);
        myFrame.setVisible(true);
        
    }//main
    
}//class DecadeWindow
