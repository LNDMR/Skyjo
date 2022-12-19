class Card(var front: Int, val BACK: String = "X", var flipped: Boolean = false) {

    // the display of the card is set to the mark of
    var surface: String = BACK

    // flips the card to reveal its value
    fun turnOver(){
        surface = front.toString()
        flipped = true
        print("The card's value is: ")
        display()
    }

    // displays either the back or front of a card depending on whether it has been flipped or not
    fun display(){
        println(surface)
    }

}





