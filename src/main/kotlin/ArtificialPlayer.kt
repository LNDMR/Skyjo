class ArtificialPlayer(name: String, currentCards: PlayingField = PlayingField(mutableListOf()),
                       score: Int = 0, terminate: Boolean = false)
                        : Player(name, currentCards, score, terminate) {


    override fun chooseCard(): Int{
        return currentCards.references[currentCards.references.keys.random()]!!
    }


    override fun flipCard(i: Int) {
        if (!currentCards.cards[i].flipped) {
            currentCards.cards[i].turnOver()
        } else {
            var tryagain: Int
            do {
                tryagain = chooseCard()
            } while (currentCards.cards[tryagain].flipped)
            currentCards.cards[tryagain].turnOver()
        }
    }


    override fun drawCard(discardPile: MutableList<Card>, pile: MutableList<Card>): Card {
        var newCard: Card
        println()
        // TODO: FUTURE: make smarter by checking the discardPile [a. for a small value, b. to potentially complete a column]
        var choice = listOf(1,2).random()
        if(choice == 1){
            newCard = pile.removeFirst()
            println("The cpu draws a card from the draw pile.")
        } else {
            newCard = discardPile.removeLast()
            println("The cpu picks up the card on top of the discard pile.")
        }
        newCard.turnOver()
        return newCard
    }


    override fun keepOrDismiss(newCard: Card, discardPile: MutableList<Card>) {
        println()
        // TODO: FUTURE: make cpu smarter by comparing the card's value
        var choice = listOf(1,2).random()
        if(choice == 1){
            println("The cpu decided to swap the new card with a card on the playing field.")
            dismissCard(currentCards.swapCard(currentCards.cards[chooseCard()], newCard),discardPile)
        } else {
            println("The cpu decided to turn over a hidden card on the playing field.")
            flipCard(chooseCard())
            dismissCard(newCard, discardPile)
        }
    }


    override fun wantToCloseRound(discardPile: MutableList<Card>){
        println()
        endOfTurnCheck(discardPile)
        if (currentCards.allCardsRevealed){
            terminate = true
        } else {
            // TODO: FUTURE: make smarter by comparing the current scores
            //var close: String= listOf("yes","no").random()
            var close: String= "no"
            terminate = close == "yes"
        }
    }
}
