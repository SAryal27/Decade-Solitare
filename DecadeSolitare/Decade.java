import java.util.Random;

/**
 * The Decade class manages an array of char that represents
 * the cards that have been dealt in a game of Decade Solitaire.
 *
 * @author  Shubu Aryal
 * @version 10/10/23
 */
public class Decade
{
    //Each "card" is represented by one character from DECK
    public static final String DECK = "A23456789TJQK";

    //The current cards will be stored in this array
    public char[] cards = null;
    public char[] newArray = null;

    /**
     * initCards
     *
     * populates the cards array with cards chosen randomly from the DECK.
     * It's okay if some cards appear more than once.
     *
     * @param size  how many cards should be placed in the array
     */
    public void initCards(int size) {
        //TODO: You will write this method
        Random rand = new Random();
        cards = new char[size];

        for (int i = 0; i < size; i++){
            int randomIndex = rand.nextInt(DECK.length());
            char randomChar = DECK.charAt(randomIndex);
            cards[i] = randomChar;
        }
    }//initCards

    /**
     * totalCards
     *
     * calculates the total value of a sequence of 1 or more cards
     *
     * @param pos   the index of the first card in sequence
     * @param num   the number of cards in the sequence
     */
    public int totalCards(int pos, int num) {
        //TODO: You will write this method
        int totalVal = 0;
        int cardValue;
        for (int i = pos; i < pos + num; i++){
            char currCard = cards[i];
            if (currCard == 'A'){
                cardValue = 1;
            }
            else if (currCard == '2'){
                cardValue = 2;
            }
            else if (currCard == '3'){
                cardValue = 3;
            }
            else if (currCard == '4'){
                cardValue = 4;
            }
            else if (currCard == '5'){
                cardValue = 5;
            }
            else if (currCard == '6'){
                cardValue = 6;
            }
            else if (currCard == '7'){
                cardValue = 7;
            }
            else if (currCard == '8'){
                cardValue = 8;
            }
            else if (currCard == '9'){
                cardValue = 9;
            }
            else{
                cardValue = 10;
            }
            totalVal += cardValue;
        }
        return totalVal;
    }//totalCards

    /**
     * removeCards
     *
     * removes a subsequence of cards from the array by
     * replacing it with a new array that lacks those cards
     *
     * @param pos  index of the first card to remove
     * @param num  the number of cards to remove
     */
    public void removeCards(int pos, int num) {
        //checks if the sequence ads up to 10,20 OR 30 then create a new array 
        // that doesn't have those cards in it.
        
        if ((totalCards(pos,num) == 10) || (totalCards(pos,num) == 20) || (totalCards(pos,num) == 30)){
            newArray = new char[cards.length];
            int newIndex = 0;
            for (int i = 0; i < cards.length; i++){
                if (!(i >= pos && i < pos + num)){
                    newArray[newIndex] = cards[i];
                    newIndex++;
                }
            }
            cards = new char[newIndex];
            for (int i = 0; i < newIndex; i++){
                cards[i] = newArray[i];
            }
        }   
    // a decade can be 10 but it can't be one card
    }//removeCards

    
    /**
     * return true if there is a decade (10,20,30) at the given position
     */
    public boolean hasDecade(int pos, int num){
        if ((totalCards(pos,num) == 10) || (totalCards(pos,num) == 20) || (totalCards(pos,num) == 30)){
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * findDecade
     *
     * searches the array for a sequence of 2 or more cards whose
     * value sums to 10, 20 or 30.
     *
     * @return the index of the first card in the sequence.  If no such sequence
     * exists the method returns -1 instead.
     */
    public int findDecade() {
        //TODO: You will write this method
        
        //USE HAS DECADE METHOD
        for (int i = 0; i < cards.length; i++){
            for (int j = 2; j <= cards.length - i; j++){
                if (hasDecade(i,j)){
                    return i;
                }
            }
        }        
        return -1;
    }//findDecade

    /**
     * shuffle
     *
     * shuffles the values in the array into a random order
     */
    public void shuffle() {
        //TODO: You will write this method
        Random rand = new Random();
        
        for (int i = 0; i < cards.length; i++){
            int randomIndex = rand.nextInt(i + 1);
            char temp = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = temp;
        }
        
    }//shuffle

}//class Decade
